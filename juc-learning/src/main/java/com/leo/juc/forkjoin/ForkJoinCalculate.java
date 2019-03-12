package com.leo.juc.forkjoin;

import lombok.AllArgsConstructor;

import java.util.concurrent.RecursiveTask;


@AllArgsConstructor
public class ForkJoinCalculate extends RecursiveTask<Long> {

    private static final long serialVersionUID = -4891748235554441810L;

    private long start;
    private long end;

    private static final long THRESHOLD = 10000L; // 临界值

    // 从 start 累加到 end
    @Override
    protected Long compute() {
        long length = end - start;

        if (length <= THRESHOLD) {
            long sum = 0;

            for (long i = start; i <= end; i++) {
                sum += i;
            }

            return sum;
        } else {
            long middle = (start + end) / 2;

            ForkJoinCalculate left = new ForkJoinCalculate(start, middle);
            left.fork(); // 拆分，并将该子任务压入线程队列

            ForkJoinCalculate right = new ForkJoinCalculate(middle + 1, end);
            right.fork();

            return left.join() + right.join();
        }
    }
}
