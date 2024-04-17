package cn.hjh.ahrpc.core.consummr;

import cn.hjh.ahrpc.core.api.LoadBalancer;
import cn.hjh.ahrpc.core.api.RegistryCenter;
import cn.hjh.ahrpc.core.api.Router;
import cn.hjh.ahrpc.core.cluster.RandomRibonLoadBalancer;
import cn.hjh.ahrpc.core.meta.InstanceMeta;
import cn.hjh.ahrpc.core.registry.zk.ZKRegistryCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @ClassName : ConsumerConfig
 * @Description : 消费者配置
 * @Author : hujh
 * @Date: 2024-03-15 00:12
 */
@Slf4j
@Configuration
public class ConsumerConfig {
    @Value("${ahrpc.providers}")
    String services;
    @Bean
    ConsumerBootstrap createConsumerBootstrap() {
        return new ConsumerBootstrap();
    }

    @Bean
    @Order(Integer.MIN_VALUE)
    public ApplicationRunner consumerBootstrap_runner(@Autowired ConsumerBootstrap consumerBootstrap) {
        return x -> {
            log.info("===== consumerBootstrap starting ======");
            consumerBootstrap.start();
            log.info("===== consumerBootstrap started ======");
        };
    }

    @Bean
    public LoadBalancer<InstanceMeta> loadBalancer() {
//        return LoadBalancer.Default;
//        return new RandomLoadBalancer();
        return new RandomRibonLoadBalancer();
    }

    @Bean
    public Router<InstanceMeta> router() {
        return Router.Default;
    }

    @Bean(initMethod = "start",destroyMethod = "stop")
    public RegistryCenter consumer_rc(){
        return new ZKRegistryCenter();
    }

}
