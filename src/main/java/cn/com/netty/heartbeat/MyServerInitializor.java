package cn.com.netty.heartbeat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author niejian
 * @description
 * @file cn.com.netty.four.MyServerInitializor
 * @create 2020-01-14 22:13
 **/
public class MyServerInitializor extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 在一定的事件间隔之内，链接没有发生任何读写事件，会触发该事件
        // server端的空闲检测； 读空闲： 5 server5秒内未读取到数据，则提示读超时
        pipeline.addLast("IdleStateHandler", new IdleStateHandler(5, 7, 10, TimeUnit.SECONDS));
        pipeline.addLast(new MyServerHandler());
    }
}
