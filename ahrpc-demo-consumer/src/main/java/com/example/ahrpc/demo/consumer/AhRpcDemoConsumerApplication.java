package com.example.ahrpc.demo.consumer;

import cn.hjh.ahrpc.core.annotation.AHConsumer;
import cn.hjh.ahrpc.core.consummr.ConsumerConfig;
import cn.hjh.ahrpc.demo.api.Order;
import cn.hjh.ahrpc.demo.api.OrderService;
import cn.hjh.ahrpc.demo.api.User;
import cn.hjh.ahrpc.demo.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ConsumerConfig.class})
public class AhRpcDemoConsumerApplication {

    @AHConsumer
    UserService userService;
    @AHConsumer
    OrderService orderService;
    @Autowired
    Demo2 demo2;

    public static void main(String[] args) {
        SpringApplication.run(AhRpcDemoConsumerApplication.class, args);
    }

    @Bean
    public ApplicationRunner consumer_runner() {
        return x -> {
//            User user = userService.findById(1);
//            System.out.println(user);
            int id = userService.getId(90);
            System.out.println("id = " + id);
//            Order order = orderService.findById(2);
//            System.out.println(order);
//            orderService.toString();
//            demo2.test();

//            Order order404 = orderService.findById(404);
//            System.out.println(order404);
        };
    }

}
