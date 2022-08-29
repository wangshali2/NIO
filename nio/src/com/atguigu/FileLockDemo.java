package com.atguigu;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileLockDemo {

    public static void main(String[] args) throws Exception {

        ByteBuffer buffer = ByteBuffer.wrap("wsl wsl wsl wsl ".getBytes());

        String filePath = "./nio/input/01.txt";
        Path path = Paths.get(filePath);

        FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        channel.position(channel.size() - 1);

        //加锁
       // FileLock lock = channel.lock(); //独占锁
       // FileLock lock1 = channel.lock(0L,Long.MAX_VALUE,true);  //共享锁 有读写时报异常
       FileLock lock2 = channel.tryLock(0L,Long.MAX_VALUE,false);  //非阻塞
        System.out.println("是否共享锁：" + lock2.isShared());

        channel.write(buffer);
        channel.close();
        System.out.println("写操作完成...");

        //读操作
        readFile(filePath);
    }

    private static void readFile(String filePath) throws Exception {
        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String tr = bufferedReader.readLine();
        System.out.print("读取出内容：");
        while (tr != null) {
            System.out.println(" " + tr);
            tr = bufferedReader.readLine();
        }
        fileReader.close();
        bufferedReader.close();
    }
}
