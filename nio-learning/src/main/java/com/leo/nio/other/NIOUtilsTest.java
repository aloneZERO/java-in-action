package com.leo.nio.other;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributeView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author justZero
 * @since 2019/3/14
 */
public class NIOUtilsTest {

    /*
     * 文件路径工具类 Paths
     * Paths 提供的 get() 方法用来获取 Path 对象：
     *     Path get(String first, String … more) : 用于将多个字符串串连成路径。
     * Path 常用方法：
     *     boolean endsWith(String path) : 判断是否以 path 路径结束
     *     boolean startsWith(String path) : 判断是否以 path 路径开始
     *     boolean isAbsolute() : 判断是否是绝对路径
     *     Path getFileName() : 返回与调用 Path 对象关联的文件名
     *     Path getName(int idx) : 返回的指定索引位置 idx 的路径名称
     *     int getNameCount() : 返回Path 根目录后面元素的数量
     *     Path getParent() ：返回Path对象包含整个路径，不包含 Path 对象指定的文件路径
     *     Path getRoot() ：返回调用 Path 对象的根路径
     *     Path resolve(Path p) :将相对路径解析为绝对路径
     *     Path toAbsolutePath() : 作为绝对路径返回调用 Path 对象
     *     String toString() ： 返回调用 Path 对象的字符串表示形式
     */
    @Test
    public void test1() {
        // 路径可以不存在
        Path path = Paths.get("e:/", "nio/hello.txt");

        assertTrue(path.endsWith("hello.txt"));
        assertTrue(path.startsWith("e:/"));
        assertTrue(path.isAbsolute());
        assertEquals("hello.txt", path.getFileName().toString());

        for (int i = 0; i < path.getNameCount(); i++) {
            System.out.print(path.getName(i) + " ");
        }

        assertEquals("e:" + File.separator + "nio", path.getParent().toString());
        assertEquals("e:" + File.separator, path.getRoot().toString());

        Path path2 = Paths.get("1.jpg");
        Path newPath = path2.toAbsolutePath();
        System.out.println(newPath);
    }

    /*
     * 文件工具类 Files
     * Files常用方法：
     *     Path copy(Path src, Path dest, CopyOption … how) : 文件的复制
     *     Path createDirectory(Path path, FileAttribute<?> … attr) : 创建一个目录
     *     Path createFile(Path path, FileAttribute<?> … arr) : 创建一个文件
     *     void delete(Path path) : 删除一个文件
     *     Path move(Path src, Path dest, CopyOption…how) : 将 src 移动到 dest 位置
     *     long size(Path path) : 返回 path 指定文件的大小
     */
    @Test
    public void test2() throws IOException {
        Path dir = Paths.get("e:/nio");
        Files.createDirectory(dir);

        Path file1 = Paths.get("e:/nio/hello.txt");
        Path file2 = Paths.get("e:/nio/hello2.txt");
        Files.createFile(file1);
        Files.createFile(file2);
        Files.write(file1, "Hello World!".getBytes(), StandardOpenOption.WRITE);
        System.out.println(Files.size(file1) + "字节");

        Files.copy(file1, file2, StandardCopyOption.REPLACE_EXISTING);
        assertEquals(Files.size(file1), Files.size(file2));

        assertTrue(Files.deleteIfExists(file1));
        assertTrue(Files.deleteIfExists(file2));
        assertTrue(Files.deleteIfExists(dir));
    }

    /*
     * Files 常用方法：用于判断
     *     boolean exists(Path path, LinkOption … opts) : 判断文件是否存在
     *     boolean isDirectory(Path path, LinkOption … opts) : 判断是否是目录
     *     boolean isExecutable(Path path) : 判断是否是可执行文件
     *     boolean isHidden(Path path) : 判断是否是隐藏文件
     *     boolean isReadable(Path path) : 判断文件是否可读
     *     boolean isWritable(Path path) : 判断文件是否可写
     *     boolean notExists(Path path, LinkOption … opts) : 判断文件是否不存在
     *     public static <A extends BasicFileAttributes> A readAttributes(Path path,Class<A> type,LinkOption... options) :
     *         获取与 path 指定的文件相关联的属性。
     */
    @Test
    public void test3() throws IOException {
        Path path = Paths.get("e:/hello.txt");
        Files.createFile(path);

        BasicFileAttributes readAttributes = Files.readAttributes(path,
                BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);

        System.out.println(readAttributes.creationTime());
        System.out.println(readAttributes.lastModifiedTime());

        DosFileAttributeView fileAttributeView = Files.getFileAttributeView(path,
                DosFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);

        fileAttributeView.setHidden(false);

        Files.deleteIfExists(path);
    }

    /*
     * Files常用方法：用于操作内容
     *     SeekableByteChannel newByteChannel(Path path, OpenOption…how) : 获取与指定文件的连接，how 指定打开方式。
     *     DirectoryStream newDirectoryStream(Path path) : 打开 path 指定的目录
     *     InputStream newInputStream(Path path, OpenOption…how):获取 InputStream 对象
     *     OutputStream newOutputStream(Path path, OpenOption…how) : 获取 OutputStream 对象
     */
    @Test
    public void test4() throws IOException {
        Path tempFile = Paths.get("e:/hello.txt");
        Files.createFile(tempFile);
        Files.write(tempFile, "123456".getBytes());

        SeekableByteChannel newByteChannel = Files.newByteChannel(tempFile, StandardOpenOption.READ);
        assertEquals(6, newByteChannel.size());
        assertTrue(Files.deleteIfExists(tempFile));

        DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(Paths.get("e:/"));
        for (Path path : newDirectoryStream) {
            System.out.println(path);
        }
    }
}
