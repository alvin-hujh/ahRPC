package cn.hjh.ahrpc.core.provider;

import cn.hjh.ahrpc.core.api.RegistryCenter;
import cn.hjh.ahrpc.core.consummr.ConsumerBootstrap;
import cn.hjh.ahrpc.core.registry.ZKRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * @ClassName : ProviderConfig
 * @Description : 服务提供者配置类
 * @Author : hujh
 * @Date: 2024-03-10 23:13
 */
@Configuration
public class ProviderConfig {

    @Bean
    ProviderBootstrap providerBootstrap() {
        return new ProviderBootstrap();
    }

    @Bean(initMethod = "start",destroyMethod = "stop")
    public RegistryCenter provider_rc(){
        return new ZKRegistryCenter();
    }

    @Bean
    @Order(Integer.MIN_VALUE)
    public ApplicationRunner providerBootstrap_runner(@Autowired ProviderBootstrap providerBootstrap) {
        return x -> {
            System.out.println("===== consumerBootstrap starting ======");
            providerBootstrap.start();
            System.out.println("===== consumerBootstrap started ======");
        };
    }


}
