package cn.com.netty.six;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

/**
 *
 * @user niejian9001@163.com
 * @date 2020/1/18 17:19
 */
public class ProtobufClientHandler extends SimpleChannelInboundHandler<MyDataInfo.Student> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.Student msg) throws Exception {

    }

    /**
     * 建立连接时，向服务端发送消息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        MyDataInfo.MyMessage myMessage = null;

        // 0, 1, 2
        int randomInt = new Random().nextInt(3);
        if (randomInt == 0) {
            MyDataInfo.Student student = MyDataInfo.Student.newBuilder()
                    .setId(1001)
                    .setAddress("广州")
                    .setAge(30)
                    .setName("张三")
                    .build();
            // student
            myMessage = MyDataInfo.MyMessage.newBuilder()
                    .setDateType(MyDataInfo.MyMessage.DataType.StudentType)
                    .setStudent(student)
                    .build();
        } else if (randomInt == 1) {
            // dog
            MyDataInfo.Dog dog = MyDataInfo.Dog.newBuilder()
                    .setAge(10)
                    .setName("旺财")
                    .build();
            myMessage = MyDataInfo.MyMessage.newBuilder()
                    .setDateType(MyDataInfo.MyMessage.DataType.DogType)
                    .setDog(dog)
                    .build();
        } else {
            // cat
            MyDataInfo.Cat cat = MyDataInfo.Cat.newBuilder()

                    .setName("喵喵")
                    .build();
            myMessage = MyDataInfo.MyMessage.newBuilder()
                    .setDateType(MyDataInfo.MyMessage.DataType.CatType)
                    .setCat(cat)
                    .build();
        }
        // 建立连接后，向服务端发送这个student对象
        if (null != myMessage) {
            ctx.writeAndFlush(myMessage);
        }

    }
}
