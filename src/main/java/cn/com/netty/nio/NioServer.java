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
        /**
         * 服务端先注册连接加入事件，然后再处理客户端的读写事件
         */
        // 将channel注册到selector上面
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            try {
                // 返回selector所关注(OP_ACCEPT)的事件数量
                // register(selector, SelectionKey.OP_ACCEPT);
                selector.select();
                // 通过selector获取到连接集合
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                // 注册读取事件
                selectionKeys.forEach(selectionKey -> {
                    final SocketChannel client;
                    try {

                        // 客户端的连接事件
                        if (selectionKey.isAcceptable()) {

                            ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                            // 获取到连接进来的客户端信息
                            client = server.accept();
                            //将client注册到selector上
                            client.configureBlocking(false);
                            // 关注client的read事件
                            client.register(selector, SelectionKey.OP_READ);

                            String key = "[" + UUID.randomUUID() + "]";
                            // 将客户端放到容器中，后续再做其他处理
                            clientMap.put(key, client);
                        } else if (selectionKey.isReadable()){
                            // 服务端处理完客户端的连接事件后，再来处理客户端的读事件
                            // 数据可读了
                            client = (SocketChannel) selectionKey.channel();
                            ByteBuffer readBuffer = ByteBuffer.allocate(512);

                            // 将客户端发送过来的数据读到readBuffer
                            int count = client.read(readBuffer);
                            if (count > 0) {

                                // ByteBuffer翻转。postion = 0， limit = 数据内容大小
                                readBuffer.flip();
                                Charset charset = Charset.forName("utf-8");
                                String receiveMessage = String
                                        .valueOf(charset.decode(readBuffer).array());

                                System.out.println(client + ":" + receiveMessage);
                                String senderKey = null;
                                // 找到发送数据的客户端的key
                                for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                                    if (client == entry.getValue()) {
                                        senderKey = entry.getKey();
                                        break;
                                    }
                                }

                                for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                                    SocketChannel value = entry.getValue();
                                    ByteBuffer writeBuffer = ByteBuffer.allocate(512);
                                    writeBuffer.put(("【服务端返回数据】：" + senderKey + ":" + receiveMessage).getBytes());
                                    writeBuffer.flip();
                                    // 服务端向客户端返回数据
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
