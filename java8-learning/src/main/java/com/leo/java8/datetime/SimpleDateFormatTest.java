package com.leo.java8.datetime;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class SimpleDateFormatTest {

    /*
     * SimpleDateFormat 的线程安全问题
     * Caused by: java.lang.NumberFormatException: multiple points
     */
//    public static void main(String[] args) throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//
//        Callable<Date> task = () -> sdf.parse("20190309");
//        ExecutorService pool = Executors.newFixedThreadPool(10);
//
//        List<Future<Date>> results = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            results.add(pool.submit(task));
//        }
//
//        for (Future<Date> future : results) {
//            System.out.println(future.get());
//        }
//
//        pool.shutdown();
//    }

    // 使用 ThreadLocal 解决问题
//    public static void main(String[] args) throws Exception {
//        Callable<Date> task = () ->
//                DateFormatThreadLocal.convert("20190309");
//        ExecutorService pool = Executors.newFixedThreadPool(10);
//
//        List<Future<Date>> results = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            results.add(pool.submit(task));
//        }
//
//        for (Future<Date> future : results) {
//            System.out.println(future.get());
//        }
//
//        pool.shutdown();
//    }

    // java8 提供新的日期 API，实例不可变，每次都产生新实例。
    // 从而解决线程安全问题
    public static void main(String[] args) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");

        Callable<LocalDate> task = () ->
                LocalDate.parse("20190309",dtf);
        ExecutorService pool = Executors.newFixedThreadPool(10);
        List<Future<LocalDate>> results = new ArrayList<>();
        for (int i=0; i< 10; i++) {
            results.add(pool.submit(task));
        }

        results.forEach(r -> {
            try {
                System.out.println(r.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        pool.shutdown();
    }
}
