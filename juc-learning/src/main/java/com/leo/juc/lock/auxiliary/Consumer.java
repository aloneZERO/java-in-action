package com.leo.juc.lock.auxiliary;

import lombok.AllArgsConstructor;

/**
 * @author justZero
 * @since 2019/3/12
 */
@AllArgsConstructor
public class Consumer implements Runnable {
    private Clerk clerk;

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            clerk.sale();
        }
    }
}
