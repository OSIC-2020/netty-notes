package org.xian.rpc.consumer;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 将返回结果抽取出来
 *
 * @author xian
 */
public class ConsumerHandler extends ChannelInboundHandlerAdapter {
    private Object result;

    public Object getResult() {
        return result;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        result = msg;
    }
}
