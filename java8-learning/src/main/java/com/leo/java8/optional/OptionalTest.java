package com.leo.java8.optional;

import com.leo.java8.other.domain.Employee;
import com.leo.java8.other.domain.Goddess;
import com.leo.java8.other.domain.Man;
import com.leo.java8.other.domain.NewMan;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * 一、Optional 容器类：用于尽量避免空指针异常
 * <pre>
 * Optional.of(T t)：创建一个 Optional 实例
 * Optional.empty()：创建一个空的 Optional 实例
 * Optional.ofNullable(T t)：若 t 不为 null，创建 Optional 实例，否则创建空实例
 * isPresent()：判断是否包含值
 * orElse(T t)：如果调用对象包含值，返回该值，否则返回 t
 * orElseGet(Supplier s)：如果调用对象包含值，返回该值，否则返回 s 获取的值
 * map(Function f)：如果有值对其处理，并返回处理后的 Optional，否则返回 Optional.empty()
 * flatMap(Function mapper)：与 map 类似，要求返回值必须是 Optional
 * </pre>
 * 注意：Optional 无法序列化。
 *
 * @since 2019-3-9
 */
public class OptionalTest {

    @Test
    public void test1() {
        Optional<Employee> op = Optional.of(new Employee());
        Employee emp = op.get();
        System.out.println(emp);
    }

    @Test(expected = NullPointerException.class)
    public void test2() {
		Optional<Employee> op = Optional.of(null); // NullPointerException
		System.out.println(op.get());


    }
    @Test(expected = NoSuchElementException.class)
    public void test3() {
        Optional<Employee> op = Optional.empty();
        System.out.println(op.get()); // NoSuchElementException
    }

    @Test
    public void test4() {
        Optional<Employee> op = Optional.of(new Employee());
        op.ifPresent(System.out::println);

        Employee emp = op.orElse(new Employee("张三"));
        assertNull(emp.getName());

        op = Optional.empty();
        Employee emp2 = op.orElseGet(() -> new Employee("李四"));
        assertEquals("李四", emp2.getName());
    }

    @Test
    public void test5() {
        Optional<Employee> op = Optional.of(
                new Employee(101, "张三", 18, 9999.99));

        Optional<String> op2 = op.map(Employee::getName);
        op2.ifPresent(System.out::println);

        Optional<String> op3 = op.flatMap((e) -> Optional.of(e.getName()));
        System.out.println(op3.get());
    }

    // 没有 Optional 的时候
    @Test
    public void test6() {
        Man man = new Man();
        String name = getGoddessName(man);
        assertEquals("IU", name);
    }
    private String getGoddessName(Man man) {
        if (man != null) {
            Goddess g = man.getGoddess();
            if (g != null) {
                return g.getName();
            }
        }
        return "IU";
    }

    // 运用 Optional 的实体类
    @Test
    public void test7() {
        Goddess goddess = new Goddess("泫雅");
        NewMan man = new NewMan(goddess);

        String name = getGoddessName2(man);
        assertEquals("泫雅", name);
    }
    private String getGoddessName2(NewMan man) {
        return Optional.ofNullable(man)
                .orElse(new NewMan())
                .getGoddess()
                .orElse(new Goddess("IU"))
                .getName();
    }
}
