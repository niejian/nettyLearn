package cn.com.netty.official.snoop;

import cn.com.netty.common.CommonInstance;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @desc: cn.com.netty.official.snoop.HttpSnoopServerInitializer
 * @author: niejian9001@163.com
 * @date: 2020/1/16 11:53
 */
public class HttpSnoopServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("httpRequestDecoder", new HttpRequestDecoder());
//        pipeline.addLast("httpRequestEncoder", new HttpRequestEncoder());
        pipeline.addLast("httpResponseEncoder", new HttpResponseEncoder());
        pipeline.addLast(new HttpSnoopServerChannelHandler());
    }
}
