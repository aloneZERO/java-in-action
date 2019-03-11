package com.leo.java8.other.func;

@FunctionalInterface
public interface MyFunc<T, R> {
    R getValue(T t1, T t2);
}
