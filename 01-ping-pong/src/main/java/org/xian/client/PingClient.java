package org.xian.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author xiaoxian
 */
public class PingClient {


    public static void main(String[] args) throws InterruptedException {
        new PingClient().start();
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {

            Bootstrap bootstrap = new Bootstrap();
            int port = 8080;
            String host = "127.0.0.1";

            bootstrap.group(group).channel(NioSocketChannel.class)
                    // 远程服务器路径和端口号
                    .remoteAddress(new InetSocketAddress(host, port))
                    // Channel 通道的绑定 ChannelPipeline
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 绑定 PingClientHandler
                            ch.pipeline().addLast(new PingClientHandler());
                        }
                    });
            // 链接到服务端和使用 ChannelFuture 接收返回的数据
            ChannelFuture future = bootstrap.connect().sync();
            future.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
