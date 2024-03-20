package cn.hjh.ahrpc.core.registry;

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
    List<String> nodes;

    public Event(List<String> nodes) {
        this.nodes=nodes;
    }
}
