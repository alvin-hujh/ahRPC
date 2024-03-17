package cn.hjh.ahrpc.core.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

/**
 * @ClassName : ProviderMeta
 * @Description : 服务提供方元数据
 * @Author : hujh
 * @Date: 2024-03-16 18:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderMeta {

    /**
     * 方法
     */
    Method method;
    /**
     * 方法签名
     */
    String methodSign;

    /**
     * 服务提供对象
     */
    Object serviceImpl;
}
