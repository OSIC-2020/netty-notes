package org.xian.rpc.provider;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.xian.rpc.protocol.RpcProtocol;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * PRC 服务端的 Handler
 *
 * @author xian
 */
@Slf4j
public class ProviderHandler extends ChannelInboundHandlerAdapter {
    private final ProviderRegister register = new ProviderRegister();
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Object result;
        RpcProtocol rpcProtocol = (RpcProtocol) msg;
        try {
            // 从 provider 查找有没有这个服务
            Object service = register.getService(rpcProtocol.getInterfaceName());
            // 从 service 根据方法名称和传入参数类型获取具体的方法
            Method method = service.getClass().getMethod(rpcProtocol.getMethodName(),
                    rpcProtocol.getParamTypes());
            // 执行这个方法
            result = method.invoke(service, rpcProtocol.getParamValues());
            ctx.writeAndFlush(result);
            log.info("服务名称：{}，调用的方法是 {}", rpcProtocol.getInterfaceName(), rpcProtocol.getMethodName());
        } catch (NoSuchMethodException | IllegalArgumentException |
                InvocationTargetException | IllegalAccessException e) {
            log.error("服务未找到或者服务发生错误");
        } finally {
            ctx.flush();
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
