package cn.hjh.ahrpc.core.consummr;

import cn.hjh.ahrpc.core.api.LoadBalancer;
import cn.hjh.ahrpc.core.api.RegistryCenter;
import cn.hjh.ahrpc.core.api.Router;
import cn.hjh.ahrpc.core.cluster.RandomLoadBalancer;
import cn.hjh.ahrpc.core.cluster.RandomRibonLoadBalancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * @ClassName : ConsumerConfig
 * @Description : 消费者配置
 * @Author : hujh
 * @Date: 2024-03-15 00:12
 */
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
            System.out.println("===== consumerBootstrap starting ======");
            consumerBootstrap.start();
            System.out.println("===== consumerBootstrap started ======");
        };
    }

    @Bean
    public LoadBalancer loadBalancer() {
//        return LoadBalancer.Default;
//        return new RandomLoadBalancer();
        return new RandomRibonLoadBalancer();
    }

    @Bean
    public Router router() {
        return Router.Default;
    }

    @Bean(initMethod = "start",destroyMethod = "stop")
    public RegistryCenter consumer_rc(){
        return new RegistryCenter.StaticRegistryCenter(List.of(services.split(",")));
    }

}
