package cn.com.netty.second;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

/**
 * 客户端与服务端通过string对象进行传输
 * @author niejian
 * @description
 * @file cn.com.netty.second.MyServerHandler
 * @create 2020-01-13 21:04
 **/
public class MyServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     *
     * @param channelHandlerContext
     * @param s 客户端发送过来的数据
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println("客户端：" + channelHandlerContext.channel().remoteAddress() + " : " + s);
        channelHandlerContext.channel().writeAndFlush("get msg form server: " + UUID.randomUUID().toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();

    }
}
