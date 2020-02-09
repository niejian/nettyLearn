package cn.com.netty.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 内存映射文件
 * @desc: cn.com.netty.nio.NioTest9
 * @author: niejian9001@163.com
 * @date: 2020/2/8 08:06
 */
public class NioTest9 {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("NioTest9", "rw");

        FileChannel channel = randomAccessFile.getChannel();

        // 将文件中的某些数据映射到堆外内存中
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        // 直接操作堆外内存中指定数据
        mappedByteBuffer.put(0, (byte) 'a');
        mappedByteBuffer.put(3, (byte) '1');

        randomAccessFile.close();


    }
}
