package cn.com.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @desc: cn.com.netty.nio.NioServer
 * @author: niejian9001@163.com
 * @date: 2020/2/9 20:39
 */
public class NioServer {

    private static Map<String, SocketChannel> clientMap = new HashMap<>();

    // 一个channel，所有的客户端都连接到这个通道
    public static void main(String[] args) throws IOException {
        // 服务端的处理客户端连接信息
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(8899));

        Selector selector = Selector.open();
        // 将channel注册到selector上面
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            try {
                // 返回selector所关注(OP_ACCEPT)的事件数量
                // register(selector, SelectionKey.OP_ACCEPT);
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                // 注册读取事件
                selectionKeys.forEach(selectionKey -> {
                    final SocketChannel client;
                    try {

                        // 客户端的连接事件
                        if (selectionKey.isAcceptable()) {

                            ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                            client = server.accept();
                            //将client注册到selector上
                            client.configureBlocking(false);
                            // 关注client的read事件
                            client.register(selector, SelectionKey.OP_READ);

                            String key = "[" + UUID.randomUUID() + "]";
                            clientMap.put(key, client);
                        } else if (selectionKey.isReadable()){
                            // 数据可读了
                            client = (SocketChannel) selectionKey.channel();
                            ByteBuffer readBuffer = ByteBuffer.allocate(512);

                            // 将数据读到readBuffer
                            int count = client.read(readBuffer);
                            if (count > 0) {

                                readBuffer.flip();
                                Charset charset = Charset.forName("utf-8");
                                String receiveMessage = String
                                        .valueOf(charset.decode(readBuffer).array());

                                System.out.println(client + ":" + receiveMessage);
                                String senderKey = null;
                                for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                                    if (client == entry.getValue()) {
                                        senderKey = entry.getKey();
                                        break;
                                    }
                                }

                                for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                                    SocketChannel value = entry.getValue();
                                    ByteBuffer writeBuffer = ByteBuffer.allocate(512);
                                    writeBuffer.put((senderKey + ":" + receiveMessage).getBytes());
                                    writeBuffer.flip();
                                    value.write(writeBuffer);

                                }

                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                // 做完所有操作后，要清除所有的selectionKey，否则会爆出NPE
                selectionKeys.clear();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }
}
