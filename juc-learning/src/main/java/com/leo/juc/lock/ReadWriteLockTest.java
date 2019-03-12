package com.leo.juc.lock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReadWriteLock: 读写锁
 * 1. 写写/读写，需要“互斥”
 * 2. 读读，不需要互斥
 *
 * @author justZero
 * @since 2019/3/12
 */
public class ReadWriteLockTest {
    public static void main(String[] args) {
        ReadWriteLockDemo rw = new ReadWriteLockDemo();

        for (int i = 1; i <= 8; i++) {
            new Thread(() -> rw.set((int) (Math.random() * 101)),
                    "Write-" + i).start();
        }

        for (int i = 1; i <= 20; i++) {
            new Thread(rw::get, "Read-" + i).start();
        }
    }
}

class ReadWriteLockDemo {
    private int number = 0;

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    // 读
    void get() {
        lock.readLock().lock(); // 读锁，可有多个线程持有

        try {
            System.out.println(Thread.currentThread().getName() + " 准备读数据");
            Thread.sleep((long) (Math.random() * 1000));
            System.out.println(Thread.currentThread().getName() + " 读取" + number);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }

    // 写
    void set(int number) {
        lock.writeLock().lock(); // 写锁，一次只能一个线程持有

        try {
            System.out.println(Thread.currentThread().getName() + " 准备写数据");
            Thread.sleep((long) (Math.random() * 1000));
            this.number = number;
            System.out.println(Thread.currentThread().getName() + " 写入 " + number);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
