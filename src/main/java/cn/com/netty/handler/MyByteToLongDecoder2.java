package cn.com.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @desc: cn.com.netty.handler.MyByteToLongDecoder2
 * @author: niejian9001@163.com
 * @date: 2020/4/19 21:19
 */
public class MyByteToLongDecoder2 extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecoder2 invoker");
        out.add(in.readLong());
    }
}
