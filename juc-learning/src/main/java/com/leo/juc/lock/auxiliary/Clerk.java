package com.leo.juc.lock.auxiliary;

/**
 * 店员
 *
 * @author justZero
 * @since 2019/3/12
 */
public interface Clerk {
    void get(); // 进货

    void sale(); // 卖货
}
