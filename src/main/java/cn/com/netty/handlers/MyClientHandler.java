package cn.com.netty.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @desc: cn.com.netty.handlers.MyClientHandler
 * @author: niejian9001@163.com
 * @date: 2020/4/21 17:46
 */
public class MyClientHandler extends SimpleChannelInboundHandler<PersonProtocol> {

    private int count;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PersonProtocol msg) throws Exception {
        int length = msg.getLength();
        byte[] content = msg.getContent();
        System.out.println("客户端接收到的数据：");
        System.out.println("长度：" + length);
        System.out.println("内容：" + new String(content, CharsetUtil.UTF_8));
        System.out.println("客户端接收的消息数量：" + ++count);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            String message = " send from client" + i;
            byte[] content = message.getBytes(CharsetUtil.UTF_8);
            int length = content.length;
            PersonProtocol personProtocol = new PersonProtocol();
            personProtocol.setContent(content);
            personProtocol.setLength(length);
            ctx.writeAndFlush(personProtocol);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
        ctx.close();
    }
}
