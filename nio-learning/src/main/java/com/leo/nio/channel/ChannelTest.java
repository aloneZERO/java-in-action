package com.leo.nio.channel;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static org.junit.Assert.assertTrue;

/**
 * <pre>
 * 一、通道（Channel）：
 * 用于源节点与目标节点的连接。在 Java NIO 中负责缓冲区中数据的传输。
 * Channel 本身不存储数据，因此需要配合缓冲区进行传输。
 *
 * 二、通道的主要实现类
 * 	java.nio.channels.Channel 接口：
 * 		|--FileChannel
 * 		|--SocketChannel
 * 		|--ServerSocketChannel
 * 		|--DatagramChannel
 *
 * 三、获取通道
 * 1. Java 针对支持通道的类提供了 getChannel() 方法
 * 		本地 IO：
 * 		FileInputStream/FileOutputStream
 * 		RandomAccessFile
 *
 * 		网络IO：
 * 		Socket
 * 		ServerSocket
 * 		DatagramSocket
 *
 * 2. 在 JDK 1.7 中的 NIO.2（升级版） 针对各个通道提供了静态方法 open()
 * 3. 在 JDK 1.7 中的 NIO.2 的 Files 工具类的 newByteChannel()
 *
 * 四、通道之间的数据传输
 * transferFrom()
 * transferTo()
 *
 * 五、分散(Scatter)与聚集(Gather)
 * 分散读取（Scattering Reads）：将通道中的数据分散到多个缓冲区中
 * 聚集写入（Gathering Writes）：将多个缓冲区中的数据聚集到通道中
 *
 * 六、字符集：Charset
 * 编码：字符串 -> 字节数组
 * 解码：字节数组 -> 字符串
 * </pre>
 */
public class ChannelTest {

    private static String rootPath;
    private static String imageName;

    @BeforeClass
    public static void setup() {
        rootPath = ChannelTest.class.getResource("/files/")
                .getPath()
                .substring(1); // 去掉第一个字符"/"，否则可能出错
        imageName = rootPath + "1.jpg";
    }

    // 利用通道完成文件的复制（非直接缓冲区）
    @Test
    public void test1() {
        String newImageName = rootPath + "1_" + System.currentTimeMillis() + ".jpg";

        try (FileInputStream fis = new FileInputStream(imageName);
             FileOutputStream fos = new FileOutputStream(newImageName);
             // 1. 获取通道
             FileChannel inChannel = fis.getChannel();
             FileChannel outChannel = fos.getChannel()) {

            // 2. 分配指定大小的缓冲区
            ByteBuffer buf = ByteBuffer.allocate(1024);

            // 3. 将通道中的数据存入缓冲区中
            while (inChannel.read(buf) != -1) {
                buf.flip();
                // 4. 将缓冲区中的数据写入通道中
                outChannel.write(buf);
                buf.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        File newImage = new File(newImageName);
        assertTrue(newImage.exists());
        assertTrue(newImage.delete());
    }

    // 使用直接缓冲区完成文件的复制（内存映射文件）
    @Test
    public void test2() {
        String newImageName = rootPath + "1_" + System.currentTimeMillis() + ".jpg";

        try (FileChannel in = FileChannel.open(Paths.get(imageName), StandardOpenOption.READ);
             FileChannel out = FileChannel.open(Paths.get(newImageName), StandardOpenOption.WRITE,
                     StandardOpenOption.READ, StandardOpenOption.CREATE)) {

            // 内存映射文件
            MappedByteBuffer inMappedBuf = in.map(MapMode.READ_ONLY, 0, in.size());
            MappedByteBuffer outMappedBuf = out.map(MapMode.READ_WRITE, 0, in.size());

            // 直接对缓冲区进行数据的读写操作即可
            byte[] dest = new byte[inMappedBuf.limit()];
            inMappedBuf.get(dest);
            outMappedBuf.put(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File newImage = new File(newImageName);
        assertTrue(newImage.exists());
//        assertTrue(newImage.delete()); // 无法删除成功，原因未知
    }

    // 通道之间的数据传输（直接缓冲区）
    @Test
    public void test3() throws IOException {
        String newImageName = rootPath + "1_" + System.currentTimeMillis() + ".jpg";

        FileChannel in = FileChannel.open(Paths.get(imageName),
                StandardOpenOption.READ);
        FileChannel out = FileChannel.open(Paths.get(newImageName),
                StandardOpenOption.WRITE, StandardOpenOption.READ,
                StandardOpenOption.CREATE);

        // 两种方式均可：1. 我给你；2. 你从我这接
        in.transferTo(0, in.size(), out);
//        out.transferFrom(in, 0, out.size());

        in.close();
        out.close();

        File newImage = new File(newImageName);
        assertTrue(newImage.exists());
        assertTrue(newImage.delete());
    }

    // 分散和聚集
    @Test
    public void test4() throws Exception {
        String fileName = rootPath + "1.txt";

        RandomAccessFile raf = new RandomAccessFile(fileName, "rw");

        // 1. 获取通道
        FileChannel channel1 = raf.getChannel();

        // 2. 分配指定大小的缓冲区
        ByteBuffer buf1 = ByteBuffer.allocate(100);
        ByteBuffer buf2 = ByteBuffer.allocate(1024);

        // 3. 分散读取
        ByteBuffer[] bufs = {buf1, buf2};
        channel1.read(bufs);

        for (ByteBuffer byteBuffer : bufs) {
            byteBuffer.flip();
        }

        System.out.println("----------------- 前100个字节 -----------------");
        System.out.println(new String(bufs[0].array(), 0, bufs[0].limit()));
        System.out.println("----------------- 后1024个字节 -----------------");
        System.out.println(new String(bufs[1].array(), 0, bufs[1].limit()));

        // 4. 聚集写入
        String newTxtName = rootPath + "2_" + System.currentTimeMillis() + ".txt";
        FileChannel channel2 = new RandomAccessFile(newTxtName, "rw")
                .getChannel();
        channel2.write(bufs);

        channel2.close();
        channel1.close();

        File newTxt = new File(newTxtName);
        assertTrue(newTxt.exists());
        assertTrue(newTxt.delete());
    }

    // NIO 支持的全部字符集
    @Test
    public void test5() {
        Charset.availableCharsets()
                .forEach((key, value) -> System.out.println(key + "\t" + value));
    }

    // 字符集
    @Test
    public void test6() throws IOException{
        Charset charset = Charset.forName("GBK");

        // 获取编码器
        CharsetEncoder encoder = charset.newEncoder();

        // 获取解码器
        CharsetDecoder decoder = charset.newDecoder();

        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put("Hello啊！NIO");
        charBuffer.flip();

        // 编码
        ByteBuffer bBuf = encoder.encode(charBuffer);
        for (int i = 0; i < 12; i++) {
            System.out.println(bBuf.get());
        }

        // 解码
        bBuf.rewind();
        System.out.println(bBuf.position());
        System.out.println(bBuf.limit());
        CharBuffer cBuf2 = decoder.decode(bBuf);
        System.out.println(cBuf2.toString());

        System.out.println("---------------------");

        Charset cs2 = Charset.forName("UTF-8");
        bBuf.rewind();
        CharBuffer cBuf3 = cs2.decode(bBuf);
        System.out.println(cBuf3.toString()); // GBK 用 UTF-8 解，导致中文乱码
    }
}
