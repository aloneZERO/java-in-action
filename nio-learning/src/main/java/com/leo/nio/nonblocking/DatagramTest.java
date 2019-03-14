package com.leo.nio.nonblocking;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 先运行接收方法，再运行发送方法
 *
 * @author justZero
 * @since 2019/3/14
 */
public class DatagramTest {

    public static void main(String[] args) throws IOException {
        send();
    }

    // IDEA Junit 控制台无法键盘输入，所以改为用 main 方法调用
    private static void send() throws IOException {
        DatagramChannel dc = DatagramChannel.open();

        dc.configureBlocking(false);

        ByteBuffer buf = ByteBuffer.allocate(1024);

        Scanner scan = new Scanner(System.in);

        InetSocketAddress dest = new InetSocketAddress("127.0.0.1", 2333);
        while (scan.hasNext()) {
            String message = scan.nextLine().trim();
            buf.put(message.getBytes());
            buf.flip();
            dc.send(buf, dest);
            buf.clear();

            if ("#exit".equals(message)) {
                break;
            }
        }
        dc.close();
    }

    @Test
    public void receive() throws IOException {
        DatagramChannel dc = DatagramChannel.open()
                .bind(new InetSocketAddress(2333));

        dc.configureBlocking(false);

        Selector selector = Selector.open();

        dc.register(selector, SelectionKey.OP_READ);

        while (selector.select() > 0) {
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

            while (it.hasNext()) {
                SelectionKey sk = it.next();

                if (sk.isReadable()) {
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    dc.receive(buf);
                    buf.flip();
                    String message = new String(buf.array(), 0, buf.limit());
                    System.out.println(nowDateTime() + ": " + message);
                    buf.clear();

                    if ("#exit".equals(message)) {
                        dc.close();
                        return;
                    }
                }
            }

            it.remove();
        }
    }

    private static String nowDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.now().format(dtf);
    }

}
