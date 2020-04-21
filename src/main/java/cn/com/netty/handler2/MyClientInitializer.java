package cn.com.netty.handler2;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author niejian
 * @description
 * @file cn.com.netty.second.MyServerInitializer
 * @create 2020-01-13 20:58
 **/
public class MyClientInitializer extends ChannelInitializer<SocketChannel> {

    /**
     *每次 客户端与服务端建立连接后，这个方法就被调用
     * @param socketChannel
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("MyClientHandler", new MyClientHandler());
    }
}
