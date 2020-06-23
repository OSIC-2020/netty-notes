package org.xian.rpc.consumer;

import org.xian.rpc.protocol.RpcProtocol;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author xian
 */
public class ConsumerProxy implements InvocationHandler {

    private final String host;
    private final int port;

    public ConsumerProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 封装成 RPC 的请求
        RpcProtocol protocol = new RpcProtocol();
        protocol.setInterfaceName(method.getDeclaringClass().getName());
        protocol.setMethodName(method.getName());
        protocol.setParamTypes(method.getParameterTypes());
        protocol.setParamValues(args);

        // 启动 PRC Consumer
        Consumer consumer = new Consumer(host, port, protocol);
        return consumer.start();
    }
}
