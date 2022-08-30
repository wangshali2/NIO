package com.wsl.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @Author wsl
 * @Description
 */
public class TcpDemo {
    public static void main(String[] args) throws IOException {

        Thread thread = new Thread(() -> {
            try {
                ServerDemo();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(false);
        thread.start();


        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ClientDemo();
    }

    public static void ClientDemo() throws IOException {
        //创建SocketChannel
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));
        //设置阻塞和非阻塞
        socketChannel.configureBlocking(true);
        System.out.println(socketChannel.isOpen() + "--" + socketChannel.isConnected());



        //读操作
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        socketChannel.read(byteBuffer);
        byte []arr=new byte[byteBuffer.position()];
        byteBuffer.rewind();
        byteBuffer.get(arr);

        System.out.println(new String(arr));

        //socketChannel.close();
    }

    public static void ServerDemo() throws IOException, InterruptedException {
        ByteBuffer buffer = ByteBuffer.wrap("hello wsl i love you".getBytes());
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(9999));
        ssc.configureBlocking(true); //非阻塞

        //监听有新链接传入
        while (true) {
            System.out.println("Waiting for connections.....");

            SocketChannel sc = ssc.accept();
            if (sc == null) { //没有链接传入
                System.out.println("null");

            } else {
                System.out.println("Incoming connection from: " + sc.socket().getRemoteSocketAddress());
                buffer.clear();
                buffer.put("hello wsl i love you".getBytes());
                buffer.flip();

                sc.write(buffer);
                Thread.sleep(2000);
                sc.close();
            }
        }
    }
}
