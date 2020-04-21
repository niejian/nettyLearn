package cn.com.netty.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

/**
 * @desc: cn.com.netty.handlers.MyServerHandler
 * @author: niejian9001@163.com
 * @date: 2020/4/21 17:40
 */
public class MyServerHandler extends SimpleChannelInboundHandler<PersonProtocol> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PersonProtocol msg) throws Exception {
        int length = msg.getLength();
        byte[] content = msg.getContent();
        System.out.println("服务端接收到的数据：");
        System.out.println("长度：" + length);
        System.out.println("内容：" + new String(content, CharsetUtil.UTF_8));
        System.out.println("服务端接收的消息数量：" + ++count);
        // 服务端返回数据，也要经过自定义协议的处理
        String responseMsg = UUID.randomUUID().toString();
        byte[] responseMsgBytes = responseMsg.getBytes(CharsetUtil.UTF_8);
        int responseLength = responseMsgBytes.length;
        PersonProtocol personProtocol = new PersonProtocol();
        personProtocol.setLength(responseLength);
        personProtocol.setContent(responseMsgBytes);
        ctx.writeAndFlush(personProtocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
