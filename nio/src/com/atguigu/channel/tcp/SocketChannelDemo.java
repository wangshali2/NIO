package com.atguigu.channel.tcp;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * 是一个连接到 TCP 网络套接字的通道
 * SocketChannel对应Socket客户端
 */
public class SocketChannelDemo {

    public static void main(String[] args) throws Exception {
        //创建SocketChannel
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("www.baidu.com", 80));

//        SocketChannel socketChanne2 = SocketChannel.open();
//        socketChanne2.connect(new InetSocketAddress("www.baidu.com", 80));

        System.out.println(socketChannel.isOpen() + "--" +
                socketChannel.isConnected() + "--" +
                socketChannel.isConnectionPending() + "--" +
                socketChannel.finishConnect());


        //设置阻塞和非阻塞
        socketChannel.configureBlocking(false);

        //读操作
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        socketChannel.read(byteBuffer);
        socketChannel.close();
        System.out.println("read over");

    }

}
