package cn.hjh.ahrpc.core.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse {
    /**
     * 结果状态
     * 成功为 true
     */
    public boolean status;

    /**
     * 数据
     */
    public Object data;

    /**
     * 异常
     */
    Exception ex;
}
