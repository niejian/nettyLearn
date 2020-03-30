package cn.com.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * @user niejian9001@163.com
 * @date 2020/3/30 20:05
 */
public class ByteBufTest1 {

    public static void main(String[] args) {
        // 添加中文字符后，发现中文字符占用三个字符
        ByteBuf byteBuf = Unpooled.copiedBuffer("聂hello world", Charset.forName("utf-8"));

        // 是否堆上缓冲
        if (byteBuf.hasArray()) {
            byte[] content = byteBuf.array();
            System.out.println(new String(content, Charset.forName("utf-8")));
            System.out.println(byteBuf);
            // readIndex:0, writeIndex:11, capacity:33
            //UnpooledByteBufAllocator$InstrumentedUnpooledUnsafeHeapByteBuf(ridx: 0, widx: 11, cap: 33)
            System.out.println(byteBuf.arrayOffset());
            System.out.println(byteBuf.readerIndex());
            System.out.println(byteBuf.writerIndex());
            System.out.println(byteBuf.capacity());

            // writeInde - readIndex
            int readableBytes = byteBuf.readableBytes();
            System.out.println("可读字节长度：" + readableBytes);

            // 读取字节
            for (int i = 0; i < readableBytes; i++) {

//                System.out.println((char) byteBuf.getByte(i) + "----readIndex:" + byteBuf.readerIndex());
                // 调用readByte(), byteBuf的readIndex才会发生移动
                System.out.println((char) byteBuf.readByte() + "----readIndex:" + byteBuf.readerIndex());

            }
            System.out.println("readIndex:" + byteBuf.readerIndex());
//            byteBuf.re

            // 打印字符序列(指定位置下的字符段)
            CharSequence charSequence = byteBuf.getCharSequence(0, 4, Charset.forName("utf-8"));
            System.out.println(charSequence);
        }
    }
}
