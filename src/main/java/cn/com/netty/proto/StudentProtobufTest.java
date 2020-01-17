package cn.com.netty.proto;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.Date;

public class StudentProtobufTest {
    public static void main(String[] args) throws InvalidProtocolBufferException {
        DataInfo.Student student = DataInfo.Student.newBuilder()
                .setAddress("广州")
                .setAge(10)
                .setId(1)
                .setName("张三")
                .build();
        // 将student转换为二进制数组（序列化）
        byte[] bytes = student.toByteArray();
        System.err.println("student对象序列化： " + bytes);
        // 将二进制数组转换为java对象。（反序列化）
        DataInfo.Student student1 = DataInfo.Student.parseFrom(bytes);
        System.out.println(student1.getAddress());
        System.out.println(student1.getAge());
        System.out.println(student1.getName());
        System.out.println(student1.getId());
        System.err.println("============反序列化对象===========");
        System.out.println(student1.toString());
    }
}
