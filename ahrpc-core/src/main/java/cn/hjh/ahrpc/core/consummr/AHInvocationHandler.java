package cn.hjh.ahrpc.core.consummr;

import cn.hjh.ahrpc.core.api.RpcRequest;
import cn.hjh.ahrpc.core.api.RpcResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName : AHInvocationHandler
 * @Description :
 * @Author : hujh
 * @Date: 2024-03-15 00:26
 */
public class AHInvocationHandler implements InvocationHandler {
    final static MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");

    Class<?> service;

    public AHInvocationHandler(Class<?> clazz) {
        this.service = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (method.getName().equals("toString")||
                method.getName().equals("getClass")||
                method.getName().equals("hashCode")||
                method.getName().equals("equals")||
                method.getName().equals("clone")||
                method.getName().equals("notify")||
                method.getName().equals("notifyAll")||
                method.getName().equals("wait")
        ){
            return null;
        }

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setService(service.getCanonicalName());
        rpcRequest.setMethod(method.getName());
        rpcRequest.setArgs(args);

        RpcResponse rpcResponse = post(rpcRequest);
        if (rpcResponse.status) {
            Object data = rpcResponse.getData();
            if (data instanceof JSONObject) {
                JSONObject jsonResult = (JSONObject) rpcResponse.getData();
                return jsonResult.toJavaObject(method.getReturnType());
            } else {
                return data;
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

    private RpcResponse post(RpcRequest rpcRequest) {
        String reqJson = JSON.toJSONString(rpcRequest);
        System.out.println("=== reqJson ===" + reqJson);
        Request request = new Request.Builder()
                .url("http://localhost:8080/")
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
