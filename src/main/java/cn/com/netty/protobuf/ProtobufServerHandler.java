package cn.com.netty.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @user niejian9001@163.com
 * @date 2020/1/18 17:13
 */
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
