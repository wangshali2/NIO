package com.wsl.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * selector:选择器 多路复用器 单线程管理多个channel即多个网络连接
 * 多个客户端建立连接后，不需要对应一个个的线程了，这个连接注册到多路复用器上，通过多路复用器同时处理多个连接。
 */
public class SelectorDemo1 {

    public static void main(String[] args) throws IOException {
        //创建selector
        Selector selector = Selector.open();

        //channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //非阻塞
        serverSocketChannel.configureBlocking(false);

        //绑定连接
        serverSocketChannel.bind(new InetSocketAddress(9999));

        //将channel的哪些操作（即selector感兴趣的）注册到选择器上
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //查询已经就绪channel操作
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        //遍历集合
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while(iterator.hasNext()) {
            SelectionKey key = iterator.next();
            //判断key就绪状态操作
            if(key.isAcceptable()) {
                System.out.println("accepted");
                // a connection was accepted by a ServerSocketChannel.

            } else if (key.isConnectable()) {
                System.out.println("established");

                // a connection was established with a remote server.

            } else if (key.isReadable()) {
                System.out.println("reading");

                // a channel is ready for reading

            } else if (key.isWritable()) {
                System.out.println("writing");

                // a channel is ready for writing
            }
            iterator.remove();
        }
    }
}
