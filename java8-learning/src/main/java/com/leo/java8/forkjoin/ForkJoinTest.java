package com.leo.java8.forkjoin;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

public class ForkJoinTest {

    // 从 x 加到 y
    private static final long X = 0L;
    private static final long Y = 10000000000L;

    // jdk1.8 之前 Fork/Join 使用较繁琐
    @Test
    public void test1() {
        Instant start = Instant.now();

        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Long> task = new ForkJoinCalculate(X, Y);

        long sum = pool.invoke(task);
        System.out.println(sum);

        Instant end = Instant.now();
        System.out.println("耗费的时间为: " +
                Duration.between(start, end).toMillis() + "ms");
    }

    @Test
    public void test2() {
        Instant start = Instant.now();

        long sum = 0L;

        for (long i = X; i <= Y; i++) {
            sum += i;
        }
        System.out.println(sum);

        Instant end = Instant.now();
        System.out.println("耗费的时间为: " +
                Duration.between(start, end).toMillis() + "ms");
    }

    // jdk1.8 并行流
    @Test
    public void test3() {
        Instant start = Instant.now();

        Long sum = LongStream.rangeClosed(X, Y)
                .parallel()
                .sum();
        System.out.println(sum);

        Instant end = Instant.now();
        System.out.println("耗费的时间为: " +
                Duration.between(start, end).toMillis() + "ms");
    }

}
