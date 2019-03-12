package com.leo.juc.createThread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 创建执行线程的方式三：
 * 实现 Callable 接口。相较于实现 Runnable 接口的方式，
 * 方法可以有返回值，并且可以抛出异常。
 *
 * 执行 Callable 方式，需要 FutureTask 实现类的支持，
 * 用于接收运算结果。FutureTask 是 Future 接口的实现类，可用于闭锁
 *
 * @author justZero
 * @since 2019/3/11
 */
public class CallableTest {
    public static void main(String[] args) {

        // 1.需要 FutureTask 实现类的支持，用于接收运算结果。
        FutureTask<Long> result = new FutureTask<>(() -> {
            long sum = 0;
            for (int i=1; i<= Integer.MAX_VALUE-1; i++) {
                sum += i;
            }
            Thread.sleep(1500);
            return sum;
        });

        new Thread(result).start();

        // 2.接收线程运算后的结果
        try {
            System.out.println("----- 等待结果 -----");
            Long sum = result.get(); // 阻塞主线程
            System.out.println(sum);
            System.out.println("----- 计算完毕 -----");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
