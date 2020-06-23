package org.xian.rpc.protocol;


import lombok.*;

import java.io.Serializable;

/**
 * 发起 RPC 请求的格式，需要如下字段
 *
 * @author xian
 */
@Data
public class RpcProtocol implements Serializable {
    private static final long serialVersionUID = 5933724318897197513L;
    /**
     * 接口名,服务端根据接口名调用实际的实现类
     */
    private String interfaceName;
    /**
     * 方法名，接口方法名
     */
    private String methodName;
    /**
     * 参数的值
     */
    private Object[] paramValues;
    /**
     * 参数的类型
     */
    private Class<?>[] paramTypes;
}
