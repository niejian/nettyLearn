package cn.com.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @desc: cn.com.netty.handler.MyLongToByteEncoder
 * @author: niejian9001@163.com
 * @date: 2020/4/19 16:58
 */
public class MyLongToByteEncoder extends MessageToByteEncoder<Long> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        System.out.println("my encoder invoked");
        System.out.println(msg);
        out.writeLong(msg);
    }
}
