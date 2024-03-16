package cn.hjh.ahrpc.demo.api;

/**
 * @ClassName : OrderService
 * @Description : 订单服务
 * @Author : hujh
 * @Date: 2024-03-15 01:39
 */
public interface OrderService {
    Order findById(Integer id);
}
