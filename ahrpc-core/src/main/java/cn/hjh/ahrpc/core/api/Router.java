package cn.hjh.ahrpc.core.api;

import java.util.List;

/**
 * @ClassName : Router
 * @Description : 路由器
 * @Author : hujh
 * @Date: 2024-03-18 23:51
 */
public interface Router<T> {

    List<T> route(List<T> providers);

    Router Default = p -> p;
}
