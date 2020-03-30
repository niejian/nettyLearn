[TOC]
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
## 代码大纲
1. 基于netty得到`helloworld`程序；
2. 群聊广播demo;
3. 心跳检测demo;
4. 基于netty的websocket实现;
5. `protobuf`的应用
    1. `protobuf`的简单使用和代码生成
    2. `protobuf`接收多协议的处理方式<br/>
        a. `proto`的文件声明<br/>
        ```proto
           //最外层传递的消息
           message MyMessage {
               enum DataType {
                   StudentType = 1;
                   DogType = 2;
                   CatType = 3;
               }
           
               required DataType date_type = 1;
               // message中有多个可选字段，最多只有一个字段被设置，它是共享内存的。
               // r如果设置了两个oneof，那么后面的oneof会覆盖前面设置的。
               oneof dataBody {
                   Student student = 2;
                   Dog dog = 3;
                   Cat cat = 4;
               }
           
           }
           
           message Student{
               required int32 id = 1;
               required int32 age = 2;
               required string name = 3;
               optional string address = 4;
           }
           
           message Dog {
               optional string name = 1;
               optional int32 age = 2;
           }
           
           message Cat {
               optional string name = 1;
           }
        ```
          b. `Initializer`的处理<br/>
        ```java
           @Override
           protected void initChannel(SocketChannel ch) throws Exception {
               // 传入protobuf的编解码器。这样在handler端就能直接以对象的方式来传递
               ChannelPipeline pipeline = ch.pipeline();
               pipeline.addLast(new ProtobufVarint32FrameDecoder());
               // 将对象转换为字节数组
               pipeline.addLast(new ProtobufDecoder(MyDataInfo.MyMessage.getDefaultInstance()));
               pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
               pipeline.addLast(new ProtobufEncoder());
               pipeline.addLast(new ProtobufServerHandler());
          
           }
        ```
          c. `Handler`的处理
       ```java
        public class ProtobufServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {
        
            @Override
            protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
                MyDataInfo.MyMessage.DataType dataType = msg.getDateType();
        
                if (dataType == MyDataInfo.MyMessage.DataType.StudentType) {
        
                    MyDataInfo.Student student = msg.getStudent();
                    System.out.println("student....");
                    System.out.println("name ： " + student.getName());
                    System.out.println("id ： " + student.getId());
                    System.out.println("address ： " + student.getAddress());
                    System.out.println("age ： " + student.getAge());
                } else if (dataType == MyDataInfo.MyMessage.DataType.DogType) {
                    System.out.println("dog....");
                    MyDataInfo.Dog dog = msg.getDog();
                    System.out.println(dog.getName());
                    System.out.println(dog.getAge());
                } else {
                    System.out.println("cat....");
                    MyDataInfo.Cat cat = msg.getCat();
                    System.out.println(cat.getName());
                }
        
        
            }
        }
       ```
## netty的执行流程
1. 客户端连上netty服务器后，马上调用handlerAdded方法完成channel的添加操作(所谓channel可以理解为一个客户端)
2. 添加操作执行完成以后立马调用channelRegistered方法将channel注入到netty中管理起来
3. 注册好以后调用服务器端的channelActive方法，让其处于激活状态
4. 调用channelRead0方法完成客户端数据的读取和相应
5. 调用完成以后curl主动断开服务器的链接，并通知服务器端，服务器端就会调用channelInactive方法处理回调事件
6. 最后从netty的注册中将该channel删除掉
## 一个简单的`Hello World`程序
### 服务端编写
1. 声明两个EventLoopGroup;
    1. boss: 接收连接的进程
    2. worker: 处理接收到的连接
2. 绑定NioSocketServerClass;
3. 绑定 childChannel;
    1. channelInitializer;
    2. SimpleChannelInBoundHandler;
## 广播的实现
在服务端启动的时候，有新的客户端连接进的时候，服务端向其他的客户端发送：[xxx 加入]信息。
当客户端广播消息的时候，其他客户端接收消息显示[ xxx 发送]；自己显示：[自己发送]。<br/>
**实现这个需求的关键是知道以下几个知识点：**
1. 当新的客户端连接建立的时候，调用什么回调方法；<br/>
    `handlerAdded`。该方法是当有新的客户端连接服务端的时候，服务端触发的回调方法。
2. 服务端怎么保存所有连接进来的客户端连接（channel）； <br/>
    ```java
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    ```



*  `Initilatizer` <br/>

```java
 @Override
protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    pipeline.addLast("DelimiterBasedFrameDecoder", new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()));
    pipeline.addLast("StringDecoder", new StringDecoder(CharsetUtil.UTF_8));
    pipeline.addLast("StringEncoder", new StringEncoder(CharsetUtil.UTF_8));
    pipeline.addLast(new MyChatClientChannelHandler());

}
```

* `handler` <br/>

