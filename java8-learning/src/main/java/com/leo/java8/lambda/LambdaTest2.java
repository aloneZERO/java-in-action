package com.leo.java8.lambda;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.leo.java8.other.func.IntFunc;
import org.junit.Test;

/**
 * Lambda 表达式就是一种语法糖
 * <pre>
 * 一、Lambda 表达式的基础语法：
 *    Java8 中引入了一个新的操作符 "->" 该操作符称为箭头操作符或 Lambda 操作符
 *
 * 箭头操作符将 Lambda 表达式拆分成两部分：
 *     左侧：Lambda 表达式的参数列表
 *     右侧：Lambda 表达式中所需执行的功能， 即 Lambda 体
 *
 * 语法格式一：无参数，无返回值
 * 		() -> System.out.println("Hello Lambda!");
 *
 * 语法格式二：有一个参数，并且无返回值
 * 		(x) -> System.out.println(x)
 *
 * 语法格式三：若只有一个参数，小括号可以省略不写
 * 		x -> System.out.println(x)
 *
 * 语法格式四：有两个以上的参数，有返回值，并且 Lambda 体中有多条语句
 *		Comparator<Integer> com = (x, y) -> {
 *			System.out.println("函数式接口");
 *			return Integer.compare(x, y);
 *		};
 *
 * 语法格式五：若 Lambda 体中只有一条语句， return 和 大括号都可以省略不写
 * 		Comparator<Integer> com = (x, y) -> Integer.compare(x, y);
 *
 * 语法格式六：Lambda 表达式的参数列表的数据类型可以省略不写，
 *           因为 JVM 编译器通过上下文推断出，数据类型，即“类型推断”
 * 		(Integer x, Integer y) -> Integer.compare(x, y);
 *
 * 二、Lambda 表达式需要“函数式接口”的支持
 * 函数式接口：当接口中只有一个抽象方法的接口，称为函数式接口。
 *           使用注解 @FunctionalInterface 修饰某个接口后，
 *           就可以为该接口提供函数式接口规范检查。
 * </pre>
 *
 * @since 2019-3-8
 */
public class LambdaTest2 {

    @Test
    public void test1() {
        int num = 233; // jdk 1.7 前，必须是 final（现在默认 final）
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello World! " + num);
            }
        };
        r.run();

        System.out.println("-------------------------------");

        Runnable r1 = () -> System.out.println("Hello Lambda!");
        r1.run();
    }

    @Test
    public void test2() {
        Consumer<String> con = x -> System.out.println(x);
        // lambda 即是 Consumer 接口中 accept 方法的实现
        con.accept("I can print!");
    }

    @Test
    public void test3() {
        Comparator<Integer> com = (x, y) -> {
            System.out.println("函数式接口");
            return Integer.compare(x, y);
        };
    }

    @Test
    public void test4() {
        Comparator<Integer> com = (x, y) -> Integer.compare(x, y);
    }

    @Test
    public void test5() {
        // 右边没有指定类型，却可以编译通过，也是因为类型推断的作用
//		String[] strs = {"aaa", "bbb", "ccc"};

        // 下面这种写法就无法自动推断，从而报错
//        String[] strs;
//		strs = {"aaa", "bbb", "ccc"};

        List<String> list = new ArrayList<>();

        // jdk1.8 的自动推断升级，下面代码在1.7中编译通不过
        show(new HashMap<>());
    }
    private void show(Map<String, Integer> map) {}

    // 对一个数进行运算
    @Test
    public void test6() {
        Integer num = operation(100, (x) -> x * x);
        System.out.println(num);

        System.out.println(operation(200, (y) -> y + 200));
    }

    private Integer operation(Integer num, IntFunc mf) {
        return mf.getValue(num);
    }

}
