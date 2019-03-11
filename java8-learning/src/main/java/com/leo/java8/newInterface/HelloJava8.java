package com.leo.java8.newInterface;

/**
 * @author justZero
 * @since 2019/3/9
 */
public interface HelloJava8 {
    default void hello() {
        System.out.println("Hello Java8!");
    }
    static void info() {
        System.out.println("接口中的静态方法！");
    }
}
