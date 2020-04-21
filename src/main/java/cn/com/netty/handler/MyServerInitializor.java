package cn.com.netty.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @desc: cn.com.netty.handler.MyServerInitializor
 * @author: niejian9001@163.com
 * @date: 2020/4/18 21:47
 */
public class MyServerInitializor extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new MyLongToByteEncoder());
        pipeline.addLast(new MyByteToLongDecoder());
        pipeline.addLast("MyServerHandler", new MyServerHandler());
    }
}
