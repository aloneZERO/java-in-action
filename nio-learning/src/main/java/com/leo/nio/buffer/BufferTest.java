package com.leo.nio.buffer;

import org.junit.Test;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * <pre>
 * 一、缓冲区（Buffer）：
 * 在 Java NIO 中负责数据的存取。缓冲区就是数组。用于存储不同数据类型的数据
 *
 * 根据数据类型不同（boolean 除外），提供了相应类型的缓冲区：
 * ByteBuffer
 * CharBuffer
 * ShortBuffer
 * IntBuffer
 * LongBuffer
 * FloatBuffer
 * DoubleBuffer
 *
 * 上述缓冲区的管理方式几乎一致，通过 allocate() 获取缓冲区
 *
 * 二、缓冲区存取数据的两个核心方法：
 * put() : 存入数据到缓冲区中
 * get() : 获取缓冲区中的数据
 *
 * 三、缓冲区中的四个核心属性：
 * capacity : 容量，表示缓冲区中最大存储数据的容量。一旦声明不能改变。
 * limit : 界限，表示缓冲区中可以操作数据的大小。（limit 后数据不能进行读写）
 * position : 位置，表示缓冲区中正在操作数据的位置。
 * mark : 标记，表示记录当前 position 的位置。可以通过 reset() 恢复到 mark 的位置
 *
 * 0 <= mark <= position <= limit <= capacity
 *
 * 四、直接缓冲区与非直接缓冲区：
 * 非直接缓冲区：通过 allocate() 方法分配缓冲区，将缓冲区建立在 JVM 的内存中
 * 直接缓冲区：通过 allocateDirect() 方法分配直接缓冲区，将缓冲区建立在物理内存中。可以提高效率
 * </pre>
 */
public class BufferTest {

    @Test
    public void test1() {
        // 1. 分配一个指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        assertEquals(0, buf.position());
        assertEquals(1024, buf.limit());
        assertEquals(1024, buf.capacity());

        // 2. 利用 put() 存数据到缓冲区中
        String data = "1234567890";
        buf.put(data.getBytes());
        assertEquals(10, buf.position());
        assertEquals(1024, buf.limit());
        assertEquals(1024, buf.capacity());

        // 3. 切换为读数据模式
        buf.flip(); // 将 limit=position; position=0; mark=-1
        assertEquals(0, buf.position());
        assertEquals(10, buf.limit());
        assertEquals(1024, buf.capacity());

        // 4. 利用 get() 读取缓冲区中的数据
        byte[] bytes = new byte[buf.limit()];
        buf.get(bytes);
        assertEquals("1234567890", new String(bytes, 0, bytes.length));
        assertEquals(10, buf.position());
        assertEquals(10, buf.limit());
        assertEquals(1024, buf.capacity());

        // 5. rewind() 可重复读数据
        buf.rewind();
        assertEquals(0, buf.position());
        assertEquals(10, buf.limit());
        assertEquals(1024, buf.capacity());

        // 6. clear() 清空缓冲区
        // 但是缓冲区中的数据依然存在，只是处于“被遗忘”状态，数据界限信息已被初始化
        buf.clear();
        assertEquals(0, buf.position());
        assertEquals(1024, buf.limit());
        assertEquals(1024, buf.capacity());
        assertEquals('1', (char) buf.get());
    }

    @Test
    public void test2() {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        String data = "12345";
        buf.put(data.getBytes());

        buf.flip();
        byte[] dest = new byte[buf.limit()];
        buf.get(dest, 0, 2);
        assertEquals("12", new String(dest, 0, 2));

        // mark() 标记位置
        buf.mark();

        buf.get(dest, 2, 2);
        assertEquals("34", new String(dest, 2, 2));
        assertEquals(4, buf.position());

        // reset() 恢复到 mark 的位置
        buf.reset();
        assertEquals(2, buf.position());

        // 判断缓冲区中是否还有剩余数据
        assertTrue(buf.hasRemaining());
        // 获取缓冲区中可以操作的数量
        assertEquals(3, buf.remaining());
    }

    @Test
    public void test3() {
        // 分配直接缓冲区：直接在物理内存中开辟缓冲区
        // 这种方式：分配和销毁的成本高，管理困难，不方便回收
        // 最好是该方式能明显提高性能时，才使用该方式。
        ByteBuffer buf = ByteBuffer.allocateDirect(1024);
        assertTrue(buf.isDirect());
    }

}
