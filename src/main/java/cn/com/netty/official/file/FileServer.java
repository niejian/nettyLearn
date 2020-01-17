package cn.com.netty.official.file;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 文件传输
 * @desc: cn.com.netty.official.file.FileServer
 * @author: niejian9001@163.com
 * @date: 2020/1/16 16:46
 */
public class FileServer {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // backlog是用于处理服务端的socketchannel对象的。
                    // 当服务端请求线程全满的时候，用于临时存放已经完成三次握手的请求队列的最大长度
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new FileServerInitializer());

            Channel channel = serverBootstrap.bind(8899).sync().channel();
            channel.closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }
    }
}
