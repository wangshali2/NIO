import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class BufferDemo {


    public static void main(String[] args) throws Exception {

        mapBuffer();
        directBuffer();
        readBuffer();
        sliceBuffer();

        intBuffer();
    }


    static private final int start = 0;
    static private final int size = 1024;

    //内存映射文件io

    public static void mapBuffer() throws Exception {
        RandomAccessFile raf = new RandomAccessFile("./nio/input/01.txt", "rw");
        FileChannel fc = raf.getChannel();
        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, start, size);

        mbb.put(0, (byte) 97);
        mbb.put(1023, (byte) 122);
        raf.close();
    }


    //直接缓冲区  避免2次copy

    public static void directBuffer() throws Exception {

        FileInputStream fin = new FileInputStream("./nio/input/01.txt");
        FileChannel finChannel = fin.getChannel();

        FileOutputStream fout = new FileOutputStream("./nio/input/04.txt");
        FileChannel foutChannel = fout.getChannel();

        //创建直接缓冲区
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

        while (true) {
            buffer.clear();
            int r = finChannel.read(buffer);
            if (r == -1) {
                break;
            }
            buffer.flip();
            foutChannel.write(buffer);
        }
    }

    //只读缓冲区
    @Test
    public static void readBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }

        //创建只读缓冲区
        ByteBuffer readonly = buffer.asReadOnlyBuffer();

        for (int i = 0; i < buffer.capacity(); i++) {
            byte b = buffer.get(i);
            b *= 10;
            buffer.put(i, b);
        }

        readonly.position(0);
        readonly.limit(buffer.capacity());

        while (readonly.remaining() > 0) {
            System.out.println(readonly.get());
        }
    }


    //缓冲区分片
    @Test
    public static void sliceBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }

        //创建子缓冲区
        buffer.position(3);
        buffer.limit(7);
        ByteBuffer slice = buffer.slice();

        //改变子缓冲区内容
        for (int i = 0; i < slice.capacity(); i++) {
            byte b = slice.get(i);
            b *= 10;
            slice.put(i, b);
        }

        buffer.position(0);
        buffer.limit(buffer.capacity());

        while (buffer.remaining() > 0) {
            System.out.println(buffer.get());
        }
    }

    public static void intBuffer()  {

        //创建buffer
        IntBuffer buffer = IntBuffer.allocate(8);

        for (int i = 0; i < buffer.capacity(); i++) {
            int j = 2 * (i + 1);
            //入buffer
            buffer.put(j);
        }

        //重置缓冲区
        buffer.flip();  //从写模式-->读模式

        //获取
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get() + " ");
        }

        buffer.clear(); //读完了就需要清空buffer,以便可以再写入数据
        //  buffer.compact();//只会清楚已经读过的数据
    }
}
