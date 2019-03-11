package com.leo.java8.test;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author justZero
 * @since 2019/3/7
 */
public class OtherTest {

    @Test
    public void sortlambda() {
        Integer[] data = {1,3,4,-1,10,9};
        System.out.println(Arrays.toString(data));
        Arrays.sort(data, (o1, o2) -> o2 - o1);
        System.out.println(Arrays.toString(data));
    }

    @Test
    public void testInteger() {
        // 整型常量池：-128~127
        Integer b = 128;
        Integer a = Integer.valueOf("128");
        Integer x = -128;
        Integer y = Integer.valueOf("-128");

        assertNotSame(a, b);
        assertSame(x, y);
        assertEquals(a, b);
        assertEquals(x, y);

        // 加法运算时的自动拆箱
        assert new Integer(129)==new Integer(128)
                + new Integer(1);
    }

    @Test
    public void testSomething() {
        String s = "hello";
        char[] chs = {'h', 'e', 'l', 'l', 'o'};
        assertNotEquals(s, chs);
    }

}
