package cn.com.netty.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * nio方式的文件读写
 * @desc: cn.com.netty.nio.NioTest4
 * @author: niejian9001@163.com
 * @date: 2020/2/6 15:47
 */
public class NioTest4 {

    public static void main(String[] args) throws IOException {
        FileInputStream inputStream = new FileInputStream("input.txt");
        FileOutputStream outputStream = new FileOutputStream("output.txt");

        FileChannel inputStreamChannel = inputStream.getChannel();
        FileChannel outputStreamChannel = outputStream.getChannel();

        int capacity = 512;

        ByteBuffer buffer = ByteBuffer.allocate(capacity);

        int index = 1;
        while (true) {
            //重新将buffer中的position指向0
             buffer.clear();

            // 从输入流中将数据写入到buffer
            int read = inputStreamChannel.read(buffer);

            System.out.println("position: " + buffer.position() + ", limit: " + buffer.limit());

            System.out.println("read char num:" + read);

            if (index > 10) {
                break;
            }
            if (-1 == read) {
                break;
            }

            // 每读取一次就将之前读取的数据写到输出文件
            buffer.flip();
            outputStreamChannel.write(buffer);
            index++;

        }

        inputStreamChannel.close();
        outputStreamChannel.close();
    }
}
