package cn.com.netty.official.snoop;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.util.CharsetUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @desc: cn.com.netty.official.snoop.HttpSnoopServerChannelHandler
 * @author: niejian9001@163.com
 * @date: 2020/1/16 11:55
 */
public class HttpSnoopServerChannelHandler extends SimpleChannelInboundHandler<Object> {
    private HttpRequest request;
    private final StringBuffer buf = new StringBuffer();

    /**
     * 接收到客户端发过来的消息，并给它回馈一个消息
     * @param channelHandlerContext
     * @param o
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

        // 处理http请求
        if (o instanceof HttpRequest) {
            request = (HttpRequest) o;
            String uri = request.uri();
            if (HttpUtil.is100ContinueExpected(request)) {
               send100Continue(channelHandlerContext);
            }

            buf.setLength(0);
            buf.append("WELCOME TO THE WILD WILD WEB SERVER \n\r");
            buf.append("===================================\n\r");
            buf.append("Version:").append(request.protocolVersion()).append("\n\r");
            buf.append("HostName:").append(request.headers().get(HttpHeaderNames.HOST, "unkonw")).append("\n\r");
            buf.append("Uri: ").append(uri).append("\n\r");

            HttpHeaders headers = request.headers();
            if (!headers.isEmpty()) {

                headers.forEach(header -> {
                    String key = header.getKey();
                    String value = header.getValue();
                    buf.append("Header: ").append(key).append(" = ").append(value).append("\n\r");
                });

            }

            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
            // 请求参数
            Map<String, List<String>> parameters = queryStringDecoder.parameters();
            if (!parameters.isEmpty()) {
                for (Map.Entry<String, List<String>> parameter : parameters.entrySet()) {
                    String key = parameter.getKey();
                    List<String> value = parameter.getValue();
                    buf.append("parameter: ").append(key).append(" = ").append(value).append("\n\r");
                }
            }
            appendDecoderResult(buf, request);
        }

        if (o instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) o;
            // 获取到内容
            ByteBuf content = httpContent.content();

            if (content.isReadable()) {
                buf.append("content: ").append(content.toString(CharsetUtil.UTF_8)).append("\n\r");
                appendDecoderResult(buf, request);
            }
        }

        // 是否读完请求数据
        if (o instanceof LastHttpContent) {
            buf.append("end of content \n\r");
            LastHttpContent tailer = (LastHttpContent) o;
            if (!tailer.trailingHeaders().isEmpty()) {
                buf.append("\n\r");
                for (CharSequence name : tailer.trailingHeaders().names()) {
                    for (CharSequence value : tailer.trailingHeaders().getAll(name)) {
                        buf.append("trailing headers: ").append(name).append(" = ")
                                .append(value).append("\n\r");
                    }

                }
            }

            // 不是长连接的返回结果处理
            if (!writeResponse(tailer, channelHandlerContext)) {
                channelHandlerContext.writeAndFlush(Unpooled.EMPTY_BUFFER)
                        .addListener(ChannelFutureListener.CLOSE);
            }
        }

    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.CONTINUE,
                Unpooled.EMPTY_BUFFER);
        ctx.writeAndFlush(response);
    }

    private boolean writeResponse(HttpObject httpObject, ChannelHandlerContext ctx) {
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, httpObject.decoderResult().isSuccess() ? HttpResponseStatus.OK : HttpResponseStatus.BAD_REQUEST,
                Unpooled.copiedBuffer(buf.toString(), CharsetUtil.UTF_8)
        );
        // 如果当前请求时长连接
        if (keepAlive) {

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }

        // 处理cookie
        String cookie = request.headers().get(HttpHeaderNames.COOKIE);
        if (null != cookie) {
            Set<Cookie> cookies = ServerCookieDecoder.STRICT.decode(cookie);
            if (!cookies.isEmpty()) {
                cookies.forEach(c -> {
                    response.headers().add(HttpHeaderNames.SET_COOKIE,
                            ServerCookieEncoder.STRICT.encode(c));
                });
            }
        } else {
            response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode("key1", "value1"));
            response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode("key2", "value2"));
        }


        ctx.writeAndFlush(response);

        return keepAlive;


    }

    private void appendDecoderResult(StringBuffer buf, HttpObject httpObject) {
        DecoderResult decoderResult = httpObject.decoderResult();
        if (!decoderResult.isSuccess()) {
            return;
        }
        buf.append("..with decoder failure: ")
                .append(decoderResult.cause())
                .append("\n\r");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "：加入");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "：channelRegistered");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "：channelActive");
    }
}
