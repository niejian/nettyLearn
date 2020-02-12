package cn.com.netty.zerocopy;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @desc: cn.com.netty.zerocopy.NewIOServer
 * @author: niejian9001@163.com
 * @date: 2020/2/12 14:04
 */
public class NewIOServer {
    public static void main(String[] args) throws Exception {
        InetSocketAddress address = new InetSocketAddress(8899);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        // 连接复用
        serverSocket.setReuseAddress(true);

        serverSocket.bind(address);

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        while (true) {

            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(true);
            int readCount = 0;

            while (-1 != readCount) {
                try {
                    readCount = socketChannel.read(byteBuffer);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                byteBuffer.clear();

            }

        }
    }
}
