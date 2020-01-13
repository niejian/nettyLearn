package cn.com.netty.second;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author niejian
 * @description
 * @file cn.com.netty.second.MyClientHandler
 * @create 2020-01-13 21:17
 **/
public class MyClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
        System.out.println("连接服务端： " + channelHandlerContext.channel().remoteAddress());
        System.out.println("get msg from server: " + s);
        channelHandlerContext.writeAndFlush("client send msg: " + dateTimeFormatter.format(LocalDateTime.now()));

    }

    // 回调

    /**
     * 与服务端建立连接后，回调
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("client send msg to server");
    }
}
