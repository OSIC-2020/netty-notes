package org.xian.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 接受和响应时间通知，实现具体的业务逻辑，ChannelHandler，ChannelHandler 是绑定在 ChannelPipeline 中，
 * ChannelPipeline 可以绑定一个或者多个 ChannelHandler ，定义为经过 Channel 的入站和出站的一系列逻辑处理
 * Chanel 建立的时候会绑定一个 ChannelPipeline 中，
 * ChannelHandlerContext 是管理和关联 ChannelPipeline 中 ChannelHandler 之间的交互
 * ** @Sharable 表示可以被多个 Channel 共享
 * @author xian
 */
@Sharable
public class PongServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当 Channel 中的数据读取成功后调用
     *
     * @param ctx ChannelHandlerContext
     * @param msg 从 Channel 读取到的数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 从 ByteBuf 读取数据
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server Received: " + in.toString(CharsetUtil.UTF_8));
        // 写入消息到 ChannelHandlerContext 中
        ctx.write(in);
    }

    /**
     * Channel 上一个读取操作完全完成后调用
     *
     * @param ctx ChannelHandlerContext
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        // 将 ctx 中的数据写入并冲刷到远程节点
        // 返回的是一个 ChannelFuture
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 发生异常时候调用
     *
     * @param ctx   ChannelHandlerContext
     * @param cause 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 打印异常和关闭 ChannelHandlerContext
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 新的客户端连接建立的时候会调用这个方法
     *
     * @param ctx ChannelHandlerContext
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        // 从 ChannelHandlerContext 获取 Channel 的信息等
        System.out.println("新建立的链接,客户端地址是 " + ctx.channel().remoteAddress());
    }
}
