package cn.hjh.ahrpc.core.consummr;

import cn.hjh.ahrpc.core.api.RpcContext;
import cn.hjh.ahrpc.core.api.RpcRequest;
import cn.hjh.ahrpc.core.api.RpcResponse;
import cn.hjh.ahrpc.core.consummr.http.OkHttpInvoker;
import cn.hjh.ahrpc.core.meta.InstanceMeta;
import cn.hjh.ahrpc.core.util.MethodUtils;
import cn.hjh.ahrpc.core.util.TypeUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @ClassName : AHInvocationHandler
 * @Description :
 * @Author : hujh
 * @Date: 2024-03-15 00:26
 */
@Slf4j
public class AHInvocationHandler implements InvocationHandler {
    final static MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");

    Class<?> service;
    RpcContext rpcContext;
    List<InstanceMeta> providers;

    HttpInvoker httpInvoker = new OkHttpInvoker();

    public AHInvocationHandler(Class<?> clazz, RpcContext rpcContext, List<InstanceMeta> providers) {
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

        List<InstanceMeta> instances = rpcContext.getRouter().route(providers);
        InstanceMeta instance = rpcContext.getLoadBalancer().choose(instances);
        log.info("consumer invoker instance = ${}", instance);

        RpcResponse rpcResponse = httpInvoker.post(rpcRequest, instance.toUrl());
        if (rpcResponse.status) {
            Object data = rpcResponse.getData();
            return TypeUtils.castMethodResult(method, rpcResponse, data);
        } else {
            Exception ex = rpcResponse.getEx();
            throw new RuntimeException(ex.getMessage());
        }
    }
}
