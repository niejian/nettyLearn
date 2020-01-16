package cn.com.netty.official.snoop;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.Set;

/**
 * @desc: cn.com.netty.official.snoop.HttpSnoopClientHandler
 * @author: niejian9001@163.com
 * @date: 2020/1/16 14:01
 */
public class HttpSnoopClientHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * client端的channelRead0。接收服务端返回的数据再根据实际情况是否再次发送消息给客户端
     * @param channelHandlerContext
     * @param httpObject
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {

        if (httpObject instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) httpObject;
            System.out.println("status: " + response.decoderResult());
            System.out.println("version: " + response.protocolVersion());
            System.out.println();
            if (!response.headers().isEmpty()) {
                Set<String> names = response.headers().names();
                names.forEach(name -> {
                    System.out.println("header: " + name + " = " + response.headers().getAll(name));
                });
                System.out.println();
            }

            if (HttpUtil.isTransferEncodingChunked(response)) {
                System.out.println("chunked content: {");
            } else {
                System.out.println("content {");
            }
        }

        if (httpObject instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) httpObject;

            System.out.println(httpContent.content().toString(CharsetUtil.UTF_8));
            System.err.flush();

            if (httpContent instanceof LastHttpContent) {
                System.out.println("} end of content");
                channelHandlerContext.close();
            }
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
