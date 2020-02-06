package cn.com.netty.nio;

import java.nio.IntBuffer;
import java.security.SecureRandom;

/**
 * nio 测试
 * @desc: cn.com.netty.nio.Test1
 * @author: niejian9001@163.com
 * @date: 2020/2/3 21:38
 */
public class Test1 {
    public static void main(String[] args) {

        IntBuffer buffer = IntBuffer.allocate(10);
        for (int i = 0; i < buffer.capacity(); i++) {
            int randomNum = new SecureRandom().nextInt(20);
            buffer.put(randomNum);
        }
        // 翻转
        buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
    }
}
