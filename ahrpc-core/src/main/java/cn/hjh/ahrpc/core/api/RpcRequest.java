package cn.hjh.ahrpc.core.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest {

    /**
     * 接口
     */
    private String service;

    /**
     * 方法
     */
    private String method;

    /**
     * 参数
     */
    private  Object[] args;
}
