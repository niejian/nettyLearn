[toc]
# netty学习
首先要明确的一点是： netty并未实现Servlet的相关接口。所以跟我们使用的tomcat的容器是不太一样的。<br/>
简单的来说，netty的编程主要分为三个步骤：
1. server端、client端
```java
public static void main(String[] args) {
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new MyServerInitializer());

        ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
        channelFuture.channel().closeFuture().sync();

    } catch (Exception e) {
        e.printStackTrace();
    }finally {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
```
2. 实现相关的ChannelInitializer继承`ChannelInitializer`；
```java
/**
 * 客户端与服务端建立连接后，这个方法就被调用
 * @param socketChannel
 * @throws Exception
 */
@Override
protected void initChannel(SocketChannel socketChannel) throws Exception {
    ChannelPipeline pipeline = socketChannel.pipeline();
    pipeline.addLast("LengthFieldBasedFrameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
    pipeline.addLast("LengthFieldPrepender", new LengthFieldPrepender(4));
    pipeline.addLast("StringDecoder", new StringDecoder(CharsetUtil.UTF_8));
    pipeline.addLast("StringEncoder", new StringEncoder(CharsetUtil.UTF_8));
    pipeline.addLast("MyClientHandler", new MyClientHandler());
}
```
3. 实现对应的handler继承`SimpleChannelInboundHandler`
```java
 /**
 *
 * @param channelHandlerContext
 * @param s 客户端发送过来的数据
 * @throws Exception
 */
@Override
protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
    System.out.println("客户端：" + channelHandlerContext.channel().remoteAddress() + " : " + s);
    channelHandlerContext.channel().writeAndFlush("get msg form server: " + UUID.randomUUID().toString());
}

@Override
public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();

}
```
## 一个简单的`Hello World`程序
### 服务端编写
1. 声明两个EventLoopGroup;
    1. boss: 接收连接的进程
    2. worker: 处理接收到的连接
2. 绑定NioSocketServerClass;
3. 绑定 childChannel;
    1. channelInitializer;
    2. SimpleChannelInBoundHandler;
## netty的服务器端和客户端的编写

## 学习地址
[B站视频地址信息](https://www.bilibili.com/video/av33707223?p=4)
