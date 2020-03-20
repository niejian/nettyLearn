package cn.com.netty.thread.completefuture;

import io.netty.util.concurrent.CompleteFuture;

import java.util.concurrent.CompletableFuture;

/**
 * CompleteFuture是一个增强的Future接口。除了实现了Future接口外，
 * @desc: cn.com.netty.thread.completefuture.CompleteFutureTest
 * @author: niejian9001@163.com
 * @date: 2020/3/20 21:22
 */
public class CompleteFutureTest implements Runnable{
    CompletableFuture<Integer> re = null;


    public CompleteFutureTest(CompletableFuture<Integer> re) {
        this.re = re;
    }

    @Override
    public void run() {
        int myRe = 0;
        try {
            myRe = re.get() * re.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("myRe = " + myRe);
    }

    public static void main(String[] args) throws Exception {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        new Thread(new CompleteFutureTest(future)).start();
        // 模拟计算时长
        Thread.sleep(3000);
        // 告知完成结果
        // 如果还没计算完，就给-1
        future.complete(-1);

    }
}
