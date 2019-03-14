package com.leo.nio.nonblocking;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 先启动服务端，再启动客户端
 *
 * @author justZero
 * @since 2019/3/14
 */
public class ServerClientTest {

    public static void main(String[] args) throws IOException {
        client();
    }

    // 客户端
    // IDEA Junit 控制台无法键盘输入，所以改为用 main 方法调用
    private static void client() throws IOException {
        // 1. 获取通道
        SocketChannel sChannel = SocketChannel.open(
                new InetSocketAddress("127.0.0.1", 2333));

        // 2. 切换非阻塞模式
        sChannel.configureBlocking(false);

        // 3. 分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        // 4. 发送数据给服务端
        Scanner scan = new Scanner(System.in);
        while (scan.hasNext()) {
            String message = scan.nextLine();
            buf.put(message.getBytes());
            buf.flip();
            sChannel.write(buf);
            buf.clear();
            // "#exit" 结束指令
            if ("#exit".equals(message)) break;
        }

        // 5. 关闭通道
        sChannel.close();
    }

    // 服务端
    @Test
    public void server() throws IOException {
        // 1. 获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();

        // 2. 切换非阻塞模式
        ssChannel.configureBlocking(false);

        // 3. 绑定连接
        ssChannel.bind(new InetSocketAddress(2333));

        // 4. 获取选择器
        Selector selector = Selector.open();

        // 5. 将通道注册到选择器上，并且指定“监听接收事件”
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 6. 轮询式的获取选择器上已经“准备就绪”的事件
        // 当注册的通道有需要处理的 IO 操作时，select() 返回
        // 并将对应得的 SelectionKey 加入被选择的 SelectionKey 集合中
        // 返回注册通道的数量
        while (selector.select() > 0) {
            // 7. 获取当前选择器中所有注册的“选择键(已就绪的监听事件)”
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

            while (it.hasNext()) {
                // 8. 获取准备“就绪”的事件
                SelectionKey sk = it.next();

                // 9. 判断具体是什么事件准备就绪
                if (sk.isAcceptable()) {
                    // 10. 若“接收就绪”，获取客户端连接
                    SocketChannel sChannel = ssChannel.accept();

                    // 11. 切换非阻塞模式
                    sChannel.configureBlocking(false);

                    // 12. 将该通道注册到选择器上
                    sChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("接收就绪...");
                } else if (sk.isReadable()) {
                    // 13. 获取当前选择器上“读就绪”状态的通道
                    SocketChannel sChannel = (SocketChannel) sk.channel();

                    // 14. 读取数据
                    ByteBuffer buf = ByteBuffer.allocate(1024);

                    int len;
                    while ((len = sChannel.read(buf)) > 0) {
                        buf.flip();
                        String message = new String(buf.array(), 0, len);
                        System.out.println(nowDateTime() + ": " + message);
                        buf.clear();

                        // "#exit" 结束指令
                        if ("#exit".equals(message.trim())) {
                            System.out.println("再见~");
                            return;
                        }
                    }
                }

                // 15. 取消选择键 SelectionKey
                it.remove();
            }
        }
    }

    private static String nowDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.now().format(dtf);
    }
}
