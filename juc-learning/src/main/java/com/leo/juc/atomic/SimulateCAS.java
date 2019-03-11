package com.leo.juc.atomic;

/**
 * CAS（Compare-And-Swap）算法保证数据变量的原子性
 * CAS 算法是硬件对于并发操作的支持
 * CAS 包含了三个操作数：
 *     ① 内存值 V
 *     ② 预估值 A（旧值）
 *     ③ 更新值 B（新值）
 *     当且仅当 V == A 时， V = B; 否则，不会执行任何操作。
 *
 * @author justZero
 * @since 2019/3/11
 */
public class SimulateCAS {

}
