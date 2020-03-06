
## nio与io
java.io中最为核心的概念是流（Stream），面向流的编程。

> java中一个流要么是输入流，要么是输出流。不可能是既是输入流又是输出流。<br/>

java.nio中有3个核心的概念，`Selector`, `Channel` , `Buffer`。面向块编程。

* buffer本身就是一块内存，底层实现上，它实际上就是个数组。数据的 **读、写** 都是通过Buffer来实现的。`io中输入输出只能二者选其一`；
* 除了数组之外，Buffer还提供了数据的结构化访问方式，并且可以追踪到系统的读写位置
* Java中的8中数据类型都有各自对应的Buffer类型；char，byte，boolean、short、int、float、dubbo、long；
* Channel指的是可以向其写入数据或是从中读取数据的对象，类似于io中stream。与stream不同的是，Channel是双向的，一个流只可能是InputStream、OutPutStream。channel打开后则可以进行数据读、写、读写；由于channel是双向的，因此能更好的反映出底层操作系统的工作情况；在Linux系统中，它的通道是双向的；
* 所有数据的读写都是通过Buffer来进行的，<font color=red>**永远不会出现直接向Channel中写入数据，或是直接从Channel中读取数据；**</font>



### java中io重要的设计模式

#### 装饰模式

##### 说明

假设有一个接口Human，一个接口的实现类Man。人类Human是可以跑步的，但是不能飞。

    如果想给人类加上飞翔的翅膀，可以有三种解决方案：

1. 修改实现类Man的方法，但不符合开闭原则
2. 给实现类Man添加一个子类，扩展一个人类可以飞的功能。问题在于，如果又想给人类增加猎豹般奔跑的速度，需要继续扩展一个子类。显然，使用继承的方式去扩展一个类的功能，会增加类的层级，类的臃肿会加大维护的成本。
3. 使用装饰模式扩展一个类的功能。好处在于，如果继承关系是纵向的，那么装饰类则是某个类横向的扩展，并不会影响继承链上的其他类。例如：C extends B , B extends A，如果需要扩展B的功能，可以设计一个B的装饰类，它并不会影响B的子类C。如果采用在B里面增加方法，势必会使B的所有子类结构被改变。



##### uml

