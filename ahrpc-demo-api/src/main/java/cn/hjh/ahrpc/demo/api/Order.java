package cn.hjh.ahrpc.demo.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName : Order
 * @Description : 订单
 * @Author : hujh
 * @Date: 2024-03-15 01:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private Float amount;
}
