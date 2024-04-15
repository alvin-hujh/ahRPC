package cn.hjh.ahrpc.core.provider;

import cn.hjh.ahrpc.core.annotation.AHProvider;
import cn.hjh.ahrpc.core.api.RegistryCenter;
import cn.hjh.ahrpc.core.meta.InstanceMeta;
import cn.hjh.ahrpc.core.meta.ProviderMeta;
import cn.hjh.ahrpc.core.util.MethodUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
    RegistryCenter rc;
    private MultiValueMap<String, ProviderMeta> skeleton = new LinkedMultiValueMap<>();

    private InstanceMeta instance;
    @Value("${server.port}")
    private String port;

    @SneakyThrows
    @PostConstruct
    public void init() {
        Map<String, Object> providers = applicationContext.getBeansWithAnnotation(AHProvider.class);
        rc = applicationContext.getBean(RegistryCenter.class);
        providers.forEach((x, y) -> System.out.println(x));
        providers.values().forEach(
                x -> getInstance(x)
        );
    }

    public void start() throws UnknownHostException {
        rc.start();
        String ip = InetAddress.getLocalHost().getHostAddress();
        instance = InstanceMeta.http(ip, Integer.valueOf(port));
        skeleton.keySet().forEach(this::registerService);
    }

    @PreDestroy
    public void stop() {
        System.out.println("===> unregister all service!");
        skeleton.keySet().forEach(this::unRegisterService);
        rc.stop();
    }

    private void unRegisterService(String service) {
        rc.unRegister(service, instance);
    }

    private void registerService(String service) {
        rc.register(service, instance);

    }

    private void getInstance(Object x) {
        Class<?> itfer = x.getClass().getInterfaces()[0];
        for (Method method : x.getClass().getMethods()) {
            if (MethodUtils.checkLocalMethod(method.getName())) {
                continue;
            }
            createProvider(itfer, x, method);
        }
//        skeleton.put(itfer.getCanonicalName(), x);
    }

    private void createProvider(Class<?> itfer, Object x, Method method) {
        ProviderMeta providerMeta = new ProviderMeta();
        providerMeta.setMethod(method);
        providerMeta.setServiceImpl(x);
        providerMeta.setMethodSign(MethodUtils.methodSign(method));
        System.out.println("create a new provider  -> " + providerMeta);
        skeleton.add(itfer.getCanonicalName(), providerMeta);
    }
}
