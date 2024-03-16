package cn.hjh.ahrpc.core.annotation;

import java.lang.annotation.*;

/**
 * @ClassName : AHProvider
 * @Description :RPC 服务提供方
 * @Author : hujh
 * @Date: 2024-03-10 22:27
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface AHProvider {
}
