package com.leo.java8.annotation;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 重复注解和类型注解
 *
 * @author justZero
 * @since 2019/3/9
 */
public class AnnotationTest {

    @Test
    public void test1() throws Exception {
        Class<AnnotationTest> clazz = AnnotationTest.class;
        Method m1 = clazz.getDeclaredMethod("show", String.class);

        for (Annotation anno : m1.getParameterAnnotations()[0]) {
            MyAnnotation ma = (MyAnnotation) anno;
            System.out.println(ma.value());
        }

        MyAnnotation[] mas = m1.getAnnotationsByType(MyAnnotation.class);
        for (MyAnnotation myAnnotation : mas) {
            System.out.println(myAnnotation.value());
        }
    }

    @MyAnnotation("Hello")
    @MyAnnotation("World")
    private void show(@MyAnnotation("abc") String str) {
    }
}
