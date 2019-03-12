package com.leo.juc.countdownlatch;

import lombok.AllArgsConstructor;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch
 * 闭锁——在完成某些运算时，只有其他所有线程的运算
 * 全部完成，当前运算才继续执行
 *
 * @author justZero
 * @since 2019/3/11
 */
public class CountDownLatchTest {
    // 需求：计算多线程任务的耗费时间
    public static void main(String[] args) {
        final int threadNum = 5;
        final CountDownLatch latch = new CountDownLatch(threadNum);
        LatchDemo task = new LatchDemo(latch);

        long start = System.currentTimeMillis();

        for (int i=0; i< threadNum; i++) {
            new Thread(task).start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end-start) + "ms");
    }
}

@AllArgsConstructor
class LatchDemo implements Runnable {
    private CountDownLatch latch;

    @Override
    public void run() {
        synchronized (this) {
            try {
                for (int i=0; i< 50000; i++) {
                    if (i % 2 == 0) {
                        System.out.println(Thread.currentThread().getName()
                                + ": " + i);
                    }
                }
            } finally {
                latch.countDown();
            }
        }
    }
}