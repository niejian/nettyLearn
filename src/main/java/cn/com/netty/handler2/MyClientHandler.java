package cn.com.netty.handler2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author niejian
 * @description
 * @file cn.com.netty.second.MyClientHandler
 * @create 2020-01-13 21:17
 **/
public class MyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf s) throws Exception {
        byte[] buffer = new byte[s.readableBytes()];
        // 将bytebuf中的数据存入到buffer
        s.readBytes(buffer);
        String msg = new String(buffer, CharsetUtil.UTF_8);
        System.out.println("客户端接收到消息：" + msg);
        System.out.println("客户端接收到的消息数量：" + ++count);
//        channelHandlerContext.writeAndFlush(resp);

    }

    // 回调

    /**
     * 与服务端建立连接后，回调
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            ByteBuf buffer = Unpooled.copiedBuffer(" send from client" + i, CharsetUtil.UTF_8);
            ctx.writeAndFlush(buffer);

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
