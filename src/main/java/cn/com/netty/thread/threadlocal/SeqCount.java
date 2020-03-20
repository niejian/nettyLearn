package cn.com.netty.thread.threadlocal;

/**
 * 基于ThreadLocal的demo
 * @desc: cn.com.netty.thread.threadlocal.SeqCount
 * @author: niejian9001@163.com
 * @date: 2020/3/10 14:11
 */
public class SeqCount {
    static ThreadLocal<Integer> seqCount = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    public int netSeq() {
        seqCount.set(seqCount.get() + 1);
        return seqCount.get();
    }

    static class SeqThread extends Thread {
        private SeqCount seqCount;

        SeqThread(SeqCount seqCount) {
            this.seqCount = seqCount;
        }

        @Override
        public void run() {
            for (int i = 0; i < 30; i++) {
                System.out.println(Thread.currentThread().getName() + " --- num:" + seqCount.netSeq());
            }
        }
    }

    public static void main(String[] args) {
        SeqCount seqCount = new SeqCount();
        SeqThread seqThread1 = new SeqThread(seqCount);
        SeqThread seqThread2 = new SeqThread(seqCount);
        SeqThread seqThread3 = new SeqThread(seqCount);

        seqThread1.start();
        seqThread2.start();
        seqThread3.start();
    }
 }
