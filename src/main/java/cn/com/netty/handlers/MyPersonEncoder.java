package cn.com.netty.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器
 * @desc: cn.com.netty.handlers.MyPersonEncoder
 * @author: niejian9001@163.com
 * @date: 2020/4/21 17:39
 */
public class MyPersonEncoder extends MessageToByteEncoder<PersonProtocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, PersonProtocol msg, ByteBuf out) throws Exception {
        System.out.println("MyPersonEncoder encode invoked");
        out.writeInt(msg.getLength());
        out.writeBytes(msg.getContent());
    }
}
