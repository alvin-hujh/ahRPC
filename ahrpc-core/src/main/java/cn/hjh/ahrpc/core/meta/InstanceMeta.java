package cn.hjh.ahrpc.core.meta;

import lombok.Data;

import java.util.Map;

/**
 * @ClassName : InstanceMeta
 * @Description : service meta
 * @Author : hujh
 * @Date: 2024-04-05 21:12
 */
@Data
public class InstanceMeta {
    private String schema;
    private String host;

    public InstanceMeta(String schema, String host, Integer port, String context) {
        this.schema = schema;
        this.host = host;
        this.port = port;
        this.context = context;
    }

    private Integer port;
    private String context;

    /**
     * true-available
     * false-disable
     */
    private boolean status;

    private Map<String, String> parameters;

    public String toPath() {
        return String.format("%s_%d", host, port);
    }

    public static InstanceMeta http(String host, Integer port) {
        return new InstanceMeta("http", host, port, "");
    }

    public String toUrl() {
        return String.format("%s://%s:%d/%s", schema, host, port, context);
    }
}
