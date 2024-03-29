package cn.hjh.ahrpc.core.registry;

import cn.hjh.ahrpc.core.api.RegistryCenter;
import lombok.SneakyThrows;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName : ZKRegistryCenter
 * @Description : ZK 注册中心
 * @Author : hujh
 * @Date: 2024-03-19 02:21
 */
public class ZKRegistryCenter implements RegistryCenter {
    private CuratorFramework client = null;

    @Override
    public void start() {
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181")
                .namespace("ahRpc")
                .retryPolicy(policy)
                .build();
        client.start();
        System.out.println("=====> ZK client started success! <=====");
    }

    @Override
    public void stop() {
        if (client != null) {
            client.close();
        }

    }

    @Override
    public void register(String service, String instance) {
        String servicePath = "/" + service;
        try {
            // 创建服务的持久化节点
            if (client != null && client.checkExists().forPath(servicePath) == null) {
                client.create().withMode(CreateMode.PERSISTENT).forPath(servicePath, "service".getBytes());
            }
            // 创建示例的临时性节点
            String instancePath = servicePath + "/" + instance;
            System.out.println("=====> start to register to ZK client <=====");
            client.create().withMode(CreateMode.EPHEMERAL).forPath(instancePath, "provider".getBytes());
            System.out.println("=====> register to ZK client success! <=====");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void unRegister(String service, String instance) {
        String servicePath = "/" + service;
        try {
            // 判断服务是否存在
            if (client == null || client.checkExists() == null) {
                return;
            }
            // 删除实例节点
            String instancePath = servicePath + "/" + instance;
            System.out.println("=====> start to unRegister to ZK client <=====");
            client.delete().quietly().forPath(instancePath);
            System.out.println("=====> unRegister to ZK client success! <=====");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> fetchAll(String service) {
        String servicePath = "/" + service;
        List<String> nodes = null;
        try {
            // 判断服务是否存在
            System.out.println("=====> start to fetchAll client from ZK client <=====");
            nodes = client.getChildren().forPath(servicePath);
            if (nodes == null || nodes.size()==0)return nodes;
            nodes = nodes.stream()
                    .map(x -> "http://" + x.replace("_", ":") )
                    .collect(Collectors.toList());
            System.out.println("=====> fetchAll client from ZK client success = "+nodes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return nodes;
    }

    @SneakyThrows
    @Override
    public void subscribe(String service, ChangeListener listener) {
        final TreeCache cache = TreeCache.newBuilder(client,"/"+service)
                .setCacheData(true).setMaxDepth(2).build();
        cache.getListenable().addListener(((curatorFramework, treeCacheEvent) -> {
            List<String> nodes = fetchAll(service);
            listener.reFresh(new Event(nodes));
        })

        );
        cache.start();
    }
}
