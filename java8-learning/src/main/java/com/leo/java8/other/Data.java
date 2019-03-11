package com.leo.java8.other;

import com.leo.java8.other.domain.Employee;

import java.util.Arrays;
import java.util.List;

/**
 * @author justZero
 * @since 2019/3/7
 */
public interface Data {
    List<Employee> EMPS = Arrays.asList(
            new Employee(101, "张三", 18, 9999.99, Employee.Status.VOCATION),
            new Employee(102, "李四", 59, 6666.66, Employee.Status.BUSY),
            new Employee(103, "王五", 28, 3333.33),
            new Employee(104, "赵六", 8, 7777.77, Employee.Status.VOCATION),
            new Employee(104, "赵六", 8, 7777.77, Employee.Status.VOCATION),
            new Employee(105, "田七", 38, 2222.22),
            new Employee(105, "周八", 23, 5555.55, Employee.Status.BUSY)
    );
}
