package cn.com.netty.handler;

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
public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long s) throws Exception {
        System.out.println("get msg from server: " + s);

        ctx.writeAndFlush(65535L);
    }

    // 回调

    /**
     * 与服务端建立连接后，回调
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //ctx.writeAndFlush("client send msg to server");
        // 客户端与服务端简历连接后，此方法被调用，向服务端发送一个long数据
        ctx.writeAndFlush(65535L);
    }
}
