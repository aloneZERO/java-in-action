package com.leo.juc.volatileModifier;

import lombok.Data;

/**
 * <pre>
 * volatile 关键字：
 * 当多个线程进行操作共享数据时，可以保证内存中的数据可见。
 * 相较于 synchronized 是一种较为轻量级的同步策略。
 *
 * 注意：
 * 1. volatile 不具备“互斥性”
 * 2. volatile 不能保证变量的“原子性”
 * </pre>
 * @author justZero
 * @since 2019/3/11
 */
public class VolatileTest {
    public static void main(String[] args) {
        MyTask task = new MyTask();
        new Thread(task).start();

        // 当 finish 没有被 volatile 修饰时，由于线程缓存的问题，
        // 即使 finish 已经被修改为了 true，
        // 但因为 while(true){} 代码比较底层，执行效率飞快，
        // 主线程来不及去更新主存中最新的 finish 值。
        while (true) {
            if (task.isFinish()) {
                System.out.println("======= 任务结束 =======");
                break;
            }
        }

        // 解决方法一：给主线程同步的时间
        // 显然不够优雅，有点投机取巧的感觉。
//        while (true) {
//            if (task.isFinish()) {
//                System.out.println("======= 任务结束 =======");
//                break;
//            }
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        // 解决方法二：加锁
        // 执行效率大打折扣
//        while (true) {
//            synchronized (task) {
//                if (task.isFinish()) {
//                    System.out.println("======= 任务结束 =======");
//                    break;
//                }
//            }
//        }
    }
}

@Data
class MyTask implements Runnable {
//    private boolean finish = false; // 死循环
    private volatile boolean finish = false;

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finish = true;
        System.out.println("flag=" + isFinish());
    }
}
