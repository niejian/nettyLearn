package cn.com.netty.zerocopy;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 非领拷贝方式
 * @desc: cn.com.netty.zerocopy.OldServer
 * @author: niejian9001@163.com
 * @date: 2020/2/12 13:51
 */
public class OldIOServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8899);

        while (true) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            try {

                byte[] byteArray = new byte[1024];
                // 不断的从客户端读取数据
                while (true) {
                    int readCount = dataInputStream.read(byteArray, 0, byteArray.length);

                    if (readCount == -1) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
