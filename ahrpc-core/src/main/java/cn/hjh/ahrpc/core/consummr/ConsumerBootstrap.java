package cn.hjh.ahrpc.core.consummr;

import cn.hjh.ahrpc.core.annotation.AHConsumer;
import cn.hjh.ahrpc.core.api.LoadBalancer;
import cn.hjh.ahrpc.core.api.RegistryCenter;
import cn.hjh.ahrpc.core.api.Router;
import cn.hjh.ahrpc.core.api.RpcContext;
import cn.hjh.ahrpc.core.meta.InstanceMeta;
import cn.hjh.ahrpc.core.registry.ChangeListener;
import cn.hjh.ahrpc.core.registry.Event;
import cn.hjh.ahrpc.core.util.MethodUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName : ConsumerBootstrap
 * @Description : 消费者的服务初始化
 * @Author : hujh
 * @Date: 2024-03-15 00:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumerBootstrap implements ApplicationContextAware, EnvironmentAware {
    /**
     * 获取所有的装配 bean
     */
    ApplicationContext applicationContext;
    /**
     * 获取所有的环境变量
     */
    Environment environment;

    private Map<String, Object> stub = new HashMap<>();

    public void start() {

        Router router = applicationContext.getBean(Router.class);
        LoadBalancer loadBalancer = applicationContext.getBean(LoadBalancer.class);
        RegistryCenter rc = applicationContext.getBean(RegistryCenter.class);

        RpcContext rpcContext = new RpcContext();
        rpcContext.setRouter(router);
        rpcContext.setLoadBalancer(loadBalancer);
        String[] names = applicationContext.getBeanDefinitionNames();
        for (String name : names) {
            Object bean = applicationContext.getBean(name);
            List<Field> fields = MethodUtils.findAnnotatedField(bean.getClass(), AHConsumer.class);
            fields.stream().forEach(f -> {
                System.out.println("====>" + f.getName());
                try {
                    Class<?> service = f.getType();
                    String serviceName = service.getCanonicalName();
                    Object consumer = stub.get(serviceName);
                    if (consumer == null) {
                        consumer = createFromRegister(service, rpcContext, rc);
                        stub.put(serviceName,consumer);
                    }
                    f.setAccessible(true);
                    f.set(bean, consumer);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    private Object createFromRegister(Class<?> service, RpcContext rpcContext, RegistryCenter rc) {
        String serviceName = service.getCanonicalName();
        List<InstanceMeta> providers = rc.fetchAll(serviceName);
        rc.subscribe(serviceName, new ChangeListener() {
            @Override
            public void reFresh(Event event) {
                providers.clear();
                providers.addAll(event.getNodes());
            }
        });
        return createConsumer(service, rpcContext, providers);
    }

    private Object createConsumer(Class<?> service, RpcContext rpcContext, List<InstanceMeta> providers) {

        return Proxy.newProxyInstance(service.getClassLoader(),
                new Class[]{service}, new AHInvocationHandler(service, rpcContext, providers));
    }


}
