package com.leo.juc.lock;

import com.leo.juc.lock.auxiliary.Clerk;
import com.leo.juc.lock.auxiliary.Consumer;
import com.leo.juc.lock.auxiliary.Producter;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock 实现生产者和消费者的等待唤醒
 *
 * @author justZero
 * @since 2019/3/12
 */
public class ProducterAndConsumerWithLock {
    public static void main(String[] args) {
        ClerkWithLock clerk = new ClerkWithLock();
        Producter producter = new Producter(clerk);
        Consumer consumer = new Consumer(clerk);

        new Thread(producter, "生产者-赵").start();
        new Thread(consumer, "消费者-钱").start();
        new Thread(producter, "生产者-孙").start();
        new Thread(consumer, "消费者-李").start();
    }
}

class ClerkWithLock implements Clerk {
    private int product = 0;

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    @Override
    public void get() {
        lock.lock();

        try {
            while (product >= 1) { // 1 表示一个货架上最多一个商品
                System.out.println("产品已满！");
                condition.await();
            }

            System.out.println(Thread.currentThread().getName() + " : "
                    + ++product);
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void sale() {
        lock.lock();

        try {
            if (product <= 0) {
                System.out.println("缺货！");
                condition.await();
            }

            System.out.println(Thread.currentThread().getName() + " : "
                    + --product);
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
