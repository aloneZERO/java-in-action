package com.leo.juc.lock;

import com.leo.juc.lock.auxiliary.Clerk;
import com.leo.juc.lock.auxiliary.Consumer;
import com.leo.juc.lock.auxiliary.Producter;

/**
 * 生产者和消费者的等待唤醒
 *
 * @author justZero
 * @since 2019/3/12
 */
public class ProducterAndConsumer {
    public static void main(String[] args) {
        MyClerk clerk = new MyClerk();

        Producter producter = new Producter(clerk);
        Consumer consumer = new Consumer(clerk);

        new Thread(producter, "生产者 A").start();
        new Thread(consumer, "消费者 B").start();
        new Thread(producter, "生产者 C").start();
        new Thread(consumer, "消费者 D").start();
    }
}

class MyClerk implements Clerk {
    private int product = 0;

    @Override
    public synchronized void get() { // 循环次数：0
        while (product >= 1) {
            System.out.println("产品已满！");

            try {
                this.wait(); // 为了避免虚假唤醒问题，应该总是使用在循环中
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(Thread.currentThread().getName() + " : " +
                ++product);
        this.notifyAll();
    }

    @Override
    public synchronized void sale() { // product = 0; 循环次数：0
        while (product <= 0) {
            System.out.println("缺货！");

            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(Thread.currentThread().getName() + " : " +
                --product);
        this.notifyAll();
    }
}
