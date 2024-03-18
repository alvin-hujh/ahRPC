package cn.hjh.ahrpc.core.api;

import java.util.List;

/**
 * @ClassName : LoadBalancer
 * @Description : 负载均衡
 * @Author : hujh
 * @Date: 2024-03-18 23:51
 */
public interface LoadBalancer<T> {

    T choose(List<T> providers);

    LoadBalancer Default = p -> p == null || p.size() == 0 ? null : p.get(0);

}
