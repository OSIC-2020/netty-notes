package org.xian.rpc.consumer;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.xian.rpc.protocol.RpcProtocol;

public class Consumer {

    private final int port;
    private final String host;
    private final RpcProtocol protocol;

    public Consumer(String host, int port, RpcProtocol protocol) {
        this.port = port;
        this.host = host;
        this.protocol = protocol;
    }

    public Object start() throws InterruptedException {
        // TODO  Netty 连接复用，将这些业务抽取出来
        EventLoopGroup group = new NioEventLoopGroup();
        ConsumerHandler consumerHandler = new ConsumerHandler();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    // 连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    // 是否开启 TCP 底层心跳机制
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    // TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
                    .option(ChannelOption.TCP_NODELAY, true)
                    // Channel 通道的绑定 ChannelPipeline
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 对象参数类型解码器
                            pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
                                    ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                            // 对象参数类型编码器
                            pipeline.addLast(new ObjectEncoder());

                            pipeline.addLast(consumerHandler);
                        }
                    });
            // 链接到服务端和使用 ChannelFuture 接收返回的数据
            ChannelFuture future = bootstrap.connect(host, port).sync();
            // 发送请求
            future.channel().writeAndFlush(protocol).sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }

        return consumerHandler.getResult();
    }
}
