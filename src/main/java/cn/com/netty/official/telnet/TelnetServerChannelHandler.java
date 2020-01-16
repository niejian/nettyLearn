package cn.com.netty.official.telnet;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.StringUtil;

import java.time.LocalDateTime;

/**
 * @desc: cn.com.netty.telnet.TelnetServerChannelHandler
 * @author: niejian9001@163.com
 * @date: 2020/1/16 11:11
 */
public class TelnetServerChannelHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 根据客户端发来的消息返回客户端
     * @param channelHandlerContext
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        String response = "";
        boolean close = false;
        System.out.println(LocalDateTime.now() + " - 接收到客户端消息：" + msg + " \n");
        if (StringUtil.isNullOrEmpty(msg)) {
            response = "请输入内容 \n";
        } else if ("bye".equals(msg)) {
            response = "bye \n";
            close = true;
        } else {
            response = LocalDateTime.now() + " - " + channelHandlerContext.channel().remoteAddress() + " 发来消息：" + msg + " \n";
        }

        channelHandlerContext.writeAndFlush(response);

        if (close) {
            channelHandlerContext.close();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        StringBuffer message = new StringBuffer(LocalDateTime.now() + "【客户端】" + ctx.channel().remoteAddress() + "，连接进来");
        ctx.writeAndFlush(message.toString());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel inacvive");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel register");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel unregister");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
