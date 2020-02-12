package cn.com.netty.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @desc: cn.com.netty.zerocopy.NewIOClient
 * @author: niejian9001@163.com
 * @date: 2020/2/12 14:26
 */
public class NewIOClient {

    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8899));
        socketChannel.configureBlocking(true);
        String fileName = "/Users/a/Downloads/apache-jmeter-5.2.1.zip";

        FileChannel fileChannel = new FileInputStream(fileName).getChannel();
        long startTime = System.currentTimeMillis();
        // 文件传递至Server端
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);

        System.out.println("发送总字节数 ： " + transferCount + ", 耗时：" + (System.currentTimeMillis() - startTime));
        fileChannel.close();

    }
}
