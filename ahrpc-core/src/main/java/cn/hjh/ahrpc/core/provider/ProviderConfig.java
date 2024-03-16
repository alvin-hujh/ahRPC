package cn.hjh.ahrpc.core.provider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
