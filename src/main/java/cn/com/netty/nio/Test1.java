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
        for (int i = 0; i < 5; i++) {
            int randomNum = new SecureRandom().nextInt(20);
            buffer.put(randomNum);
        }
        System.out.println("before flip limit: " + buffer.limit());
        System.out.println("before flip posotion: " + buffer.position());

        // 翻转, limit指向最后一个元素。position指向0
        buffer.flip();
        System.out.println("after flip limit: " + buffer.limit());

        System.out.println("after flip posotion: " + buffer.position());

        System.out.println("=====");
        while (buffer.hasRemaining()) {
            System.out.println("position: " + buffer.position());
            System.out.println("limit: " + buffer.limit());
            System.out.println("capacity: " + buffer.capacity());


            System.out.println(buffer.get());
        }
    }
}
