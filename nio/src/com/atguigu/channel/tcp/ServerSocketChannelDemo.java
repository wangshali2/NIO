package com.atguigu.channel.tcp;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 传统的socket是基于流的 ，NIO是基于通道的
 *
 * DatagramChannel、SocketChannel 、 ServerSocketChannel
 * window cmd  ：telnet 127.0.01 9999
 * 界面会收到数据
 *
 * ServerSocketChannel对应ServerSocket服务端
 */

public class ServerSocketChannelDemo {

    public static void main(String[] args) throws Exception {


        ByteBuffer buffer = ByteBuffer.wrap("hello wsl i love you".getBytes());

        ServerSocketChannel ssc = ServerSocketChannel.open();


        ssc.socket().bind(new InetSocketAddress(9999));

        //false：非阻塞
        ssc.configureBlocking(false);

        //监听有新链接传入
        while (true) {
            System.out.println("Waiting for connections");
            SocketChannel sc = ssc.accept();
            if (sc == null) { //没有链接传入
                System.out.println("null");
                Thread.sleep(2000);
            } else {
                System.out.println("Incoming connection from: " + sc.socket().getRemoteSocketAddress());
                buffer.rewind(); //指针0
                sc.write(buffer);
                // sc.close();
            }
        }
    }
}
