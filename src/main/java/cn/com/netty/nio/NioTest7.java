package cn.com.netty.nio;

import java.nio.ByteBuffer;

/**
 * 只读buffer我们可以随时将一个普通buffer调用asOnlyReadBuffer返回一个只读buffer
 * 但是不能讲只读buffer转换为读写buffer
 * @desc: cn.com.netty.nio.NioTest7
 * @author: niejian9001@163.com
 * @date: 2020/2/7 08:43
 */
public class NioTest7 {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        System.out.println(buffer.getClass());
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }

        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();

        System.out.println(readOnlyBuffer.getClass());


        // 在只读buffer上写入数据,抛出异常
        readOnlyBuffer.put((byte) 1);
    }
}
