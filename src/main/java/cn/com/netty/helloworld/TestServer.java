package cn.com.netty.helloworld;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author niejian
 * @description
 * @file cn.com.netty.firstclass.TestServer
 * @create 2020-01-12 16:21
 **/
public class TestServer {

    public static void main(String[] args) throws InterruptedException {
        // 获取链接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 拿到获取到链接进行相关的处理
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 启动服务端
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 定义自定义的initialialer套路都是一样的
                    .childHandler(new TestServerInitializer());
            ChannelFuture channelFuture = serverBootstrap.bind(8081).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
