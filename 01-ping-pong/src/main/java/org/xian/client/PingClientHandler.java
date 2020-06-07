package org.xian.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;


/**
 * @author xiaoxian
 */
@ChannelHandler.Sharable
public class PingClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 使用 UTF-8 编码，ByteBuf Netty 的数据容器
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello Netty!", CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        String string = in.toString(CharsetUtil.UTF_8);
        System.out.println("Client Received: " + string);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
