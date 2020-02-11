package cn.com.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * nio selector相关概念
 * @desc: cn.com.netty.nio.NioTest12
 * @author: niejian9001@163.com
 * @date: 2020/2/8 14:29
 */
public class NioTest12 {
    public static void main(String[] args) throws IOException {
        // selector的相关概念
        int[] ports = new int[5];
        ports[0] = 5000;
        ports[1] = 5001;
        ports[2] = 5002;
        ports[3] = 5003;
        ports[4] = 5004;

        //构造selector对象
        Selector selector = Selector.open();

        for (int i = 0; i < ports.length; i++) {
            ServerSocketChannel socketChannel = ServerSocketChannel.open();
            // 将serverscoket设置成非阻塞的
            socketChannel.configureBlocking(false);
            ServerSocket serverSocket = socketChannel.socket();
            InetSocketAddress address = new InetSocketAddress(ports[i]);
            // 端口绑定
            serverSocket.bind(address);
            // 注册至selector
            // 当客户端向服务端发起一个链接的时候，服务端会获取到这个链接
            socketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("监听端口：" + ports[i]);
        }

        while (true) {
            int selectNums = selector.select();
            System.out.println("selectNums = " + selectNums);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("selectionKeys = " + selectionKeys);
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 是否有链接进来
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();

                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    // 注册读取事件，为下一阶段获取读取数据做准备
                    socketChannel.register(selector, SelectionKey.OP_READ);

                    iterator.remove();
                    System.out.println("获取到的客户端连接 : " + socketChannel);

                } else if (selectionKey.isReadable()) {
                    // 是否有数据读
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    int bytesRead = 0;
                    while (true) {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
                        byteBuffer.clear();
                        int read = socketChannel.read(byteBuffer);

                        if (read <= 0) {
                            break;
                        }

                        byteBuffer.flip();
                        socketChannel.write(byteBuffer);
                        System.out.println("read = " + read);
                        bytesRead += read;

                    }

                    System.out.println("读取: " + bytesRead + "，来自于：" + socketChannel);

                    iterator.remove();


                }
            }
        }

    }
}
