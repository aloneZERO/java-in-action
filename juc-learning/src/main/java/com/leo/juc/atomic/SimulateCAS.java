package com.leo.juc.atomic;

/**
 * <pre>
 * CAS（Compare-And-Swap）算法保证数据变量的原子性
 * CAS 算法是硬件对于并发操作的支持
 * CAS 包含了三个操作数：
 * ① 内存值 V
 * ② 预估值 A（旧值）
 * ③ 更新值 B（新值）
 * 当且仅当 V == A 时， V = B; 否则，不会执行任何操作。
 * </pre>
 * @author justZero
 * @since 2019/3/11
 */
public class SimulateCAS {
    private static final CompareAndSwap CAS = new CompareAndSwap();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                int expectedValue = CAS.get();
                boolean b = CAS.compareAndSet(expectedValue,
                        (int) (Math.random() * 101));
                System.out.println(b);
            }).start();
        }
    }
}

// 模拟 CAS 算法
class CompareAndSwap {
    private int value;

    // 获取内存值
    synchronized int get() {
        return value;
    }

    // 比较
    private synchronized int compareAndSwap(int expectedValue, int newValue) {
        int oldValue = value;

        if (oldValue == expectedValue) {
            this.value = newValue;
        }
        return oldValue;
    }

    // 设置
    synchronized boolean compareAndSet(int expectedValue, int newValue) {
        return expectedValue == compareAndSwap(expectedValue, newValue);
    }
}
