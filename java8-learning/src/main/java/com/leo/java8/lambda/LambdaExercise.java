package com.leo.java8.lambda;

import com.leo.java8.other.Data;
import com.leo.java8.other.func.MyFunc;
import com.leo.java8.other.func.StringFunc;
import com.leo.java8.other.domain.Employee;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LambdaExercise {

    @Test
    public void test1() {
        List<Employee> emps = new ArrayList<>(Data.EMPS);

        System.out.println("排序前：");
        emps.forEach(System.out::println);

        System.out.println("-----------------------------------------");

        emps.sort((e1, e2) -> {
            if (e1.getAge() == e2.getAge()) {
                return e1.getName().compareTo(e2.getName());
            } else {
                return -Integer.compare(e1.getAge(), e2.getAge());
            }
        });
        System.out.println("排序后：");
        emps.forEach(System.out::println);
    }

    @Test
    public void test2() {
        String trimStr = strHandler("\t\t\t 疯狂练习一下   ", String::trim);
        assertEquals("疯狂练习一下", trimStr);

        String upper = strHandler("abcdef", String::toUpperCase);
        assertEquals("ABCDEF", upper);

        String newStr = strHandler("疯狂练习一下", (s) -> s.substring(2, 4));
        assertEquals("练习", newStr);
    }
    // 用于处理字符串
    private String strHandler(String str, StringFunc mf) {
        return mf.getValue(str);
    }

    @Test
    public void test3() {
        long res1 = op(100L, 200L, (x, y) -> x + y);
        long res2 = op(200L, 210L, (x, y) -> x * y);
        assertEquals(300, res1);
        assertEquals(42000, res2);
    }

    // 对于两个 Long 型数据进行处理
    private long op(Long l1, Long l2, MyFunc<Long, Long> mf) {
        return mf.getValue(l1, l2);
    }

}
