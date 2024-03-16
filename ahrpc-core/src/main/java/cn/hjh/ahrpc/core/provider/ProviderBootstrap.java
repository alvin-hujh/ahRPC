package cn.hjh.ahrpc.core.provider;

import cn.hjh.ahrpc.core.annotation.AHProvider;
import cn.hjh.ahrpc.core.api.RpcResponse;
import cn.hjh.ahrpc.core.api.RpcRequest;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName : ProviderBootstrap
 * @Description : 服务提供者功能实现
 * @Author : hujh
 * @Date: 2024-03-10 23:09
 */
@Data
public class ProviderBootstrap implements ApplicationContextAware {

    ApplicationContext applicationContext;

    private Map<String, Object> skeleton = new HashMap<>();

    @PostConstruct
    public void buildProviders() {
        Map<String, Object> providers = applicationContext.getBeansWithAnnotation(AHProvider.class);
        providers.forEach((x, y) -> System.out.println(x));
//        skeleton.putAll(providers);
        providers.values().forEach(
                x -> getInstance(x)
        );

    }

    private void getInstance(Object x) {
        Class<?> itfer = x.getClass().getInterfaces()[0];
        skeleton.put(itfer.getCanonicalName(), x);
    }

    public RpcResponse invoke(RpcRequest request) {
        Object bean = skeleton.get(request.getService());
        RpcResponse rpcResponse = new RpcResponse();
        Method method = findMethod(bean.getClass(), request.getMethod());
        //bean.getClass().getMethod(request.getMethod());
        try {
            assert method != null;
            Object result = method.invoke(bean, request.getArgs());
            rpcResponse.setStatus(true);
            rpcResponse.setData(result);
        } catch (InvocationTargetException e) {
           rpcResponse.setStatus(false);
           rpcResponse.setEx(new RuntimeException(e.getTargetException().getMessage()));
        }catch (IllegalAccessException e){
            rpcResponse.setStatus(false);
            rpcResponse.setEx(new RuntimeException(e.getMessage()));
        }
        return rpcResponse;
    }

    private Method findMethod(Class<?> aClass, String methodName) {
        for (Method method : aClass.getMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
}
