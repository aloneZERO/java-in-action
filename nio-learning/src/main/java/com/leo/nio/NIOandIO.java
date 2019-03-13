package com.leo.nio;

/**
 * 1. IO 是面向流的。流可理解为水流，同时负责连接和数据的传输，且单向。
 *    NIO 是面向缓冲区的。“通道”负责数据的连接，缓冲区负责数据的传输，
 *    且是双向的。通道可理解为铁路（只负责连接），运输依托于火车（缓冲区）。
 *
 * 2. IO 是阻塞的（Blocking IO）。NIO 是非阻塞 IO（Non Blocking IO）
 *
 * 3. NIO 新增了选择器（Selectors）的功能。
 *
 *
 * @author justZero
 * @since 2019/3/13
 */
public interface NIOandIO {
}
