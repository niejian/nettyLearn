package cn.com.netty.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @desc: cn.com.netty.nio.NioTest3
 * @author: niejian9001@163.com
 * @date: 2020/2/6 09:06
 */
public class NioTest3 {

    public static void main(String[] args) throws IOException {
        FileOutputStream outputStream = new FileOutputStream("nioTest3.txt");

        FileChannel channel = outputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);


        byte[] message = "hello nio test3".getBytes();
        // 数据写入到buffer
        for (int i = 0; i < message.length; i++) {
            byteBuffer.put(message[i]);
        }

        byteBuffer.flip();


        channel.write(byteBuffer);

        channel.close();
    }
}
