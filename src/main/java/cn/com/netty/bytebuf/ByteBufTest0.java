package cn.com.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @desc: cn.com.netty.bytebuf.ByteBufTest0
 * @author: niejian9001@163.com
 * @date: 2020/3/29 20:42
 */
public class ByteBufTest0 {
    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.buffer(10);
        for (int i = 0; i < 10; i++) {
            // 将数据存放到butebuf中
            buffer.writeByte(i);
        }

        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.getByte(i));

        }
    }
}
