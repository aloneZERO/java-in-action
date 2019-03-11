package com.leo.java8.lambda;

import com.leo.java8.other.Data;
import com.leo.java8.other.domain.Employee;
import com.leo.java8.other.predicate.FilterEmployeeForAge;
import com.leo.java8.other.predicate.FilterEmployeeForSalary;
import com.leo.java8.other.predicate.MyPredicate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author justZero
 * @since 2019/3/7
 */
public class LambdaTest1 {

    // 获取当前公司中员工年龄小于35岁的员工（不使用新特性）
    private List<Employee> filterEmployeeAge(List<Employee> emps) {
        List<Employee> list = new ArrayList<>();
        for (Employee emp : emps) {
            if (emp.getAge() <= 35) {
                list.add(emp);
            }
        }
        return list;
    }

    // 获取公司中工资大于 5000 的员工信息
    // 需求虽然不同，但是冗余代码过多，需要优化！
    private List<Employee> filterEmployeeSalary(List<Employee> emps) {
        List<Employee> list = new ArrayList<>();
        for (Employee emp : emps) {
            if (emp.getSalary() >= 5000) {
                list.add(emp);
            }
        }
        return list;
    }

    @Test
    public void test1() {
        for (Employee employee : filterEmployeeAge(Data.EMPS)) {
            System.out.println(employee);
        }
    }

    // 优化方式一：策略设计模式
    private List<Employee> filterEmployee(
            List<Employee> emps,
            MyPredicate<Employee> mp) {

        List<Employee> list = new ArrayList<>();
        for (Employee emp: emps) {
            if (mp.test(emp)) list.add(emp);
        }
        return list;
    }
    @Test
    public void test2() {
        for (Employee e: filterEmployee(Data.EMPS,
                new FilterEmployeeForAge())) {
            System.out.println(e);
        }
        System.out.println("---------------------------------------");
        for (Employee e: filterEmployee(Data.EMPS,
                new FilterEmployeeForSalary())) {
            System.out.println(e);
        }
    }

    // 优化方式二：匿名内部类
    @Test
    public void test3() {
        List<Employee> list = filterEmployee(Data.EMPS, new MyPredicate<Employee>() {
            @Override
            public boolean test(Employee employee) {
                return employee.getSalary() <= 5000;
            }
        });
        for (Employee e: list) {
            System.out.println(e);
        }
    }

    // 优化方式三：Lambda 表达式
    @Test
    public void test4() {
        List<Employee> list = filterEmployee(Data.EMPS,
                e -> e.getSalary()<=5000);
        for (Employee e: list) {
            System.out.println(e);
        }
    }

    // 优化方式四：Stream API
    @Test
    public void test7() {
        // 获取所有年龄小于35的员工
        Data.EMPS.stream()
                .filter( e -> e.getAge() <= 35 )
                .forEach(System.out::println);

        System.out.println("-------------------------" +
                "------------------------------------" +
                "-------");

        // 获取年龄最小的两个员工
        Data.EMPS.stream()
                .sorted(Comparator.comparingInt(Employee::getAge))
                .limit(2)
                .forEach(System.out::println);

        System.out.println("-------------------------" +
                "------------------------------------" +
                "-------");

        // 获取员工的姓名集合
        Data.EMPS.stream()
                .map(Employee::getName)
                .forEach(System.out::println);
    }

}
