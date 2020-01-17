package cn.com.netty.official.file;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.SystemPropertyUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * @desc: cn.com.netty.official.file.FileServerChannelHandler
 * @author: niejian9001@163.com
 * @date: 2020/1/16 17:03
 */
public class FileServerChannelHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private FullHttpRequest request;
    private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[^-\\._]?[^<>&\\\"]*");
    public static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";

    public static final String HTTP_DATE_GMT_TIMEZONE = "GMT";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        this.request = request;
        if (!request.decoderResult().isSuccess()) {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);

        }


        if (!HttpMethod.GET.equals(request.method())) {
            this.sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }

        boolean keepAlive = HttpUtil.isKeepAlive(request);
        String uri = request.uri();
        String path = sanitizeUri(uri);
        if (null == path) {
            this.sendError(ctx, HttpResponseStatus.FORBIDDEN);
            return;
        }

        File file = new File(path);
        // 文件不存在
        if (file.isHidden() || !file.exists()) {
            this.sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }

        if (file.isDirectory()) {
            if (uri.endsWith("/")) {
                this.sendListing(ctx, file, uri);

            } else {
                this.sendRedirect(ctx, uri + '/');
            }
            return;
        }

        if (!file.isFile()) {
            sendError(ctx, HttpResponseStatus.FORBIDDEN);
            return;
        }

        // cache validation
        String ifModifiedSince = request.headers().get(HttpHeaderNames.IF_MODIFIED_SINCE);
        if (null != ifModifiedSince && !ifModifiedSince.isEmpty()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(HTTP_DATE_FORMAT);
            Date date = simpleDateFormat.parse(ifModifiedSince);

            long dataTimestamp = date.getTime() / 1000;
            long fileLastModified = file.lastModified() / 1000;
            if (dataTimestamp == fileLastModified) {
                this.sendNotModified(ctx);
                return;
            }
        }

        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "r");
        } catch (Exception e) {
            sendError(ctx, HttpResponseStatus.NOT_FOUND);

            e.printStackTrace();
        }

        if (null == raf) {
            this.sendError(ctx, HttpResponseStatus.NOT_FOUND);

            return;
        }

        long fileLength = raf.length();
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpUtil.setContentLength(response, fileLength);
        setContentTypeHeader(response, file);
        setDateAndCacheHeaders(response, file);


        if (!keepAlive) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        } else if (request.protocolVersion().equals(HttpVersion.HTTP_1_0)) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }

        ctx.writeAndFlush(response);

        // write content
        ChannelFuture sendChannelFuture = null;
        ChannelFuture lastContentFuture = null;
        if (ctx.pipeline().get(SslHandler.class) == null) {
            sendChannelFuture =
                    ctx.write(new DefaultFileRegion(raf.getChannel(), 0, fileLength), ctx.newProgressivePromise());
            // write the end mark
            lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        }

        sendChannelFuture.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long process, long total) throws Exception {
                if (total < 0) {
                    System.err.println(future.channel() + " transfer process: " + process);
                } else {
                    System.err.println(future.channel() + " transfer process: " + (process / total) * 100 + "%");

                }
            }

            @Override
            public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                System.err.println(future.channel() + " 传输完成！");
            }
        });

        if (!keepAlive) {
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }




    }



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(LocalDateTime.now() + " hello: " + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private void setContentTypeHeader(HttpResponse response, File file) {
        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, mimetypesFileTypeMap.getContentType(file.getPath()));
    }

    private void sendRedirect(ChannelHandlerContext ctx, String newUri) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND, Unpooled.EMPTY_BUFFER);
        response.headers().set(HttpHeaderNames.LOCATION, newUri);
        this.sendAndCleanupConnection(ctx, response);

    }

    private void sendListing(ChannelHandlerContext ctx, File dir, String dirPath) {
        StringBuilder buf = new StringBuilder();
        buf.append("<!DOCTYPE html>\r\n")
            .append("<html><head><meta charset='utf-8' /><title>")
            .append("Listing of: ")
            .append(dirPath)
            .append("</title></head><body>\r\n")
            .append("<h3>Listing of: ")
            .append(dirPath)
            .append("</h3>\r\n")
            .append("<ul>")
            .append("<li><a href=\"../\">..</a></li>\r\n");

        for (File f : dir.listFiles()) {
            if (f.isHidden() || !f.canRead()) {
                continue;
            }

            String name = f.getName();
            if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
                continue;
            }

            buf.append("<li><a href=\"")
                    .append(name)
                    .append("\">")
                    .append(name)
            .append("</a></li>\n\r");
        }
        buf.append("</ul></body></html>\n\r");

        ByteBuf buffer = ctx.alloc().buffer(buf.length());
        buffer.writeCharSequence(buf.toString(), CharsetUtil.UTF_8);

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buffer);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        this.sendAndCleanupConnection(ctx, response);

    }

    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

    private static String sanitizeUri(String uri) {
        // decode path
        try {
            uri = URLDecoder.decode(uri, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == uri || "".equals(uri.trim())) {
            return null;
        }

        // convert file separators
        uri = uri.replace('/', File.separatorChar);
        if (uri.contains(File.separator + ".")
            || uri.contains("." + File.separator)
            || uri.charAt(0) == '.'
            || uri.charAt(uri.length() - 1 ) == '.'
            || INSECURE_URI.matcher(uri).matches()) {
            return null;
        }

        return SystemPropertyUtil.get("user.dir") + File.separator + uri;
    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_MODIFIED,
                Unpooled.copiedBuffer("failure: " + status + "\n\r", CharsetUtil.UTF_8));

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");

        this.sendAndCleanupConnection(ctx, response);
    }

    private void sendAndCleanupConnection(ChannelHandlerContext ctx, FullHttpResponse response) {
        final FullHttpRequest request = this.request;
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        // 长连接
        if (!keepAlive) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        } else if (request.protocolVersion().equals(HttpVersion.HTTP_1_1)) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }

        ChannelFuture ch = ctx.writeAndFlush(response);

        if (!keepAlive) {
            ch.addListener(ChannelFutureListener.CLOSE);
        }

    }

    private void setDateAndCacheHeaders(HttpResponse response, File fileToCache) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(HTTP_DATE_FORMAT);
//        simpleDateFormat.setTimeZone(TimeZone.getTimeZone());
        response.headers().set(HttpHeaderNames.DATE, simpleDateFormat.format(new Date()));
    }

    private void sendNotModified(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.NOT_MODIFIED, Unpooled.EMPTY_BUFFER);
        setDateHeader(response);
        this.sendAndCleanupConnection(ctx, response);

    }

    private void setDateHeader(FullHttpResponse response) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(HTTP_DATE_FORMAT);
        response.headers().set(HttpHeaderNames.DATE, simpleDateFormat.format(new Date()));
    }
}
