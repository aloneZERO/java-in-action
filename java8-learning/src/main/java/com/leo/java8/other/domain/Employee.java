package com.leo.java8.other.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author justZero
 * @since 2019/3/7
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private long id;
    private String name;
    private int age;
    private double salary;
    private Status status = Status.FREE;

    public Employee(String name) {
        this.name = name;
    }

    public Employee(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public Employee(long id, String name, Integer age, double salary) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public String show() {
        return "测试方法引用！";
    }

    public enum Status {
        FREE, BUSY, VOCATION;
    }
}
