import java.io.*;

/**
 * 死锁demo
 * @author niejian
 * @description
 * @file PACKAGE_NAME.DeadLock
 * @create 2020-01-13 20:21
 **/
public class DeadLock {

    public static void main(String[] args) throws FileNotFoundException {
        Object o1 = new Object();
        Object o2 = new Object();
        Thread t1 = new Thread(new T1(o1,o2));
        Thread t2 = new Thread(new T2(o1,o2));
        t1.start();
        t2.start();


    }

   static class T1 implements Runnable {
        Object o1, o2;

        public T1(Object o1, Object o2) {
            this.o1 = o1;
            this.o2 = o2;
        }

        @Override
        public void run() {
           synchronized (o1) {
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }

               synchronized (o2) {
                   System.out.println("t1 call o2.....");
               }
           }

        }
    }

    static class T2 implements Runnable {
        Object o1, o2;

        public T2(Object o1, Object o2) {
            this.o1 = o1;
            this.o2 = o2;
        }

        @Override
        public void run() {
            synchronized (o2) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (o1) {
                    System.out.println("t2 call o1....");
                }
            }
        }
    }


}
