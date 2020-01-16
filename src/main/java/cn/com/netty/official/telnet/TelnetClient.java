package cn.com.netty.official.telnet;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @desc: cn.com.netty.telnet.TelnetClient
 * @author: niejian9001@163.com
 * @date: 2020/1/16 11:22
 */
public class TelnetClient {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new TelnetClientInitializer());

            Channel channel = bootstrap.connect("localhost", 8899).sync().channel();


            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (; ; ) {
                String line = in.readLine();
                if (null == line) {
                    break;
                }
                channel.writeAndFlush(line + "\n\r");
                if ("bye".equals(line.toLowerCase())) {
                    channel.closeFuture().sync();
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            eventLoopGroup.shutdownGracefully();
        }


    }
}
