package cn.com.netty.third;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author niejian
 * @description
 * @file cn.com.netty.third.MyChatClientChannelHabdler
 * @create 2020-01-14 21:47
 **/
public class MyChatClientChannelHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg);

    }
}
