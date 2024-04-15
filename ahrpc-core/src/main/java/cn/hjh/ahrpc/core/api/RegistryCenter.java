package cn.hjh.ahrpc.core.api;

import cn.hjh.ahrpc.core.meta.InstanceMeta;
import cn.hjh.ahrpc.core.registry.ChangeListener;

import java.util.List;

/**
 * @ClassName : RegistryCenter
 * @Description : 注册中心
 * @Author : hujh
 * @Date: 2024-03-19 01:59
 */
public interface RegistryCenter {
    /**
     * 启动
     */
    void start();

    /**
     * 销毁
     */
    void stop();

//  provider 侧

    /**
     * 注册服务
     *
     * @param service
     */
    void register(String service, InstanceMeta instance);

    /**
     * 取消注册
     *
     * @param service
     * @param instance
     */
    void unRegister(String service, InstanceMeta instance);


//    consumer 侧

    /**
     * 获取所有服务
     *
     * @return
     */
    List<InstanceMeta> fetchAll(String service);

    /**
     * 订阅变更
     */
    void subscribe(String service, ChangeListener listener);


    class StaticRegistryCenter implements RegistryCenter {

        List<InstanceMeta> providers;

        public StaticRegistryCenter(List<InstanceMeta> providers) {
            this.providers = providers;

        }

        @Override
        public void start() {

        }

        @Override
        public void stop() {

        }

        @Override
        public void register(String service, InstanceMeta instance) {

        }

        @Override
        public void unRegister(String service, InstanceMeta instance) {

        }

        @Override
        public List<InstanceMeta> fetchAll(String service) {
            return providers;
        }

        @Override
        public void subscribe(String service, ChangeListener listener) {

        }
    }
}
