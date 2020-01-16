package cn.com.netty.official.snoop;

import com.sun.jndi.toolkit.url.Uri;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.ClientCookieEncoder;
import io.netty.handler.codec.http.cookie.DefaultCookie;

import java.net.URI;
import java.time.LocalDateTime;

/**
 * @desc: cn.com.netty.official.snoop.HttpSnoopClient
 * @author: niejian9001@163.com
 * @date: 2020/1/16 13:55
 */
public class HttpSnoopClient {

    private static final String url = "http://localhost:8890/";

    public static void main(String[] args) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {

            URI uri = new URI(url);

            String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
            String host = uri.getHost() == null ? "localhost" : uri.getHost();
            int port = uri.getPort();

            if (port < 0) {
                return;
            }

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new HttpSnoopClientInitializer());

            Channel channel = bootstrap.connect(host, port).sync().channel();

            HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                    HttpMethod.GET, uri.getRawPath(), Unpooled.EMPTY_BUFFER);
            request.headers().set(HttpHeaderNames.HOST, host);
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);

            request.headers().set(
                    HttpHeaderNames.COOKIE,
                    ClientCookieEncoder.STRICT.encode(
                            new DefaultCookie("my-cookie", LocalDateTime.now() + ""),
                            new DefaultCookie("remote-host", host),
                            new DefaultCookie("remote-port", port + ""),
                            new DefaultCookie("schemem", scheme)
                    )
            );


            channel.writeAndFlush(request);
            channel.closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }



    }
}
