package cn.hjh.ahrpc.core.registry;

import cn.hjh.ahrpc.core.meta.InstanceMeta;
import lombok.Data;

import java.util.List;

/**
 * @ClassName : Event
 * @Description :
 * @Author : hujh
 * @Date: 2024-03-21 00:48
 */
@Data
public class Event {
    List<InstanceMeta> nodes;

    public Event(List<InstanceMeta> nodes) {
        this.nodes=nodes;
    }
}
