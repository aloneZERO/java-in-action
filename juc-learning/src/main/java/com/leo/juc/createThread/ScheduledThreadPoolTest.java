package com.leo.juc.createThread;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @author justZero
 * @since 2019/3/12
 */
public class ScheduledThreadPoolTest {
    public static void main(String[] args) throws Exception {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(3);

        for (int i = 0; i < 3; i++) {
            Future<Integer> result = pool.schedule(() -> {
                int num = new Random().nextInt(100);// 生成随机数
                System.out.println(Thread.currentThread().getName() + " : " + num);
                return num;
            }, 2, TimeUnit.SECONDS); // 上个线程任务完成后，间隔2秒，开启下一个线程任务

            System.out.println(result.get());
        }

        pool.shutdown();
    }
}
