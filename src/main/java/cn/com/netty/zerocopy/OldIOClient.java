package cn.com.netty.zerocopy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.Socket;

/**
 * @desc: cn.com.netty.zerocopy.OldClient
 * @author: niejian9001@163.com
 * @date: 2020/2/12 13:57
 */
public class OldIOClient {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 8899);
        // 从文件获取信息
        // 找一个比较大的文件
        String fileName = "/Users/a/Downloads/apache-jmeter-5.2.1.zip";
        InputStream inputStream = new FileInputStream(fileName);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] buffer = new byte[1024];
        long readCount = 0;
        long total = 0;
        long startTime = System.currentTimeMillis();
        while ((readCount = inputStream.read(buffer)) >= 0) {
            total += readCount;
            dataOutputStream.write(buffer);
        }

        System.out.println("发送总字节数： " + total + ", 耗时：" + (System.currentTimeMillis() - startTime));
        dataOutputStream.close();
        socket.close();

    }
}
