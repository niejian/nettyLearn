package cn.com.netty.handler2;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

/**
 * 客户端与服务端通过string对象进行传输
 * @author niejian
 * @description
 * @file cn.com.netty.second.MyServerHandler
 * @create 2020-01-13 21:04
 **/
public class MyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;
    /**
     *
     * @param ctx
     * @param s 客户端发送过来的数据
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf s) throws Exception {
        byte[] buffer = new byte[s.readableBytes()];
        // 将bytebuf中的数据存入到buffer
        s.readBytes(buffer);
        String msg = new String(buffer, CharsetUtil.UTF_8);
        System.out.println("服务端接收到消息：" + msg);
        System.out.println("服务端接收到的消息数量：" + ++count);

        ByteBuf resp = Unpooled.copiedBuffer(UUID.randomUUID().toString(), CharsetUtil.UTF_8);
        ctx.writeAndFlush(resp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();

    }
}
