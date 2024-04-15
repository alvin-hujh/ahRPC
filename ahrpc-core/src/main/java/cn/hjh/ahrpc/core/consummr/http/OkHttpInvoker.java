package cn.hjh.ahrpc.core.consummr.http;

import cn.hjh.ahrpc.core.api.RpcRequest;
import cn.hjh.ahrpc.core.api.RpcResponse;
import cn.hjh.ahrpc.core.consummr.HttpInvoker;
import com.alibaba.fastjson.JSON;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName : OkHttpInvoker
 * @Description :
 * @Author : hujh
 * @Date: 2024-04-05 20:43
 */
public class OkHttpInvoker implements HttpInvoker {

    final static MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client;

    public OkHttpInvoker() {
        client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(8, 60, TimeUnit.SECONDS))
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.SECONDS)
                .connectTimeout(1, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public RpcResponse<?> post(RpcRequest rpcRequest, String url) {
        String reqJson = JSON.toJSONString(rpcRequest);
        System.out.println("=== reqURL ===" + url);
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
