package cn.com.netty.official.snoop;

import cn.com.netty.common.CommonInstance;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;

/**
 * @desc: cn.com.netty.official.snoop.HttpSnoopClientInitializer
 * @author: niejian9001@163.com
 * @date: 2020/1/16 13:59
 */
public class HttpSnoopClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        // Enable HTTPS if necessary.
//      if (sslCtx != null) {
//          p.addLast(sslCtx.newHandler(ch.alloc()));
//      }
//        pipeline.addLast(CommonInstance.STRINGDECODER_NAME, CommonInstance.STRINGDECODER);
//        pipeline.addLast(CommonInstance.STRINGENCODER_NAME, CommonInstance.STRINGENCODER);
        pipeline.addLast(new HttpClientCodec());
        pipeline.addLast(new HttpContentDecompressor());
        pipeline.addLast(new HttpSnoopClientHandler());
    }
}
