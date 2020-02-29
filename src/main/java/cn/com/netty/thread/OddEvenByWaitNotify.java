package cn.com.netty.thread;

/**
 * @desc: cn.com.netty.thread.OddEvenByWaitNotify
 * @author: niejian9001@163.com
 * @date: 2020/2/21 16:41
 */
public class OddEvenByWaitNotify {
    private static final Object lock = new Object();
    private static int count = 0;
    private static final int MAX_COUNT = 100;

    public static void main(String[] args) {
        Runnable runnable = () -> {

            while (count <= MAX_COUNT) {
                // 获取锁，使其他访问的线程阻塞
                synchronized (lock) {
                    try {
                        System.out.println(Thread.currentThread().getName() + ": " + count++);
                        // 唤醒其他线程（在获得锁的情况下）
                        lock.notify();
                        // 等待，放弃持有改资源的锁-将锁的所有权丢给synchronized同步块
                        lock.wait();
//                        lock.notify();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                while (count <= MAX_COUNT ) {
//                    synchronized (lock) {
//                        try {
//                            System.out.println(Thread.currentThread().getName() + ": " + count++);
//                            lock.notify();
//                            // 如果任务还没结束 就让出锁 自己休眠
//                            lock.wait();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        };
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        thread1.start();
        thread2.start();
    }


}
