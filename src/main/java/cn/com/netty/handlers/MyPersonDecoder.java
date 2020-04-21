package cn.com.netty.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * person的解码器
 * @desc: cn.com.netty.handlers.MyPersonDecoder
 * @author: niejian9001@163.com
 * @date: 2020/4/21 17:36
 */
public class MyPersonDecoder extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        System.out.println("MyPersonDecoder decode invoked");
        // 获取消息长度
        int length = in.readInt();
        byte[] content = new byte[length];
        in.readBytes(content);
        PersonProtocol personProtocol = new PersonProtocol();
        personProtocol.setContent(content);
        personProtocol.setLength(length);

        out.add(personProtocol);
    }
}
