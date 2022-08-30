package http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * netty的buffer 3要素:capacity、readerIndex、writerIndex。
 * 不需要使用flip 进行反转,底层维护了 readerIndex 和 writerIndex。
 */
public class NettyByteBuf01 {

    public static void main(String[] args) {


        ByteBuf buffer = Unpooled.buffer(10);

        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }

        System.out.println("capacity=" + buffer.capacity());//10


        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.getByte(i));
        }


        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.readByte());
        }
    }
}
