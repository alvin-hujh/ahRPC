package cn.hjh.ahrpc.demo.provider;

import cn.hjh.ahrpc.core.annotation.AHProvider;
import cn.hjh.ahrpc.demo.api.Order;
import cn.hjh.ahrpc.demo.api.OrderService;
import org.springframework.stereotype.Component;

@Component
@AHProvider
public class OrderServiceImpl implements OrderService {
    @Override
    public Order findById(Integer orderId) {
        if (orderId==404){
            throw new RuntimeException("404-Exception");
        }
        return new Order(orderId.longValue(), 12.3f);
    }
}
