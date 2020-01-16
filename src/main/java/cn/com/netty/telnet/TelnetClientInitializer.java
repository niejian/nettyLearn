package cn.com.netty.telnet;

import cn.com.netty.common.CommonInstance;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @desc: cn.com.netty.telnet.TelnetClientInitializer
 * @author: niejian9001@163.com
 * @date: 2020/1/16 11:24
 */
public class TelnetClientInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(CommonInstance.STRINGDECODER_NAME, CommonInstance.STRINGDECODER);
        pipeline.addLast(CommonInstance.STRINGENCODER_NAME, CommonInstance.STRINGENCODER);
        pipeline.addLast(new TelnetClientChannelHandler());

    }
}
