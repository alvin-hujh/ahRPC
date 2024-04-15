package cn.hjh.ahrpc.core.consummr;

import cn.hjh.ahrpc.core.api.RpcRequest;
import cn.hjh.ahrpc.core.api.RpcResponse;

/**
 * @ClassName : HttpInvoker
 * @Description : http请求
 * @Author : hujh
 * @Date: 2024-04-05 20:43
 */
public interface HttpInvoker {
    RpcResponse<?> post(RpcRequest request, String url);
}
