package cn.com.netty.nio;

import java.io.IOException;
import java.nio.channels.Selector;

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

    }
}
