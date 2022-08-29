package com.atguigu;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FilesDemo {

    public static void main(String[] args) throws IOException {
        Path sourcePath = Paths.get("./nio/input/newPath");
        Path destinationPath = Paths.get("./nio/input/newPath/01.txt");

//        Files.createDirectory(sourcePath);
//        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
//        Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
//        Files.delete(sourcePath);

        test();
    }

    public static void test() {
        Path rootPath = Paths.get("./nio/input/");
        String fileToFind = File.separator + "02.txt";

        try {
            Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                    String fileString = file.toAbsolutePath().toString();

                    if (fileString.endsWith(fileToFind)) {
                        System.out.println("file found at path: " + file.toAbsolutePath());
                        return FileVisitResult.TERMINATE;
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void pathTest() {
        //创建path实例
        Path path = Paths.get("./nio/input/01.txt");

        //创建相对路径
        Path projects = Paths.get("./nio/input/01.txt", "projects");

        Path path1 = Paths.get("./nio/input/01.txt");
        System.out.println("path1 = " + path1);

        Path path2 = path1.normalize();
        System.out.println("path2 = " + path2);
    }
}
