package org.xian.rpc.provider;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 启动 PRC 服务
 *
 * @author xian
 */
@Slf4j
public class Provider {
    private final int port;
    private final String host;

    private final ProviderRegister register = new ProviderRegister();

    public Provider(String host, int port) {
        this.port = port;
        this.host = host;
    }

    /**
     * 启动 Netty 服务，跟前面的 demo 差不多，不同点在于编码器和解码器
     */
    public void start() {
        log.info("开始启动 RPC 服务，地址是 {} 端口号是：{}", host, port);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    // TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。
                    // TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 是否开启 TCP 底层心跳机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //表示系统用于临时存放已完成三次握手的请求的队列的最大长度,如果连接建立频繁，服务器处理创建新连接较慢，
                    // 可以适当调大这个参数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // Channel 通道的绑定 ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 使用 JDK 自带的序列化机制 TODO 使用 protobuf 或者 kryo 进行序列化
                            // 对象的解码器
                            pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
                                    ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                            // 对象的编码器
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new ProviderHandler());
                        }
                    });
            // 使用 bind 监听 host 和 port
            ChannelFuture future = bootstrap.bind(host, port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("开启服务错误:", e);
        } finally {
            log.info("关闭 bossGroup 和 workerGroup");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * PRC  Provider 服务端注册服务
     *
     * @param service 服务
     * @param clazz   服务接口定义的类
     * @param <T>     服务具体的实现类
     */
    public <T> void addService(T service, Class<T> clazz) {
        register.addService(service, clazz);
    }
}
