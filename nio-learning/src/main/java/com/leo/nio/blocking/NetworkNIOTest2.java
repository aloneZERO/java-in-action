package com.leo.nio.blocking;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author justZero
 * @since 2019/3/13
 */
public class NetworkNIOTest2 {

    private static String rootPath;
    private static String imageName;

    @BeforeClass
    public static void setup() {
        rootPath = NetworkNIOTest.class.getResource("/files/")
                .getPath()
                .substring(1);
        imageName = rootPath + "1.jpg";
    }

    // 客户端
    @Test
    public void client() throws IOException {
        SocketChannel sChannel = SocketChannel.open(
                new InetSocketAddress("127.0.0.1", 2333));

        FileChannel inChannel = FileChannel.open(
                Paths.get(imageName), StandardOpenOption.READ);

        ByteBuffer buf = ByteBuffer.allocate(1024);

        while (inChannel.read(buf) != -1) {
            buf.flip();
            sChannel.write(buf);
            buf.clear();
        }
        sChannel.shutdownOutput(); // 通知服务端，数据已发送完毕

        // 接收服务端的反馈
        int len;
        // 当服务端那边 close 后，read() 才返回 -1
        while ((len = sChannel.read(buf)) != -1) {
            buf.flip();
            System.out.println(new String(buf.array(), 0, len));
            buf.clear();
        }

        inChannel.close();
        sChannel.close();
    }

    // 服务端
    @Test
    public void server() throws IOException {
        String receiveImage = rootPath + System.currentTimeMillis() + ".jpg";

        FileChannel outChannel = FileChannel.open(
                Paths.get(receiveImage), StandardOpenOption.WRITE,
                StandardOpenOption.CREATE);

        SocketChannel sChannel = ServerSocketChannel.open()
                .bind(new InetSocketAddress(2333))
                .accept();

        System.out.println("客户端已连接......");
        ByteBuffer buf = ByteBuffer.allocate(1024);

        // 当客户端那边通道 shutdownOutput 或 close 后，read 才返回 -1
        while (sChannel.read(buf) != -1) {
            buf.flip();
            outChannel.write(buf);
            buf.clear();
        }

        // 发送反馈给客户端
        buf.put("服务端接收数据成功~".getBytes());
        buf.flip();
        sChannel.write(buf);

        sChannel.close();
        outChannel.close();
    }
}
