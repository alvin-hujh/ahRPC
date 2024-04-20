package cn.hjh.ahrpc.core.provider;

import cn.hjh.ahrpc.core.annotation.AHProvider;
import cn.hjh.ahrpc.core.api.RegistryCenter;
import cn.hjh.ahrpc.core.meta.InstanceMeta;
import cn.hjh.ahrpc.core.meta.ProviderMeta;
import cn.hjh.ahrpc.core.meta.ServiceMeta;
import cn.hjh.ahrpc.core.util.MethodUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Data
public class ProviderBootstrap implements ApplicationContextAware {

    ApplicationContext applicationContext;
    RegistryCenter rc;
    private MultiValueMap<String, ProviderMeta> skeleton = new LinkedMultiValueMap<>();

    private InstanceMeta instance;
    @Value("${server.port}")
    private String port;

    @Value("${app.id}")
    private String application;
    @Value("${app.namespace}")
    private String namespace;
    @Value("${app.env}")
    private String env;

    @SneakyThrows
    @PostConstruct
    public void init() {
        Map<String, Object> providers = applicationContext.getBeansWithAnnotation(AHProvider.class);
        rc = applicationContext.getBean(RegistryCenter.class);
        providers.forEach((x, y) -> log.info(x));
        providers.values().forEach(
                x -> getInstance(x)
        );
    }

    public void start() throws UnknownHostException {
        rc.start();
        String ip = InetAddress.getLocalHost().getHostAddress();
        instance = InstanceMeta.http(ip, Integer.valueOf(port));
        log.info("instance=${}", instance);
        skeleton.keySet().forEach(this::registerService);
    }

    @PreDestroy
    public void stop() {
        log.info("===> unregister all service!");
        skeleton.keySet().forEach(this::unRegisterService);
        rc.stop();
    }

    private void unRegisterService(String service) {
        ServiceMeta serviceMeta = ServiceMeta.builder()
                .application(application)
                .env(env)
                .namespace(namespace)
                .name(service).build();
        rc.unRegister(serviceMeta, instance);
    }

    private void registerService(String service) {
        ServiceMeta serviceMeta = ServiceMeta.builder()
                .application(application)
                .env(env)
                .namespace(namespace)
                .name(service).build();
        rc.register(serviceMeta, instance);

    }

    private void getInstance(Object impl) {
        Class<?> service = impl.getClass().getInterfaces()[0];
        for (Method method : impl.getClass().getMethods()) {
            if (MethodUtils.checkLocalMethod(method.getName())) {
                continue;
            }
            createProvider(service, impl, method);
        }
//        skeleton.put(itfer.getCanonicalName(), x);
    }

    private void createProvider(Class<?> service, Object impl, Method method) {
        ProviderMeta providerMeta = ProviderMeta.builder()
                .serviceImpl(impl)
                .method(method)
                .methodSign(MethodUtils.methodSign(method))
                .build();
        log.info("create a new provider  -> " + providerMeta);
        skeleton.add(service.getCanonicalName(), providerMeta);
    }
}
