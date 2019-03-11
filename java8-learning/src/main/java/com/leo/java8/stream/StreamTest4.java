package com.leo.java8.stream;

import com.leo.java8.other.Data;
import com.leo.java8.other.domain.Employee;
import com.leo.java8.other.domain.Employee.Status;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;


public class StreamTest4 {

    //3. 终止操作
    /*
     * 归约：可以将流中元素反复结合起来，得到一个值
     *   reduce(T identity, BinaryOperator)
     *   reduce(BinaryOperator)
     */
    @Test
    public void test1() {
        int sum = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .reduce(0, (x, y) -> x + y); // 起始值设0
        assertEquals(55, sum);

        Data.EMPS.stream()
                .map(Employee::getSalary)
                .reduce(Double::sum)
                .ifPresent(System.out::println);
    }

    // 搜索名字中 “六” 出现的次数
    @Test
    public void test2() {
        Data.EMPS.stream()
                .map(Employee::getName)
                .flatMap(StreamTest2::filterCharacter)
                .map((ch) -> {
                    if (ch.equals('六')) return 1;
                    else return 0;
                }).reduce(Integer::sum)
                .ifPresent(System.out::println);
    }

    // collect——将流转换为其他形式。接收一个 Collector 接口的实现，用于给 Stream 中元素做汇总的方法
    @Test
    public void test3() {
        List<String> list = Data.EMPS.stream()
                .map(Employee::getName)
                .collect(Collectors.toList());
        list.forEach(System.out::println);

        System.out.println("----------------------------------");

        Set<String> set = Data.EMPS.stream()
                .map(Employee::getName)
                .collect(Collectors.toSet());
        set.forEach(System.out::println);

        System.out.println("----------------------------------");

        HashSet<String> hs = Data.EMPS.stream()
                .map(Employee::getName)
                .collect(Collectors.toCollection(HashSet::new));
        hs.forEach(System.out::println);
    }

    // collect 配合 Collectors，可由一些更简洁的操作代替
    @Test
    public void test4() {
        System.out.print("最高薪资：");
        Data.EMPS.stream()
                .map(Employee::getSalary)
                .max(Double::compare)
                .ifPresent(System.out::println);

        System.out.print("最低薪资：");
        Data.EMPS.stream()
                .min(Comparator.comparingDouble(Employee::getSalary))
                .ifPresent(e -> System.out.println(e.getSalary()));

        double sum = Data.EMPS.stream()
                .mapToDouble(Employee::getSalary)
                .sum();
        System.out.println("薪资总和：" + sum);

        Double avg = Data.EMPS.stream()
                .collect(Collectors.averagingDouble(Employee::getSalary));
        System.out.println("平均薪资：" + avg);

        long count = Data.EMPS.stream()
                .distinct()
                .count();
        System.out.println("员工数：" + count);

        // 获取员工薪资的全部统计信息：最大值、最低值、平均值等
        DoubleSummaryStatistics dss = Data.EMPS.stream()
                .collect(Collectors.summarizingDouble(Employee::getSalary));
        System.out.println(dss);
    }

    // 分组
    @Test
    public void test5() {
        Map<Status, List<Employee>> map = Data.EMPS.stream()
                .collect(Collectors.groupingBy(Employee::getStatus));

        System.out.println(Status.FREE);
        map.get(Status.FREE).forEach(System.out::println);
        System.out.println("-----------------------------------------------");

        System.out.println(Status.BUSY);
        map.get(Status.BUSY).forEach(System.out::println);
        System.out.println("-----------------------------------------------");

        System.out.println(Status.VOCATION);
        map.get(Status.VOCATION).forEach(System.out::println);
    }

    // 多级分组
    @Test
    public void test6() {
        Map<Status, Map<String, List<Employee>>> map = Data.EMPS.stream()
                .collect(Collectors.groupingBy(Employee::getStatus, Collectors.groupingBy((e) -> {
                    if (e.getAge() >= 60)
                        return "老年";
                    else if (e.getAge() >= 35)
                        return "中年";
                    else
                        return "青年";
                })));

        Map<String, List<Employee>> freeMap = map.get(Status.FREE);
        System.out.println("青年");
        Optional.ofNullable(freeMap.get("青年"))
                .ifPresent(x -> x.forEach(System.out::println));
        System.out.println("-----------------------------------------------");

        System.out.println("中年");
        Optional.ofNullable(freeMap.get("中年"))
                .ifPresent(x -> x.forEach(System.out::println));
        System.out.println("-----------------------------------------------");

        System.out.println("老年");
        Optional.ofNullable(freeMap.get("老年"))
                .ifPresent(x -> x.forEach(System.out::println));
    }

    // 分区
    @Test
    public void test7() {
        Map<Boolean, List<Employee>> map = Data.EMPS.stream()
                .collect(Collectors.partitioningBy((e) -> e.getSalary() >= 5000));
        System.out.println("薪资大于5000的员工：");
        map.get(true).forEach(System.out::println);
        System.out.println("薪资低于5000的员工：");
        map.get(false).forEach(System.out::println);
    }

    @Test
    public void test8() {
        String str = Data.EMPS.stream()
                .map(Employee::getName)
                .distinct()
                .collect(Collectors.joining(",", "----", "----"));

        System.out.println(str);
    }
}
