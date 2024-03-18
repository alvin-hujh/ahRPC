package cn.hjh.ahrpc.core.consummr;

import cn.hjh.ahrpc.core.annotation.AHConsumer;
import cn.hjh.ahrpc.core.api.LoadBalancer;
import cn.hjh.ahrpc.core.api.RegistryCenter;
import cn.hjh.ahrpc.core.api.Router;
import cn.hjh.ahrpc.core.api.RpcContext;
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

//        String urls = environment.getProperty("ahrpc.providers");

//        if (Strings.isBlank(urls)) {
//            System.out.println("=== provider is empty ===");
//        }
//        String[] providers = urls.split(",");

        String[] names = applicationContext.getBeanDefinitionNames();
        for (String name : names) {
            Object bean = applicationContext.getBean(name);
//            if (!name.contains("ahRpcDemoConsumerApplication")) {
//                return;
//            }
            List<Field> fields = findAnnotatedField(bean.getClass());
            fields.stream().forEach(f -> {
                System.out.println("====>" + f.getName());
                try {
                    Class<?> service = f.getType();
                    String serviceName = service.getCanonicalName();
                    Object consumer = stub.get(serviceName);
                    if (consumer == null) {
                        consumer = createFromRegister(service, rpcContext, rc);
//                                createConsumer(service, rpcContext, List.of(providers));
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
       List<String> providers =  rc.fetchAll(serviceName);
       return createConsumer(service,rpcContext,providers);
    }

    private Object createConsumer(Class<?> service, RpcContext rpcContext, List<String> providers) {

        return Proxy.newProxyInstance(service.getClassLoader(),
                new Class[]{service}, new AHInvocationHandler(service, rpcContext, providers));
    }

    private List<Field> findAnnotatedField(Class<?> aClass) {
        List<Field> result = new ArrayList<>();
        while (aClass != null) {
            Field[] fields = aClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(AHConsumer.class)) {
                    result.add(field);
                }
            }
            aClass = aClass.getSuperclass();
        }
        return result;
    }
}
