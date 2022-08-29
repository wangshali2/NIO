package com.atguigu.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author wsl
 * @Description  InputStream、OutputStream、RandomAccessFile
 */
public class FileChannelDemo {
    public static void main(String[] args) throws IOException {
        read();
        write();
        fromTo();
        many();
    }

    /**
     * Scatter分散 read:1个channel-->多个buffer
     * Gather聚集 write：多个buffer-->1个channel
     * @throws IOException
     */
    public static void many() throws IOException {
        RandomAccessFile aFile = new RandomAccessFile("./nio/input/01.txt", "rw");
        FileChannel channel = aFile.getChannel();
        ByteBuffer header = ByteBuffer.allocate(128);
        ByteBuffer body = ByteBuffer.allocate(1024);
        ByteBuffer[] arr = {header, body};
        channel.read(arr);
        channel.write(arr);
    }

    /**
     * channel-channel之间数据传输
     *
     * @throws IOException
     */
    public static void fromTo() throws IOException {
        RandomAccessFile aFile = new RandomAccessFile("./nio/input/01.txt", "rw");
        FileChannel fromChannel = aFile.getChannel();

        RandomAccessFile bFile = new RandomAccessFile("./nio/input/03.txt", "rw");
        FileChannel toChannel = bFile.getChannel();

        //fromChannel 传输到 toChannel
        long position = 0;
        long size = fromChannel.size();

        //transferFrom将数据从源通道传输到 FileChannel 中
        // toChannel.transferFrom(fromChannel,position,size);

        //transferTo()将数据从 FileChannel传输到其他的 channel 中
        fromChannel.transferTo(0, size, toChannel);

        aFile.close();
        bFile.close();
        System.out.println("over!");
    }

    /**
     * 写入：从 buffer-->FileChannel
     *
     * @throws IOException
     */
    public static void write() throws IOException {
        // 1.打开FileChannel
        RandomAccessFile aFile = new RandomAccessFile("./nio/input/02.txt", "rw");
        FileChannel channel = aFile.getChannel();

        //创建buffer对象
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();

        //写入内容
        buffer.put("newData".getBytes());

        buffer.flip();

        //FileChannel完成最终实现
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }

        channel.close();
    }

    /**
     * 读取：从FileChannel-->buffer
     *
     * @throws IOException
     */
    public static void read() throws IOException {
        //1.创建FileChannel
        RandomAccessFile file = new RandomAccessFile("./nio/input/01.txt", "rw");
        FileChannel channel = file.getChannel();

        //创建Buffer
        ByteBuffer buf = ByteBuffer.allocate(1024);

        //指定size后删除
        channel.truncate(1024);
        //从指定位置读取
        channel.position(channel.position() + 1);
        channel.force(true);

        int bytesRead = channel.read(buf);

        while (bytesRead != -1) {
            System.out.println("读取了：" + bytesRead + ";   channel的size：" + channel.size());

            buf.flip();
            while (buf.hasRemaining()) {
                System.out.println((char) buf.get());
            }
            buf.clear();
            bytesRead = channel.read(buf);
        }
        file.close();
        System.out.println("结束了");
    }
}
