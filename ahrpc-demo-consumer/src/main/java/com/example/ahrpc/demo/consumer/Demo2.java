package com.example.ahrpc.demo.consumer;

import cn.hjh.ahrpc.core.annotation.AHConsumer;
import cn.hjh.ahrpc.demo.api.User;
import cn.hjh.ahrpc.demo.api.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName : Demo2
 * @Description : demo
 * @Author : hujh
 * @Date: 2024-03-16 15:58
 */
@Slf4j
@Component
public class Demo2 {
    @AHConsumer
    private UserService userService2;


    public void test() {
        User user = userService2.findById(123);
        log.info(user.toString());
    }
}
