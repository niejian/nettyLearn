package cn.com.netty.bytebuf;

import cn.com.netty.thrift.generate.Person;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @desc: cn.com.netty.bytebuf.AtomicIntegerFieldUpdaterTest
 * @author: niejian9001@163.com
 * @date: 2020/4/12 21:31
 */
public class AtomicIntegerFieldUpdaterTest {

    public static void main(String[] args) {
        Persons person = new Persons();
//        for (int i = 0; i < 10; i++) {
//            new Thread(() -> {
//                try {
//                    Thread.sleep(20);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(person.age++);
//            }).start();
//
//        }

        AtomicIntegerFieldUpdater<Persons> updater = AtomicIntegerFieldUpdater.newUpdater(Persons.class, "age");

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(updater.getAndIncrement(person));
            }).start();
        }


    }


}

class Persons {
    volatile int age = 1;
}