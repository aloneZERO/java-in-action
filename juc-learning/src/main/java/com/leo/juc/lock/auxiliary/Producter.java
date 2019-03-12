package com.leo.juc.lock.auxiliary;

import lombok.AllArgsConstructor;

/**
 * @author justZero
 * @since 2019/3/12
 */
// 生产者
@AllArgsConstructor
public class Producter implements Runnable {
    private Clerk clerk;

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clerk.get();
        }
    }
}
