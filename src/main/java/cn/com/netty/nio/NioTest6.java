package cn.com.netty.nio;

import java.nio.ByteBuffer;

/**
 * byteBuffer slice
 * Slice Buffer 截取的数组信息，修改的是同一份数据
 * @desc: cn.com.netty.nio.NitTest6
 * @author: niejian9001@163.com
 * @date: 2020/2/7 08:31
 */
public class NioTest6 {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        // 截取buffer （截取数组）
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }

        // 手动设置postion、limit
        buffer.position(2);
        buffer.limit(6);

        // 根据上面的postion、limit截取到对应的子buffer数组，在做 *2的操作
        ByteBuffer sliceBuffer = buffer.slice();
        for (int i = 0; i < sliceBuffer.capacity(); i++) {
            byte b = sliceBuffer.get(i);
            b *= 2;
            sliceBuffer.put(i, b);
        }

        // 重置 position、limit
        buffer.clear();

        // 输出，原来的buffer上从第3到第5上数字乘2
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }

        System.out.println("========slice buffer====");

        while (sliceBuffer.hasRemaining()) {
            System.out.println(sliceBuffer.get());
        }



    }
}
