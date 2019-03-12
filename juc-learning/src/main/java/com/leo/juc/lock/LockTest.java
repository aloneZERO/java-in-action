package com.leo.juc.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 用于解决多线程安全问题的方式：
 * 隐式锁
 * 1. 同步代码块（synchronized）
 * 2. 同步方法（synchronized）
 * <p>
 * JDK1.5 之后
 * 3. 同步锁
 * 这是一个显示锁：需要通过 lock() 方法上锁，必须通过 unlock() 方法进行释放锁
 *
 * @author justZero
 * @since 2019/3/11
 */
public class LockTest {
    public static void main(String[] args) {
        TicketTask task = new TicketTask();

        for (int i = 1; i <= 3; i++) {
            new Thread(task, i + "号窗口").start();
        }
    }
}

class TicketTask implements Runnable {
    private int ticket = 100;
    private Lock lock = new ReentrantLock();

    @Override
    public void run() {
        while (true) {
            lock.lock(); // 上锁
            try {
                if (ticket > 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName()
                            + " 完成售票，余票为：" + --ticket);
                } else {
                    break;
                }
            } finally {
                lock.unlock(); // 释放锁
            }
        }
    }
}
