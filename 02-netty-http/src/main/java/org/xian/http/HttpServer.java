package org.xian.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;


/**
 * @author xian
 */
public class HttpServer {

    private static final Logger log = LoggerFactory.getLogger(HttpServer.class);

    private final int port;

    /**
     * @param port 端口号
     */
    public HttpServer(int port) {
        this.port = port;
    }

    public void start() {
        log.info("开始启动 Http 服务，端口号是：" + port);
        // Netty 会为每个 Channel 分配一个 EventLoop
        // 一个 EventLoop 可以管理多个 Channel
        EventLoopGroup executors = new NioEventLoopGroup();
        try {
            // 引导，功能是将 ChannelHandler，ChannelPipeline、EventLoop 组织起来，
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(executors)
                    // 使用 NIO 的 Channel
                    .channel(NioServerSocketChannel.class)
                    // 绑定 Handler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // Http 编码器和解码器
                            ch.pipeline().addLast(new HttpServerCodec());// http 编解码
                            ch.pipeline().addLast("httpAggregator",
                                    new HttpObjectAggregator(512 * 1024)); // http 消息聚合器
                            // 将 HttpRequestHandler 绑定到 ChannelPipeline
                            ch.pipeline().addLast(new HttpRequestHandler());
                        }
                    });

            ChannelFuture future = bootstrap.bind(new InetSocketAddress(this.port));

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                executors.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
