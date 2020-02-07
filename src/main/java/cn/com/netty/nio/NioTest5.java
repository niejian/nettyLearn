package cn.com.netty.nio;

import java.nio.ByteBuffer;

/**
 * @desc: cn.com.netty.nio.NioTest5
 * @author: niejian9001@163.com
 * @date: 2020/2/7 08:25
 */
public class NioTest5 {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);

        buffer.putInt(15);
        buffer.putLong(5000L);
        buffer.putChar('你');
        buffer.putShort((short) 2);
        buffer.putChar('好');

        buffer.flip();
        // 按什么顺序放进去的，取出来的时候也要按照这种顺序取出来
        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getShort());
    }


}
