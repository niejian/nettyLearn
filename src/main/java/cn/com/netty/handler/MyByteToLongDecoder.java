package cn.com.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * bye转龙类型，decode参考ByteToMessageDecoder的javadoc
 * @desc: cn.com.netty.handler.MyByteToLongDecoder
 * @author: niejian9001@163.com
 * @date: 2020/4/18 22:04
 */
public class MyByteToLongDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("myDecoder invoker");
        System.out.println(in.readableBytes());

        // Long类型占用8个字节
        // 参考ByteToMessageDecoder的javadoc
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
