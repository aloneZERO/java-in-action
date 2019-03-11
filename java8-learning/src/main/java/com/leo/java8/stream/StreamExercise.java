package com.leo.java8.stream;

import com.leo.java8.other.Data;
import com.leo.java8.other.domain.Trader;
import com.leo.java8.other.domain.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author justZero
 * @since 2019/3/8
 */
public class StreamExercise {

    /*
     * 1. 给定一个数字列表，如何返回一个由每个数的平方构成的列表呢？
     *    给定【1，2，3，4，5】， 应该返回【1，4，9，16，25】。
     */
    @Test
    public void test1() {
        Integer[] nums = {1, 2, 5, 4, 3};
        List<Integer> list = Arrays.stream(nums)
                .map(x -> x * x)
                .collect(Collectors.toList());
        System.out.println(list);
    }

    // 2. 怎样用 map 和 reduce 方法数一数流中有多少个 Employee 呢？
    @Test
    public void test2() {
        Optional<Integer> countOp = Data.EMPS.stream()
                .map(e -> 1)
                .reduce(Integer::sum);
        countOp.ifPresent(System.out::println);
    }

    private List<Transaction> transactions;

    @Before
    public void before() {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");

        transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );
    }

    // 3. 找出2011年发生的所有交易， 并按交易额排序（从低到高）
    @Test
    public void test3() {
        transactions.stream()
                .filter(t -> t.getYear() == 2011)
                .sorted(Comparator.comparingInt(Transaction::getValue))
                .forEach(System.out::println);
    }

    // 4. 交易员都在哪些不同的城市工作过？
    @Test
    public void test4() {
        transactions.stream()
                .map(t -> t.getTrader().getCity())
                .distinct()
                .forEach(System.out::println);
    }

    // 5. 查找所有来自剑桥的交易员，并按姓名排序
    @Test
    public void test5() {
        transactions.stream()
                .map(Transaction::getTrader)
                .filter(t -> t.getCity().equals("Cambridge"))
                .sorted(Comparator.comparing(Trader::getName))
                .distinct()
                .forEach(System.out::println);
    }

    // 6. 返回所有交易员的姓名字符串，按字母顺序排序
    @Test
    public void test6() {
        transactions.stream()
                .map(t -> t.getTrader().getName())
                .distinct()
                .sorted()
                .forEach(System.out::println);
        System.out.println("----------------------------");

        String names = transactions.stream()
                .map(t -> t.getTrader().getName())
                .distinct()
                .sorted()
                .collect(Collectors.joining(", "));
        System.out.println(names);
        System.out.println("----------------------------");

        // 把名字中的全部字母排序
        transactions.stream()
                .map(t -> t.getTrader().getName())
                .distinct()
                .flatMap(StreamExercise::filterChar)
                .sorted(String::compareToIgnoreCase)
                .forEach(System.out::print);
    }
    private static Stream<String> filterChar(String str) {
        List<String> list = new ArrayList<>();
        for (Character ch: str.toCharArray()) {
            list.add(ch.toString());
        }
        return list.stream();
    }

    // 7. 有没有交易员是在米兰工作的？
    @Test
    public void test7() {
        boolean res = transactions.stream()
                .map(Transaction::getTrader)
                .anyMatch(t -> t.getCity().equals("Milan"));
        System.out.println(res);
    }

    // 8. 打印生活在剑桥的交易员的所有交易额
    @Test
    public void test8() {
        transactions.stream()
                .filter(t -> t.getTrader().getCity().equals("Cambridge"))
                .mapToInt(Transaction::getValue)
                .reduce(Integer::sum)
                .ifPresent(System.out::println);
    }

    // 9. 所有交易中，最高的交易额是多少
    @Test
    public void test9() {
        transactions.stream()
                .mapToInt(Transaction::getValue)
                .max()
                .ifPresent(System.out::println);
    }

    // 10. 找到交易额最小的交易
    @Test
    public void test10() {
        transactions.stream()
                .min(Comparator.comparingInt(Transaction::getValue))
                .ifPresent(System.out::println);
    }

}
