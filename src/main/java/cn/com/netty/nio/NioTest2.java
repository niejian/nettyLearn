package cn.com.netty.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 传统的io调用相应的方法转换为nio的操作方式
 * @desc: cn.com.netty.nio.NioTest2
 * @author: niejian9001@163.com
 * @date: 2020/2/6 09:00
 */
public class NioTest2 {

    public static void main(String[] args) throws IOException {
        FileInputStream inputStream = new FileInputStream("nioTest.txt");

        // nio channel
        FileChannel channel = inputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        // 将文件的内容读取到bytebuffer
        channel.read(byteBuffer);

        byteBuffer.flip();

        while (byteBuffer.remaining() > 0) {
            byte by = byteBuffer.get();
            System.out.println("charset: " + (char) by);
        }

        channel.close();


    }
}
