package org.xian.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;


/**
 * @author xian
 */
public class PongServer {

    private final int port;

    public PongServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        new PongServer(port).start();

    }

    public void start() throws InterruptedException {
        // Handler 处理的类
        final PongServerHandler serverHandler = new PongServerHandler();
        // Netty 会为每个 Channel 分配一个 EventLoop
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 引导，功能是将 ChannelHandler，ChannelPipeline、EventLoop 组织起来，
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    // 使用 NIO 的 Channel
                    .channel(NioServerSocketChannel.class)
                    // 绑定的端口号
                    .localAddress(new InetSocketAddress(this.port))
                    // 绑定 Handler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // 将 serverHandler 绑定到 ChannelPipeline
                            ch.pipeline().addLast(serverHandler);
                        }
                    });
            // 每个 Netty 出入站 I/O 操作返回 ChannelFuture
            // 核心的bind()方法，用于监听端口和新建一个 ServerSocketChannel
            ChannelFuture future = bootstrap.bind();

            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
