package cn.hjh.ahrpc.core.meta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName : ServiceMeta
 * @Description : 服务元数据
 * @Author : hujh
 * @Date: 2024-04-16 01:57
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceMeta {

    private String application;

    private String namespace;

    private String env;

    private String name;

    public String toPath() {
        return String.format("%s_%s_%s_%s", application, namespace, env, name);
    }
}