![](https://s2.ax1x.com/2020/02/15/1vOKtH.md.jpg)

##### demo

- 定义一个接口Human
- 定义一个被装饰的类Man
- 定义一个装饰的抽象类，**内部持有被装饰类的引用**
- 定义一个装饰的实现类，里面添加需要增强的功能方法

```java
// 装饰类，需要
public abstract class AbstractDecorateHuman implements Human {
    /***
     * 有一个对接口类的引用
     */
    private Human human;

    AbstractDecorateHuman(Human human) {
        this.human = human;
    }

    /**
     * @return
     */
    @Override
    public String run() {
        return human.run();
    }
}
```

```java
// 装饰类继承抽象类并添加增强方法
public class DecorateHuman extends AbstractDecorateHuman {

    public DecorateHuman(Human human) {
        super(human);
    }

    /**
     * 对装饰类添加的增强的方法
     */
    public void fly() {
        System.out.println("增强的fly功能");

    }

    /**
     * @return
     */
    @Override
    public String run() {
        fly();
        return super.run();
    }
}
```



##### 使用场景及优缺点

* 使用场景
  * 替代继承，扩展一个类的功能；
  * 动态给对象添加功能、撤销功能；
* 优点
  * 动态扩展一个实现类的功能，在不需要添加功能的时候，可以撤销装饰；
  * 装饰类和被装饰类模块间，通过抽象产生依赖，不会相互耦合；
  * 装饰模式替换继承，可以避免继承链的子类被影响；

#### 装饰模式与代理模式的区别

装饰模式：侧重给一个实现类添加动态功能，不回对类的方法进行过滤拦截；

代理模式：侧重将一个实现类的功能委托给代理类处理。可以对实现类的方法进行过滤拦截（某种情况下，可能不执行实现类的方法）

### nio的工作模式

![](https://s2.ax1x.com/2020/02/06/1yQ7IP.md.png)

### NIO Buffer

Buffer的底层是由java数组组成的；NIO Buffer中的三个重要状态属性含义

#### position

> 读、写操作时指向的数组下标，随着读、写操作而改变。可以理解成操作JAVA中的数组下标信息；

#### limit

> buffer中第一个不能读取的数组下标索引；

limit的变化情况有以下几种情况：

1. Buffer初始化的时候，此时limit = capacity；
2. 当buffer发生读或写的时候（调用flip()）,此时limit = position；

#### capacity

> buffer包含的元素数量，这个值大于0并且永远不会被改变。

#### flip()方法

flip()方法的智行过程过程；

1. 将limit设置成position；
2. 将position归0，即从头开始读写；

#### nio的零拷贝

* java 堆内存声明；
```java
 ByteBuffer buffer = ByteBuffer.allocate(capacity);
```
* java直接内存声明；
```java
ByteBuffer buffer = ByteBuffer.allocateDirect(capacity);
```
两者之间的区别是：<br/>
1. `ByteBuffer.allocate(capacity)`声明的java对内存，当程序工作的时候，需要将此内存拷贝到堆外内存中再和IO设备交互；
2. `ByteBuffer.allocateDirect(capacity)` 直接声明java堆外的内存，**不需要再次拷贝而直接与IO设备交互**，减少一次拷贝的过程；

#### 代码解析

* 启动一个基于nio的服务器


  ```java
  /**
   * 服务端一个启动一个线程来处理客户端的多个端口的连接请求
   */
      public static void main(String[] args) throws IOException {
          // selector的相关概念
          int[] ports = new int[5];
          ports[0] = 5000;
          ports[1] = 5001;
          ports[2] = 5002;
          ports[3] = 5003;
          ports[4] = 5004;
  
          //构造selector对象
          Selector selector = Selector.open();
  
          for (int i = 0; i < ports.length; i++) {
              ServerSocketChannel socketChannel = ServerSocketChannel.open();
              // 将serverscoket设置成非阻塞的
              socketChannel.configureBlocking(false);
              ServerSocket serverSocket = socketChannel.socket();
              InetSocketAddress address = new InetSocketAddress(ports[i]);
              // 端口绑定
              serverSocket.bind(address);
              // 将channel注册至selector
              // 服务端开启接受客户端连接的操作
              // 当客户端向服务端发起一个链接的时候，服务端会获取到这个链接
              socketChannel.register(selector, SelectionKey.OP_ACCEPT);
              System.out.println("监听端口：" + ports[i]);
          }
  
          while (true) {
              int selectNums = selector.select();
              System.out.println("selectNums = " + selectNums);
              /**
               * 一个selection key包含两种操作集合：
               * 1. interest set
               * 2. ready set
               */
              Set<SelectionKey> selectionKeys = selector.selectedKeys();
              System.out.println("selectionKeys = " + selectionKeys);
              Iterator<SelectionKey> iterator = selectionKeys.iterator();
              while (iterator.hasNext()) {
                  SelectionKey selectionKey = iterator.next();
                  // 判断是否有链接进来
                  if (selectionKey.isAcceptable()) {
                      ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
  
                      SocketChannel socketChannel = serverSocketChannel.accept();
                      socketChannel.configureBlocking(false);
                      // 注册读取事件，为下一阶段获取读取数据做准备
                      socketChannel.register(selector, SelectionKey.OP_READ);
                      // 操作完后就调用remove方法，不然会爆空指针的错误
                      iterator.remove();
                      System.out.println("获取到的客户端连接 : " + socketChannel);
  
                  } else if (selectionKey.isReadable()) {
                      // 操作客户端的读取事件
                      // 当客户端向服务端发送数据的时候，此处被激活。
                      SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
  
                      int bytesRead = 0;
                      while (true) {
                          ByteBuffer byteBuffer = ByteBuffer.allocate(512);
                          byteBuffer.clear();
                          // 将socketchannel中的数据（即客户端输入的数据）读取到bytebuffer中
                          int read = socketChannel.read(byteBuffer);
  
                          if (read <= 0) {
                              break;
                          }
  
                          byteBuffer.flip();
                          // 将bytebuffer中的数据写入到socketchannel中，返回客户端
                          socketChannel.write(ByteBuffer.wrap(SERVER_BACK_MSG.getBytes()));
                          socketChannel.write(byteBuffer);
                          System.out.println("写入信息长度： " + read);
                          bytesRead += read;
  
                      }
  
                      System.out.println("读取信息长度: " + bytesRead + "，来自于：" + socketChannel);
                      // 操作完后就调用remove方法，不然会爆空指针的错误
                      iterator.remove();
                  }
              }
          }
  
      }
  ```

* nio客户端

  ```java
      public static void main(String[] args) {
          try {
              SocketChannel socketChannel = SocketChannel.open();
              socketChannel.configureBlocking(false);
              Selector selector = Selector.open();
              socketChannel.register(selector, SelectionKey.OP_CONNECT);
              socketChannel.connect(new InetSocketAddress("127.0.0.1", 8899));
  
              while (true) {
                  selector.select();
  
                  Set<SelectionKey> selectionKeys = selector.selectedKeys();
                  for (SelectionKey selectionKey : selectionKeys) {
                      if (selectionKey.isConnectable()) {
                          SocketChannel client = (SocketChannel) selectionKey.channel();
  
                          // 是否处于连接过程中
                          if (client.isConnectionPending()) {
                              client.finishConnect();
  
                              ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                              byteBuffer.put((LocalDateTime.now() + "连接成功").getBytes());
                              byteBuffer.flip();
                              client.write(byteBuffer);
  
                              ExecutorService executorService = Executors.newFixedThreadPool(10);
                              executorService.submit(() -> {
                                  while (true) {
                                      byteBuffer.clear();
                                      InputStreamReader input = new InputStreamReader(System.in);
                                      BufferedReader br = new BufferedReader(input);
                                      String sendMessage = br.readLine();
                                      // 客户端向服务端写入数据
                                      byteBuffer.put(sendMessage.getBytes());
                                      byteBuffer.flip();
                                      client.write(byteBuffer);
                                  }
                              });
                          }
  
                          // 注册读取事件，获得服务端的返回数据
                          client.register(selector, SelectionKey.OP_READ);
  
                      } else if (selectionKey.isReadable()) {
                          SocketChannel client = (SocketChannel) selectionKey.channel();
                          ByteBuffer readBuffer = ByteBuffer.allocate(512);
                          int count = client.read(readBuffer);
                          if (count > 0) {
                              String receiveMessage = new String(readBuffer.array());
                              System.out.println("【客户端】接收到服务端返回的数据 ： " + receiveMessage);
                          }
                      }
                  }
  
                  selectionKeys.clear();
              }
  
  
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
  ```
  
### NIO 的零拷贝相关概念

#### 什么是领拷贝

零拷贝描述的是CPU不用执行拷贝数据从一个存储区域到另一个存储区域的任务，这通常用于桐木关通过网络传输一个文件时以减少cpu周期（减少上下文切换）和内存带块。

#### 如何避免数据拷贝

1. 避免操作系统内核缓冲区之间进行数据拷贝操作。
2. 避免操作系统和用户应用程序地址空间这两者之间进行数据拷贝。
3. 用户空间可以避开操作系统直接访问硬件存储。

#### 零拷贝带来的好处

1. 减少甚至完全避免不必要的CPU拷贝，从而让cpu解脱出来去执行其他的任务
2. 减少内存使用
3. 减少上下文切换
4. 零拷贝完全依赖于操作系统

#### 示意图

![](https://s2.ax1x.com/2020/02/12/1Hun9U.md.png)
