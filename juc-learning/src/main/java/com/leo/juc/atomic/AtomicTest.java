package com.leo.juc.atomic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * i++ 的原子性问题：i++ 的操作实际上分为三个步骤“读-改-写”
 *   int i = 10;
 *   i = i++; //10
 * 可看作如下三步：
 *   int temp = i;
 *   i = i + 1;
 *   i = temp;
 *
 * 原子变量：在 java.util.concurrent.atomic 包下提供了一些原子变量。
 *   1. volatile 保证内存可见性
 *   2. CAS（Compare-And-Swap）算法保证数据变量的原子性
 *      CAS 算法是硬件对于并发操作的支持
 *      CAS 包含了三个操作数：
 *        ① 内存值 V
 *        ② 预估值 A（旧值）
 *        ③ 更新值 B（新值）
 *        当且仅当 V == A 时， V = B; 否则，不会执行任何操作。
 *
 * @author justZero
 * @since 2019/3/11
 */
public class AtomicTest {
    static final Set<Integer> SET1 = Collections.synchronizedSet(new HashSet<>());
    static final Set<Integer> SET2 = Collections.synchronizedSet(new HashSet<>());
    static final Set<Integer> SET3 = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        AtomicDemo1 task1 = new AtomicDemo1();
        AtomicDemo2 task2 = new AtomicDemo2();
        AtomicDemo3 task3 = new AtomicDemo3();

        for (int i=0; i< 100; i++) {
            SET1.clear();
            SET2.clear();
            SET3.clear();
            for (int k = 0; k < 10; k++) {
                new Thread(task1).start();
                new Thread(task2).start();
                new Thread(task3).start();
            }

            try {
                // 保证线程任务全部结束
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 正确结果应为 10
            System.out.println(SET1.size());
            System.out.println(SET2.size());
            System.out.println(SET3.size());
            System.out.println("--------------------");
        }
    }
}

// 使用原子变量保证原子性操作
class AtomicDemo3 implements Runnable {
    private AtomicInteger serialNumber = new AtomicInteger(0);

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AtomicTest.SET3.add(getSerialNumber());
    }

    private int getSerialNumber() {
        return serialNumber.getAndIncrement();
    }
}

// volatile 并不能解决非原子性操作问题
class AtomicDemo2 implements Runnable {
    private volatile int serialNumber = 0;

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AtomicTest.SET2.add(getSerialNumber());
    }

    private int getSerialNumber() {
        return serialNumber++;
    }
}

// 可能出现脏读：添加未加一的 serialNumber 值
class AtomicDemo1 implements Runnable {
    private int serialNumber = 0;

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AtomicTest.SET1.add(getSerialNumber());
    }

    private int getSerialNumber() {
        return serialNumber++;
    }
}
