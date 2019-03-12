package com.leo.juc.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 编写一个程序，开启 3 个线程，这三个线程的 ID 分别为 A、B、C，
 * 每个线程将自己的 ID 在屏幕上打印 10 遍，要求输出的结果必须按顺序显示。
 * 如：ABCABCABC…… 依次递归
 *
 * @author justZero
 * @since 2019/3/12
 */
public class AlternatePrintingTest {
    public static void main(String[] args) {
        AlternatePrinting ap = new AlternatePrinting();

        new Thread(() -> {
            for (int i=1; i<= 20; i++) {
                ap.loopA(i);
            }
        }, "A").start();

        new Thread(() -> {
            for (int i=1; i<= 20; i++) {
                ap.loopB(i);
            }
        }, "B").start();

        new Thread(() -> {
            for (int i=1; i<= 20; i++) {
                ap.loopC(i);
                System.out.println("----------------");
            }
        }, "C").start();
    }
}

class AlternatePrinting {
    private int number = 1; // 当前正在执行线程的标记

    private Lock lock = new ReentrantLock();
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private Condition condition3 = lock.newCondition();

    /**
     * @param currentLoop 循环第几轮
     */
    void loopA(int currentLoop) {
        lock.lock();

        try {
            // 1. 判断
            if (number != 1) {
                condition1.await();
            }

            // 2. 打印
            System.out.println(Thread.currentThread().getName()
                        + "\t 第" + currentLoop + "轮");

            // 3. 唤醒
            number = 2;
            condition2.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    void loopB(int currentLoop) {
        lock.lock();

        try {
            if (number != 2) {
                condition2.await();
            }

            System.out.println(Thread.currentThread().getName()
                    + "\t 第" + currentLoop + "轮");

            number = 3;
            condition3.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    void loopC(int currentLoop) {
        lock.lock();

        try {
            if (number != 3) {
                condition3.await();
            }

            System.out.println(Thread.currentThread().getName()
                    + "\t 第" + currentLoop + "轮");

            number = 1;
            condition1.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}