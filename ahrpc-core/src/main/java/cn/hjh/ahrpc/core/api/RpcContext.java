package cn.hjh.ahrpc.core.api;

import lombok.Data;

import java.util.List;

/**
 * @ClassName : RpcContext
 * @Description : rpc上下文
 * @Author : hujh
 * @Date: 2024-03-19 01:46
 */
@Data
public class RpcContext {

    List<Filter> filters;//todo

    Router router;

    LoadBalancer loadBalancer;
}
