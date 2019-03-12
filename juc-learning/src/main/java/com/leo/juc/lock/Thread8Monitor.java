package com.leo.juc.lock;

import lombok.AllArgsConstructor;

/**
 * 线程八锁，即八种常见情况
 * <p>
 * 题目：判断打印的 "one" or "two" ？
 * <p>
 * 1. 两个普通同步方法，两个线程？    // one two
 * 2. 新增 Thread.sleep() 给 getOne()？ // one two
 * 3. 新增普通方法 getThree()？    //three  one   two
 * 4. 两个普通同步方法，两个 Number 对象？    //two  one
 * 5. 修改 getOne() 为静态同步方法？  // two one
 * 6. 修改两个方法均为静态同步方法，一个 Number 对象？  // one two
 * 7. 一个静态同步方法，一个非静态同步方法，两个 Number 对象？  // two one
 * 8. 两个静态同步方法，两个 Number 对象？    // one two
 * <p>
 * 线程八锁的关键：
 * ①非静态方法的锁默认为 this,  静态方法的锁为对应的 Class 实例
 * ②某一个时刻内，只能有一个线程持有锁，无论几个方法。
 *
 * @author justZero
 * @since 2019/3/12
 */
public class Thread8Monitor {

    public static void main(String[] args) throws Exception {

        // 场景1：两个普通同步方法，两个线程
        Number1 num1 = new Number1();
        new Thread(num1::getOne).start();
        new Thread(num1::getTwo).start();

        Thread.sleep(500);
        System.out.println("-------------------");

        // 场景2：添加 Thread.sleep 卡 getOne()
        Number2 num2 = new Number2();
        new Thread(num2::getOne).start();
        new Thread(num2::getTwo).start();

        Thread.sleep(3000);
        System.out.println("-------------------");

        // 场景3：新增普通方法 getThree()
        Number3 num3 = new Number3();
        new Thread(num3::getOne).start();
        new Thread(num3::getTwo).start();
        new Thread(num3::getThree).start();

        Thread.sleep(3000);
        System.out.println("-------------------");

        // 场景4：两个普通同步方法，两个 Number 对象
        Number3 num3_1 = new Number3();
        Number3 num3_2 = new Number3();
        new Thread(num3_1::getOne).start();
        new Thread(num3_2::getTwo).start();

        Thread.sleep(3000);
        System.out.println("-------------------");

        // 场景5：修改 getOne() 为静态同步方法
        Number4 num4 = new Number4();
        new Thread(() -> num4.getOne()).start();
        new Thread(num4::getTwo).start();

        Thread.sleep(3000);
        System.out.println("-------------------");

        // 场景6：修改两个方法均为静态同步方法，一个 Number 对象
        Number5 num5 = new Number5();
        new Thread(() -> num5.getOne()).start();
        new Thread(() -> num5.getTwo()).start();

        Thread.sleep(3000);
        System.out.println("-------------------");

        // 场景7：一个静态同步方法，一个非静态同步方法，两个 Number 对象
        Number4 num4_1 = new Number4();
        Number4 num4_2 = new Number4();
        new Thread(() -> num4_1.getOne()).start();
        new Thread(num4_2::getTwo).start();

        Thread.sleep(3000);
        System.out.println("-------------------");

        // 场景8：两个静态同步方法，两个 Number 对象
        Number5 num5_1 = new Number5();
        Number5 num5_2 = new Number5();
        new Thread(() -> num5_1.getOne()).start();
        new Thread(() -> num5_2.getTwo()).start();
    }
}

class Number5 {
    static synchronized void getOne() {
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("one");
    }

    static synchronized void getTwo() {
        System.out.println("two");
    }
}

class Number4 {
    static synchronized void getOne() {
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("one");
    }

    synchronized void getTwo() {
        System.out.println("two");
    }
}

@AllArgsConstructor
class Number3 {
    synchronized void getOne() {
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("one");
    }

    synchronized void getTwo() {
        System.out.println("two");
    }

    void getThree() {
        System.out.println("three");
    }
}

@AllArgsConstructor
class Number2 {
    synchronized void getOne() {
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("one");
    }

    synchronized void getTwo() {
        System.out.println("two");
    }
}

@AllArgsConstructor
class Number1 {
    synchronized void getOne() {
        System.out.println("one");
    }

    synchronized void getTwo() {
        System.out.println("two");
    }
}
