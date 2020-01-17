package cn.com.netty.five;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author niejian
 * @description
 * @file cn.com.netty.five.WebSocketChannelInitializer
 * @create 2020-01-15 19:56
 **/
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());
        // 声明websocket的协议信息
        // netty处理请求是按分段的方式来进行的，这里指定每段的长度
        pipeline.addLast(new HttpObjectAggregator(8192));
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        // 添加超时检查机制
        pipeline.addLast("IdleStateHandler", new IdleStateHandler(5, 7, 10, TimeUnit.MINUTES));
        pipeline.addLast(new MyIdleChannelHandler());
        pipeline.addLast(new TextWebSocketServerHandler());

    }
}
