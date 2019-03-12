package com.leo.juc.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * CopyOnWriteArrayList/CopyOnWriteArraySet : “写入并复制”
 * <p>
 * 注意：添加操作多时，效率低，因为每次添加时都会进行复制出一个新实例，
 * 开销非常的大。并发迭代操作多时可以选择。
 *
 * @author justZero
 * @since 2019/3/11
 */
public class CopyOnWriteArrayListTest {

    public static void main(String[] args) {
        ExceptionTask task = new ExceptionTask();
        OkTask okTask = new OkTask();

        for (int i = 0; i < 10; i++) {
//            new Thread(task).start();
            new Thread(okTask).start();
        }
    }
}

class OkTask implements Runnable {
    private static CopyOnWriteArrayList<String> list =
            new CopyOnWriteArrayList<>();

    static {
        list.add("AAA");
        list.add("BBB");
        list.add("CCC");
    }

    @Override
    public void run() {
        for (String s : list) {
            System.out.println(Thread.currentThread()
                    .getName() + " : " + s);
            list.add("DDD");
        }
    }
}

class ExceptionTask implements Runnable {
    private static List<String> list = Collections
            .synchronizedList(new ArrayList<>());

    static {
        list.add("AAA");
        list.add("BBB");
        list.add("CCC");
    }

    @Override
    public void run() {
        // ConcurrentModificationException
        for (String s : list) {
            System.out.println(Thread.currentThread()
                    .getName() + " : " + s);
            list.add("DDD");
        }
    }
}
