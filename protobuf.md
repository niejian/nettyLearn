## protobuf
[IDL: interface definition language]()工具。转化不同语言间的服务调用的，生成适用于客户端语言的客户端代码。
### protobuf 序列化优点
* 语言、平台无关性
* 高效：比xml更快更简单
* 扩展性、兼容性较好：你可以更新数据结构而不破坏、影响旧有程序
### 官网地址
[**protocbuf官网地址**](https://developers.google.cn/protocol-buffers/docs/javatutorial?hl=zh_cn)
### protobuf文件说明
如下所示：<br/>
```proto
syntax = "proto2";
// 如果未提供java_package 生成的类就以package来命名
package tutorial;
option java_package = "cn.com.netty.proto";
option java_outer_classname = "AddressBookProto";
message Person {
    required string name = 1;
    required int32 id = 2;
    optional string email = 3;

    enum PhoneType {
        MOBILE = 0;
        HOME = 1;
        WORK = 2;
    }

    message PhoneNumber {
        required string num = 1;
        optional PhoneType type = 2 [default = HOME];
    }

    repeated PhoneNumber phones = 4;
}

message AddressBook {

    repeated Person people = 1;
}
```
* package <br/>
    `.proto`文件提供的`package` 防止在不同的同名文件的冲突。
* java_package <br/>
    可选，在生成的java类中，如果写了，包路径就是`java_package`；如果没写，那么就按声明的`package`来生成；
* java_outer_classname <br/>
    可选，生成的java类名。如果没有，那么就以该proto文件的驼峰命名方式生成类名
* message <br/>
    可以理解为java中的`class`的概念。
    * 字段类型：<br/>
    
    |  .proto类型    |   JAVA类型   |  c++类型   | 备注|
    | ---- | ---- | ---- | ---- |
    | double | double | double | |
    | float | float | float | |
    | int32 | int | int32| |
    | int64 | long | int64 | |
    | unint32 | int[1] | unint32 | 总数4个字节。如果数值总是比总是比228大的话，这个类型会比uint32高效|
    | unint64 | long[1] | unint64 | 总数8个字节。如果数值总是比总是比256大的话，这个类型会比uint64高效。| 
    | sint32 | int | int32 | 有符号整型，比int32效率高 |
    |sint64 | long | int64 | 有符号整型数据 |
    | fixed32 | int[1] | unint32| |
    | fixed64 | long[1] | unint64| |
    | sfixed32 | int | int32 | 总是4个字节 |
    | sfixed64 | long | int64 | 总是8个字节 |
    | bool | boolean | bool | |
    | string | String | string| |
    | bytes | ByteString | string | |

    * 特殊字段 <br/>
    
    | 名称 | 中文 | 备注 |
    | ---- | ---- | ---- |
    | enum | 枚举（数字从0开始）为字段指定初始值 | enum Type{MAN=0, WOMAN=1} |
    | message | 结构体 | message User {} |
    | repeated | 数组/集合 | repeated = 1|
    | import | 导入定义 | import "protos/user_address.protoc" |
    | // | 注释 | |
    | extend | 扩展 | extend User {} |
    | package | 包 | |
    
#### 根据protoc文件生成java类
```shell script
protoc --java_out=./src/main/java ./src/main/resources/protocbuf/Student2.proto
```         
 

    