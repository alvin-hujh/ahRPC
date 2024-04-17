package cn.hjh.ahrpc.core.provider;

import cn.hjh.ahrpc.core.api.RegistryCenter;
import cn.hjh.ahrpc.core.registry.zk.ZKRegistryCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @ClassName : ProviderConfig
 * @Description : 服务提供者配置类
 * @Author : hujh
 * @Date: 2024-03-10 23:13
 */
@Slf4j
@Configuration
public class ProviderConfig {

    @Bean
    ProviderBootstrap providerBootstrap() {
        return new ProviderBootstrap();
    }

    @Bean
    ProvierInvoker provierInvoker(@Autowired ProviderBootstrap providerBootstrap){
        //providerBootstrap 已经在 spring 容器里面了，可以直接取到
        return new ProvierInvoker(providerBootstrap);
    }

    @Bean //(initMethod = "start",destroyMethod = "stop")
    public RegistryCenter provider_rc(){
        return new ZKRegistryCenter();
    }

    @Bean
    @Order(Integer.MIN_VALUE)
    public ApplicationRunner providerBootstrap_runner(@Autowired ProviderBootstrap providerBootstrap) {
        return x -> {
            log.info("===== consumerBootstrap starting ======");
            providerBootstrap.start();
            log.info("===== consumerBootstrap started ======");
        };
    }


}