```java
 /**
 * 保存channel对象
 */
private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
private static final String DATE_PARTTEN = "yyyy-MM-dd HH:mm:ss:SSS";
@Override
protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    Channel channel = ctx.channel();
    channelGroup.forEach(ch -> {
        // 当前遍历的channel不是发送msg的channel对象。则向其他客户端广播
        if (channel != ch) {
            ch.writeAndFlush(channel.remoteAddress() + ", 发送的消息" + msg + "\n");
        } else {
            ch.writeAndFlush("[自己] " + msg + " \n");
        }
    });
}

@Override
public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
}

@Override
public void channelActive(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    System.out.println(channel.remoteAddress() + " 上线了！");
}

@Override
public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    System.out.println(channel.remoteAddress() + " 离开了！");

}

/**
 * 客户端链接建立的时候调用
 * @param ctx
 * @throws Exception
 */
@Override
public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    //super.handlerAdded(ctx);
    // 服务端与客户端建立
    Channel channel = ctx.channel();
    // 向其他链接的客户端发送广播信息
    SocketAddress socketAddress = channel.remoteAddress();
    String date = DateTimeFormatter.ofPattern(DATE_PARTTEN).format(LocalDateTime.now());
    // 向channelGroup中的每一个channel对象发送一个消息
    channelGroup.writeAndFlush(date + " [服务器] - " + socketAddress + " 加入 \n");
    // 保存该客户端链接
    channelGroup.add(channel);
}

/**
 * 链接断开
 * @param ctx
 * @throws Exception
 */
@Override
public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    String date = DateTimeFormatter.ofPattern(DATE_PARTTEN).format(LocalDateTime.now());

    channelGroup.writeAndFlush(date + " [服务器] - " + channel.remoteAddress() + " 离开 \n");
}
```
## 心跳检查程序
* server端还是老样子。
* `initializer`的写法<br/>
  服务端超过5S未读取到信息或者超过7秒未写，或者超过10秒既没有读也没有写，那么就心跳检测失败。<br/>
```java
 @Override
protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    // 在一定的事件间隔之内，链接没有发生任何读写事件，会触发该事件
    // server端的空闲检测； 读空闲： 5 server5秒内未读取到数据，则提示读超时
    pipeline.addLast("IdleStateHandler", new IdleStateHandler(5, 7, 10, TimeUnit.SECONDS));
    pipeline.addLast(new MyServerHandler());
}
```
* `channelHandler`<br/>
  这里的channelHandler就不再是继承`SimpleChannelInBoundHandler`了，而是专门继承另一个；<br/>

```java
public class MyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 触发事件
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 如果是空闲事件
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;
            switch (event.state()) {
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
            }

            System.out.println(ctx.channel().remoteAddress() + " 超时事件： " + eventType);
            ctx.channel().close();
        }
    }
}

```
## 利用netty实现一个WebSocket服务器
* server还是老样子
* `initializerHandler`<br/>
```java
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());
        // 声明websocket的协议信息
        // netty处理请求是按分段的方式来进行的，这里指定每段的长度
        pipeline.addLast(new HttpObjectAggregator(8192));
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        // 添加超时检查机制
        pipeline.addLast("IdleStateHandler", new IdleStateHandler(5, 7, 10, TimeUnit.SECONDS));
        pipeline.addLast(new MyIdleChannelHandler());
        pipeline.addLast(new TextWebSocketServerHandler());

    }
}
```
* `ChannelHandler`<br/>
  我这里有两个handler，分别是上一节讲述的心跳检测的handler和处理文本信息的websocket
    * TextWebSocketServerHandler （文本信息的websocket处理）
    ```java
    public class TextWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
            System.out.println("客户端消息内容： " + msg.text());
            ctx.writeAndFlush(new TextWebSocketFrame("服务器时间" + LocalDateTime.now()));
        }
    
        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            System.out.println("handlerAdded：" + ctx.channel().id().asLongText());
    
        }
    
        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
            System.out.println("handlerRemoved: " + ctx.channel().id().asLongText());
    
        }
    
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
    ```
## RPC框架
1. 定义一个接口的说明文件
2. 通过编译器将这个文件编译成你想调用的那种语言文件
3. 引入编译好的文件发起远程调用 
### [protobuf说明文件](./protobuf.md)

## thrift
* 包名: `thrift`<br/>
* thrift的声明文件<br/>
    src/thrift<br/>
* 生成java代码<br/>
    ```shell script
      thrift --gen java src/thrift/data.thrift
    ```

## ***nio 相关知识***

[***nio相关***](./nio.md)

## Netty 的工作模式

### Reactor模式

###**[Reactor模式介绍](./reactor-desc.md)** 
### `EventLoop`的相关知识点
1. 一个`EventLoopGroup`当中包含一个或者多个`EventLoop`；
2. 一个`EventLoop`在它的整个生命周期当中只与唯一的Thread进行绑定；
3. 所有的`EventLoop`所处理的各种I/O事件都将在它所关联的Thread上面进行处理；
4. **一个`Channel`在它的生命周期只会注册在一个`EventLoop`上**；
5. 一个`EventLoop`会被一个或者多个`Channel`所绑定；

由以上观点可以看出，在Netty中，Channel的实现一定是线程安全的；基于此，我们可以存储一个Channel的引用，并且需要向远程端点发送数据时，通过引用
也能调用channel的相关方法，即便当时有很多线程在使用它也不会出现多线程的问题；而且**消息一定会按照顺序发送出去**

重要结论：我们在业务开发中，不需要将长时间的耗时任务放到`EventLoop`的执行队列中，因为它将会一直阻塞该线程所绑定的的所有Channel的其他执行任务；
如果要进行阻塞调用或者耗时操作，那么我们将要使用一个专门的`EventExecutor`（业务线程池）。<br/>

实现`EventExecutor`有以下两种方式.
1. 使用JDK提供的线程池操作；
2. 使用Netty提供的向ChannelPipeline`addLast(EventExecutorGroup group, ChannelHandler... handlers);`，这样所有的任务就全部丢给所有的这个group线程组执行；

## 目录说明

1. `official` package：[netty官网运行的看起来比较好玩的示例](https://netty.io/wiki/index.html)
2. `以数字序号命名` 的package：B站学习示例

## 学习地址
[B站视频地址信息](https://www.bilibili.com/video/av33707223?p=4)
