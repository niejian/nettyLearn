package cn.com.netty.third;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.SocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 消息广播状态
 * @author niejian
 * @description
 * @file cn.com.netty.third.MyServerChannelHandler
 * @create 2020-01-14 20:47
 **/
public class MyServerChannelHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 保存channel对象
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final String DATE_PARTTEN = "yyyy-MM-dd HH:mm:ss:SSS";
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            // 当前遍历的channel不是发送msg的channel对象。则向其他客户端广播
            if (channel != ch) {
                ch.writeAndFlush(channel.remoteAddress() + ", 发送的消息" + msg + "\n");
            } else {
                ch.writeAndFlush("[自己] " + msg + " \n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 上线了！");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 离开了！");

    }

    /**
     * 客户端链接建立的时候调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //super.handlerAdded(ctx);
        // 服务端与客户端建立
        Channel channel = ctx.channel();
        // 向其他链接的客户端发送广播信息
        SocketAddress socketAddress = channel.remoteAddress();
        String date = DateTimeFormatter.ofPattern(DATE_PARTTEN).format(LocalDateTime.now());
        // 向channelGroup中的每一个channel对象发送一个消息
        channelGroup.writeAndFlush(date + " [服务器] - " + socketAddress + " 加入 \n");
        // 保存该客户端链接
        channelGroup.add(channel);
    }

    /**
     * 链接断开
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String date = DateTimeFormatter.ofPattern(DATE_PARTTEN).format(LocalDateTime.now());

        channelGroup.writeAndFlush(date + " [服务器] - " + channel.remoteAddress() + " 离开 \n");
    }

    /**
     * 客户端注册进来
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }
}
