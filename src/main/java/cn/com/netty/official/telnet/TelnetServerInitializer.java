package cn.com.netty.official.telnet;

import cn.com.netty.common.CommonInstance;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @desc: cn.com.netty.telnet.TelnetServerInitializer
 * @author: niejian9001@163.com
 * @date: 2020/1/16 11:06
 */
public class TelnetServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(CommonInstance.STRINGDECODER_NAME, CommonInstance.STRINGDECODER);
        pipeline.addLast(CommonInstance.STRINGENCODER_NAME, CommonInstance.STRINGENCODER);
        pipeline.addLast(new TelnetServerChannelHandler());
    }
}
