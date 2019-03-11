package com.leo.java8.newInterface;

import org.junit.Test;

/**
 * 当继承父类和实现接口中的默认方法冲突时，类优先（即使用父类的该方法）。
 * 当实现多个接口中的默认方法冲突时，必须重写该方法。
 *
 * @author justZero
 * @since 2019/3/9
 */
public class InterfaceTest {

    @Test
    public void test1() {
        SubHelloClass helloClass = new SubHelloClass();
        helloClass.hello(); // "Hello Class!"

        HelloJava8.info(); // "接口中的静态方法！"
    }

}
