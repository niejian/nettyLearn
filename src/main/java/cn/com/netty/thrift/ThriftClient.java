package cn.com.netty.thrift;

import cn.com.netty.thrift.generate.Person;
import cn.com.netty.thrift.generate.PersonService;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * @user niejian9001@163.com
 * @date 2020/1/19 20:41
 */
public class ThriftClient {
    public static void main(String[] args) {
        TTransport tTransport = new TFramedTransport(new TSocket("localhost", 8899), 600);
        TProtocol tProtocol = new TCompactProtocol(tTransport);
        PersonService.Client client = new PersonService.Client(tProtocol);

        try {
            tTransport.open();

            Person person = client.getPersonByUsername("张三");
            System.err.println(" age: " + person.getAge());
            System.err.println(" username: " + person.getUsername());
            System.err.println(" married: " + person.married);
            System.out.println("=============");
            Person person1 = new Person();
            person1.setAge(10);
            person1.setMarried(false);
            person1.setUsername("李四");
            client.savePerson(person1);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            tTransport.close();
        }
    }
}
