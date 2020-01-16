package cn.com.netty.official.telnet;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;

/**
 * @desc: cn.com.netty.telnet.TelnetChannelHandler
 * @author: niejian9001@163.com
 * @date: 2020/1/16 11:26
 */
public class TelnetClientChannelHandler extends SimpleChannelInboundHandler<String> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

        System.out.println(LocalDateTime.now() + " - 客户端发送消息：" + s);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();

        ctx.close();
    }
}
