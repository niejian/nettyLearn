package cn.com.netty.thread.completefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 利用CompletableFuture实现一个异步的demo
 * @desc: cn.com.netty.thread.completefuture.CompleteableAsyncTest
 * @author: niejian9001@163.com
 * @date: 2020/3/20 22:06
 */
public class CompleteableAsyncTest {

    /**
     * 计算任务
     */
    static class Calc implements Runnable {
        @Override
        public void run() {
            try {
                // 模拟一个长时间的操作
                Thread.sleep(1000);
                System.out.println("计算进行中....");

            } catch (Exception e) {

            }
        }
    }

    static Integer calc(Integer param) {
        try {
            // 模拟一个长时间的操作
            System.out.println("计算进行中....");
            Thread.sleep(1000);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return param * param;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /*CompletableFuture<Void> future = CompletableFuture.runAsync(new Calc());

        while (!future.isDone()) {

        }

        System.out.println("运行完成");*/

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> calc(50));
        System.out.println("立即返回：" + future.getNow(-1));
        while (!future.isDone()) {
            System.out.println("还没计算完，等待结果返回....");
        }

        System.out.println("计算完成：" + future.get());
    }
}
