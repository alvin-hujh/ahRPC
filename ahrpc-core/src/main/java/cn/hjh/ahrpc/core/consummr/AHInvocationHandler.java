package cn.hjh.ahrpc.core.consummr;

import cn.hjh.ahrpc.core.api.*;
import cn.hjh.ahrpc.core.util.MethodUtils;
import cn.hjh.ahrpc.core.util.TypeUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static cn.hjh.ahrpc.core.util.TypeUtils.cast;

/**
 * @ClassName : AHInvocationHandler
 * @Description :
 * @Author : hujh
 * @Date: 2024-03-15 00:26
 */
public class AHInvocationHandler implements InvocationHandler {
    final static MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");

    Class<?> service;
    RpcContext rpcContext;
    List<String> providers;

    public AHInvocationHandler(Class<?> clazz, RpcContext rpcContext, List<String> providers) {
        this.service = clazz;
        this.rpcContext = rpcContext;
        this.providers = providers;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (MethodUtils.checkLocalMethod(method.getName())) {
            return null;
        }

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setService(service.getCanonicalName());
        rpcRequest.setMethodSign(MethodUtils.methodSign(method));
        rpcRequest.setArgs(args);

        List<String> urls = rpcContext.getRouter().route(providers);
        String url = (String) (rpcContext.getLoadBalancer().choose(urls));

        RpcResponse rpcResponse = post(rpcRequest, url);
        if (rpcResponse.status) {
            Object data = rpcResponse.getData();
            Class<?> type = method.getReturnType();
            if (data instanceof JSONObject) {
                JSONObject jsonResult = (JSONObject) rpcResponse.getData();
                return jsonResult.toJavaObject(method.getReturnType());
            } else if (data instanceof JSONArray jsonArray) {
                Object[] array = jsonArray.toArray();
                if (type.isArray()) {
                    Class<?> componentType = type.getComponentType();
                    Object resultArray = Array.newInstance(componentType, array.length);
                    for (int i = 0; i < array.length; i++) {
                        if (componentType.isPrimitive() || componentType.getPackageName().startsWith("java")) {
                            Array.set(resultArray, i, array[i]);
                        } else {
                            Object castObject = cast(array[i], componentType);
                            Array.set(resultArray, i, castObject);
                        }
                    }
                    return resultArray;
                } else if (List.class.isAssignableFrom(type)) {
                    List<Object> resultList = new ArrayList<>(array.length);
                    Type genericReturnType = method.getGenericReturnType();
                    System.out.println(genericReturnType);
                    if (genericReturnType instanceof ParameterizedType parameterizedType) {
                        Type actualType = parameterizedType.getActualTypeArguments()[0];
                        System.out.println(actualType);
                        for (Object o : array) {
                            resultList.add(cast(o, (Class<?>) actualType));
                        }
                    } else {
                        resultList.addAll(Arrays.asList(array));
                    }
                    return resultList;
                } else {
                    return null;
                }
            } else {
                return TypeUtils.cast(data, method.getReturnType());
            }
        } else {
            Exception ex = rpcResponse.getEx();
//            ex.printStackTrace();
//            System.out.println(ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    OkHttpClient client = new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(8, 60, TimeUnit.SECONDS))
            .readTimeout(1, TimeUnit.SECONDS)
            .writeTimeout(1, TimeUnit.SECONDS)
            .connectTimeout(1, TimeUnit.SECONDS)
            .build();

    private RpcResponse post(RpcRequest rpcRequest, String url) {
        String reqJson = JSON.toJSONString(rpcRequest);
        System.out.println("=== reqJson ===" + reqJson);
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(reqJson, JSON_TYPE))
                .build();
        RpcResponse rpcResponse;
        try {
            String responseJson = client.newCall(request).execute().body().string();
            System.out.println("=== respJson ===" + responseJson);
            rpcResponse = JSON.parseObject(responseJson, RpcResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rpcResponse;
    }
}
