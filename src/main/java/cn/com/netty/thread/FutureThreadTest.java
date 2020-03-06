package cn.com.netty.thread;

import org.apache.logging.log4j.core.util.Cancellable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * futureTask测试
 * @desc: cn.com.netty.thread.FutureThreadTest
 * @author: niejian9001@163.com
 * @date: 2020/2/29 11:05
 */
public class FutureThreadTest {
    private static String getTime()  {
        Random random = new Random();

        try {
            int i = random.nextInt(500) + 500;

            Thread.sleep(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "当前线程: "+ Thread.currentThread().getName() + "：当前时间： " +  (LocalDateTime.now()).toString();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(10);

        // 声明线程池的执行器
        ExecutorService executorService = new ThreadPoolExecutor(1, 100, 5, TimeUnit.SECONDS, blockingQueue);
        List<FutureTask<String>> futureTasks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            FutureTask<String> futureTask = new FutureTask<>(
                    new Callable<String>() {
                        @Override
                        public String call() throws Exception {
                            return getTime();
                        }
                    }
            );


            futureTasks.add(futureTask);
            executorService.submit(futureTask);

        }

        for (int i = 0; i < futureTasks.size(); i++) {
            FutureTask<String> futureTask = futureTasks.get(i);
            String s = futureTask.get();
            System.out.println(s);
        }

        executorService.shutdown();





    }

}
