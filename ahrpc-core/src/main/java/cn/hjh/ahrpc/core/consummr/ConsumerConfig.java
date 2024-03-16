package cn.hjh.ahrpc.core.consummr;

import org.springframework.beans.factory.annotation.Autowired;
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
@Configuration
public class ConsumerConfig {
    @Bean
    ConsumerBootstrap createConsumerBootstrap(){
        return new ConsumerBootstrap();
    }

    @Bean
    @Order(Integer.MIN_VALUE)
    public ApplicationRunner consumerBootstrap_runner(@Autowired ConsumerBootstrap consumerBootstrap){
        return x->{
            System.out.println("===== consumerBootstrap starting ======");
            consumerBootstrap.start();
            System.out.println("===== consumerBootstrap started ======");
        };
    }

}
