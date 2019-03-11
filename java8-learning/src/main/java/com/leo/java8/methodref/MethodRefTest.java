package com.leo.java8.methodref;

import com.leo.java8.other.domain.Employee;
import org.junit.Test;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.function.*;

import static org.junit.Assert.*;

/**
 * <pre>
 * 一、方法引用：
 *   若 Lambda 体中的功能，已经有方法提供了实现，可以使用方法引用
 *   （可以将方法引用理解为 Lambda 表达式的另外一种表现形式）
 *
 * 1. 对象的引用 :: 实例方法名
 * 2. 类名 :: 静态方法名
 * 3. 类名 :: 实例方法名
 *
 * 注意：
 *   1. 方法引用所引用的方法的参数列表与返回值类型，需要与函数式接口中抽象方法的参数列表和返回值类型保持一致！
 *   2. 若Lambda 的参数列表的第一个参数，是实例方法的调用者，第二个参数(或无参)是实例方法的参数时，
 *      格式： ClassName::MethodName
 *
 * 二、构造器引用：
 *   构造器的参数列表，需要与函数式接口中参数列表保持一致！
 *
 * 1. 类名 :: new
 *
 * 三、数组引用
 *
 * 类型[] :: new;
 * </pre>
 *
 * @since 2019-3-8
 */
public class MethodRefTest {

    @Test
    public void test1() {
        PrintStream ps = System.out;
        Consumer<String> con = (str) -> ps.println(str);
        con.accept("Hello World!");

        Consumer<String> con2 = ps::println;
        con2.accept("Hello Java8！");

        Consumer<String> con3 = System.out::println;
        con3.accept("Hello Method reference!");
    }

    // 对象的引用 :: 实例方法名
    @Test
    public void test2() {
        Employee emp = new Employee(101, "张三", 18, 9999.99);

        Supplier<String> sup = () -> emp.getName();
        assertEquals("张三", sup.get());

        Supplier<String> sup2 = emp::getName;
        assertEquals("张三", sup2.get());
    }

    @Test
    public void test3() {
        BiFunction<Double, Double, Double> fun = (x, y) -> Math.max(x, y);
        assert fun.apply(1.5, 22.2).equals(22.2);

        BiFunction<Double, Double, Double> fun2 = Math::max;
        assert fun2.apply(1.2, 1.5).equals(1.5);
    }

    // 类名 :: 静态方法名
    @Test
    public void test4() {
        Comparator<Integer> com = (x, y) -> Integer.compare(x, y);
        assertEquals(-1, com.compare(1, 2));

        Comparator<Integer> com2 = Integer::compare;
        assertEquals(0, com2.compare(3, 3));
    }

    // 类名 :: 实例方法名
    @Test
    public void test5() {
        BiPredicate<String, String> bp = (x, y) -> x.equals(y);
        assertTrue(bp.test("abcde", "abcde"));

        BiPredicate<String, String> bp2 = String::equals;
        assertFalse(bp2.test("abc", "abd"));

        Function<Employee, String> fun = (e) -> e.show();
        System.out.println(fun.apply(new Employee()));

        System.out.println("-----------------------------------------");

        Function<Employee, String> fun2 = Employee::show;
        System.out.println(fun2.apply(new Employee()));

    }

    @Test
    public void test6() {
        Supplier<Employee> sup = () -> new Employee();
        System.out.println(sup.get());

        System.out.println("------------------------------------");

        Supplier<Employee> sup2 = Employee::new;
        System.out.println(sup2.get());
    }

    // 构造器引用
    @Test
    public void test7() {
        Function<String, Employee> fun = Employee::new;
        System.out.println(fun.apply("Leo"));

        BiFunction<String, Integer, Employee> fun2 = Employee::new;
        System.out.println(fun2.apply("Jack", 23));
    }

    // 数组引用
    @Test
    public void test8() {
        Function<Integer, String[]> fun = (len) -> new String[len];
        String[] strs = fun.apply(10);
        assertEquals(10, strs.length);

        Function<Integer, Employee[]> fun2 = Employee[]::new;
        Employee[] emps = fun2.apply(20);
        assertEquals(20, emps.length);
    }

}
