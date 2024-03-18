package cn.hjh.ahrpc.core.cluster;

import cn.hjh.ahrpc.core.api.LoadBalancer;

import java.util.List;
import java.util.Random;

/**
 * @ClassName : RandomLoadBalancer
 * @Description : 随机轮询
 * @Author : hujh
 * @Date: 2024-03-19 01:21
 */
public class RandomLoadBalancer<T> implements LoadBalancer<T> {
    Random random = new Random();

    @Override
    public T choose(List<T> providers) {
        if (providers == null || providers.size() == 0) return null;
        if (providers.size() == 1) return providers.get(0);
        return providers.get(random.nextInt(providers.size()));
    }
}
