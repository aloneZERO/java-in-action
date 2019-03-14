package com.leo.nio.blocking;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static org.junit.Assert.assertTrue;

/**
 * <pre>
 * 使用 NIO 完成网络通信的三个核心：
 *
 * 1. 通道（Channel）：负责连接
 *
 * 	   java.nio.channels.Channel 接口：
 * 			|--SelectableChannel
 * 				|--SocketChannel
 * 				|--ServerSocketChannel
 * 				|--DatagramChannel
 *
 * 				|--Pipe.SinkChannel
 * 				|--Pipe.SourceChannel
 *
 * 2. 缓冲区（Buffer）：负责数据的存取
 *
 * 3. 选择器（Selector）：是 SelectableChannel 的多路复用器。用于监控 SelectableChannel 的 IO 状况
 * </pre>
 */
public class NetworkNIOTest {

    private static String rootPath;
    private static String imageName;

    @BeforeClass
    public static void setup() {
        rootPath = NetworkNIOTest.class.getResource("/files/")
                .getPath()
                .substring(1); // 去掉第一个字符"/"，否则可能出错
        imageName = rootPath + "1.jpg";
    }

    // 客户端
    @Test
    public void client() {
        // 1. 获取通道
        try (SocketChannel sChannel = SocketChannel.open(
                new InetSocketAddress("127.0.0.1", 2333));
             FileChannel inChannel = FileChannel.open(
                     Paths.get(imageName), StandardOpenOption.READ)) {

            // 2. 分配指定大小的缓冲区。这里省略，使用更简洁的方式

            // 3. 读取本地文件，并发送到服务端
            inChannel.transferTo(0, inChannel.size(), sChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 4. 关闭通道。try-with-resources 自动关闭资源
    }

    // 服务端
    @Test
    public void server() {
        String receiveImage = rootPath + System.currentTimeMillis() + ".jpg";

        // 1. 获取通道
        try (SocketChannel sChannel = ServerSocketChannel.open() // 开启服务端通道
                .bind(new InetSocketAddress(2333)) // 绑定连接
                .accept(); // 获取客户端连接的通道
             FileChannel outChannel = FileChannel.open(
                     Paths.get(receiveImage), StandardOpenOption.WRITE,
                     StandardOpenOption.CREATE)) {

            // 2. 分配指定大小的缓冲区
            ByteBuffer buf = ByteBuffer.allocate(1024);

            // 3. 接收客户端的数据，并保存到本地
            while (sChannel.read(buf) != -1) {
                buf.flip();
                outChannel.write(buf);
                buf.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 4. 关闭通道。try-with-resources 自动关闭资源

        System.out.println("---------- 已收到图片文件 ----------");
        // 阻塞，直到有客户端连接才继续执行
        File receiveFile = new File(receiveImage);
        assertTrue(receiveFile.exists());
        assertTrue(receiveFile.delete());
    }
}
