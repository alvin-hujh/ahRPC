package cn.hjh.ahrpc.core.annotation;

import java.lang.annotation.*;

/**
 * @ClassName : AHConsumer
 * @Description : 服务消费者
 * @Author : hujh
 * @Date: 2024-03-10 23:19
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface AHConsumer {
}
