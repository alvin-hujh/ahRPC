package cn.hjh.ahrpc.core.registry;

/**
 * @ClassName : ChangeListener
 * @Description :
 * @Author : hujh
 * @Date: 2024-03-21 00:34
 */
public interface ChangeListener {
    void reFresh(Event event);
}
