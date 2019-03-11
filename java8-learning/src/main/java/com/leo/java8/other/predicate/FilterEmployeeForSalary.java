package com.leo.java8.other.predicate;

import com.leo.java8.other.domain.Employee;

public class FilterEmployeeForSalary
        implements MyPredicate<Employee> {

    @Override
    public boolean test(Employee t) {
        return t.getSalary() >= 5000;
    }

}
