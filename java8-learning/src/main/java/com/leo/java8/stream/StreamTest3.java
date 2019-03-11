package com.leo.java8.stream;

import com.leo.java8.other.Data;
import com.leo.java8.other.domain.Employee;
import com.leo.java8.other.domain.Employee.Status;
import org.junit.Test;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.*;


public class StreamTest3 {

    //3. 终止操作
    /*
     * allMatch——检查是否匹配所有元素
     * anyMatch——检查是否至少匹配一个元素
     * noneMatch——检查是否没有匹配的元素
     * findFirst——返回第一个元素
     * findAny——返回当前流中的任意元素
     * count——返回流中元素的总个数
     * max——返回流中最大值
     * min——返回流中最小值
     */
    @Test
    public void test1() {
        boolean res1 = Data.EMPS.stream()
                .allMatch((e) -> e.getStatus().equals(Status.BUSY));
        assertFalse(res1);

        boolean res2 = Data.EMPS.stream()
                .anyMatch((e) -> e.getStatus().equals(Status.BUSY));
        assertTrue(res2);

        boolean res3 = Data.EMPS.stream()
                .noneMatch((e) -> e.getStatus().equals(Status.BUSY));
        assertFalse(res3);
    }

    @Test
    public void test2() {
        Optional<Employee> op = Data.EMPS.stream()
                .findFirst();
        op.ifPresent(System.out::println);

        System.out.println("--------------------------------");

        Optional<Employee> op2 = Data.EMPS.parallelStream()
                .filter((e) -> e.getStatus().equals(Status.FREE))
                .findAny();
        op2.ifPresent(System.out::println);
    }

    @Test
    public void test3() {
        long count = Data.EMPS.stream()
                .filter((e) -> e.getStatus().equals(Status.FREE))
                .count();
        System.out.println(count);

        Optional<Double> op = Data.EMPS.stream()
                .map(Employee::getSalary)
                .max(Double::compare);
        op.ifPresent(System.out::println);


        Optional<Employee> op2 = Data.EMPS.stream()
                .min(Comparator.comparingDouble(Employee::getSalary));
        op2.ifPresent(System.out::println);
    }

    // 注意：流进行了终止操作后，不能再次使用
    @Test
    public void test4() {
        Stream<Employee> stream = Data.EMPS.stream()
                .filter((e) -> e.getStatus().equals(Status.FREE));
        long count = stream.count(); // 流已经关闭
        System.out.println(count);

        // 若要使用需要新建流
        Data.EMPS.stream()
                .map(Employee::getSalary)
                .max(Double::compare)
                .ifPresent(System.out::println);
    }
}
