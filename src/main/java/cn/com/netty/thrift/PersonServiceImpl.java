package cn.com.netty.thrift;

import cn.com.netty.thrift.generate.DataException;
import cn.com.netty.thrift.generate.Person;
import cn.com.netty.thrift.generate.PersonService;
import org.apache.thrift.TException;

/**
 * @user niejian9001@163.com
 * @date 2020/1/19 20:31
 */
public class PersonServiceImpl implements PersonService.Iface {
    @Override
    public Person getPersonByUsername(String username) throws DataException, TException {
        System.err.println("---------");
        System.err.println("get client person: " + username);
        System.err.println("---------");
        Person person = new Person();
        person.setAge(10);
        person.setMarried(false);
        person.setUsername(username);

        return person;
    }

    @Override
    public void savePerson(Person person) throws DataException, TException {
        System.err.println("---------");
        System.err.println("save client person: " );
        System.err.println("---------");
        System.err.println(" age: " + person.getAge());
        System.err.println(" username: " + person.getUsername());
        System.err.println(" married: " + person.married);

    }
}
