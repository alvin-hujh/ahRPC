package cn.hjh.ahrpc.core.registry.zk;

import cn.hjh.ahrpc.core.api.RegistryCenter;
import cn.hjh.ahrpc.core.meta.InstanceMeta;
import cn.hjh.ahrpc.core.meta.ServiceMeta;
import cn.hjh.ahrpc.core.registry.ChangeListener;
import cn.hjh.ahrpc.core.registry.Event;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName : ZKRegistryCenter
 * @Description : ZK 注册中心
 * @Author : hujh
 * @Date: 2024-03-19 02:21
 */
@Slf4j
public class ZKRegistryCenter implements RegistryCenter {
    private CuratorFramework client = null;
    private TreeCache cache = null;

    @Value("${ahrpc.zkServer}")
    private String zkServer;
    @Value("${ahrpc.zkRoot}")
    private String zkRoot;

    @Override
    public void start() {
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder()
                .connectString(zkServer)
                .namespace(zkRoot)
                .retryPolicy(policy)
                .build();
        client.start();
        log.info("=====> ZK client started to server[+" + zkServer + "/" + zkRoot + "] " + " success!");
    }

    @Override
    public void stop() {
        if (client != null) {
            cache.close();
            client.close();
            log.info("=====> ZK client stop success!");
        }

    }

    @Override
    public void register(ServiceMeta service, InstanceMeta instance) {
        String servicePath = "/" + service.toPath();
        try {
            // 创建服务的持久化节点
            if (client != null && client.checkExists().forPath(servicePath) == null) {
                client.create().withMode(CreateMode.PERSISTENT).forPath(servicePath, "service".getBytes());
            }
            // 创建示例的临时性节点
            String instancePath = servicePath + "/" + instance.toPath();
            log.info("=====> start to register to ZK client server=" + servicePath);
            client.create().withMode(CreateMode.EPHEMERAL).forPath(instancePath, "provider".getBytes());
            log.info("=====> register to ZK client success!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void unRegister(ServiceMeta service, InstanceMeta instance) {
        String servicePath = "/" + service.toPath();
        try {
            // 判断服务是否存在
            if (client == null || client.checkExists() == null) {
                return;
            }
            // 删除实例节点
            String instancePath = servicePath + "/" + instance.toPath();
            log.info("=====> start to unRegister to ZK client");
            client.delete().quietly().forPath(instancePath);
            log.info("=====> unRegister to ZK client success!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<InstanceMeta> fetchAll(ServiceMeta service) {
        String servicePath = "/" + service.toPath();
        List<String> nodes = null;
        List<InstanceMeta> instanceMetas = null;
        try {
            // 判断服务是否存在
            log.info("=====> start to fetchAll client from ZK client");
            nodes = client.getChildren().forPath(servicePath);
//            nodes = nodes.stream()
//                    .map(x -> "http://" + x.replace("_", ":"))
//                    .collect(Collectors.toList());
//            log.info("=====> fetchAll client from ZK client success = " + nodes);
            instanceMetas = nodes.stream().map(x -> {
                String[] strs = x.split("_");
                return InstanceMeta.http(strs[0], Integer.valueOf(strs[1]));
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return instanceMetas;
    }

    @SneakyThrows
    @Override
    public void subscribe(ServiceMeta service, ChangeListener listener) {
        cache = TreeCache.newBuilder(client, "/" + service.toPath())
                .setCacheData(true).setMaxDepth(2).build();
        cache.getListenable().addListener(((curatorFramework, treeCacheEvent) -> {
                    List<InstanceMeta> nodes = fetchAll(service);
                    listener.reFresh(new Event(nodes));
                })

        );
        cache.start();
    }
}
