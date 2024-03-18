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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@SpringBootApplication
@Import({ConsumerConfig.class})
@RestController
public class AhRpcDemoConsumerApplication {

    @AHConsumer
    UserService userService;
    @AHConsumer
    OrderService orderService;
    @Autowired
    Demo2 demo2;

    @RequestMapping("/")
    public User consumerFindById(int id ){
        return userService.findById(id);
    }
    public static void main(String[] args) {
        SpringApplication.run(AhRpcDemoConsumerApplication.class, args);
    }

    @Bean
    public ApplicationRunner consumer_runner() {
        return x -> {

            int[] ids = userService.getIds();
            System.out.println(Arrays.toString(ids));
            System.out.println(Arrays.toString(userService.getLongIds()));
            System.out.println(Arrays.toString(userService.getIds(new int[]{4, 5, 6})));
            System.out.println("---> userName = " + userService.getUserName(new User(1, "张三")));
            Long longId = userService.getLongId(999l);
            System.out.println("---> longId = "+longId);
            User user = userService.findById(1);
            System.out.println(user);
            int id = userService.getId(90);
            System.out.println("id = " + id);
            User user1 = userService.findById(3, "hjh");
            System.out.println(user1);
            System.out.println("---> name not args = " + userService.getName());
            System.out.println("---> name one arg = " + userService.getName(234));
            Order order = orderService.findById(2);
            System.out.println(order);
            orderService.toString();
            demo2.test();

//            Order order404 = orderService.findById(404);
//            System.out.println(order404);
        };
    }

}
