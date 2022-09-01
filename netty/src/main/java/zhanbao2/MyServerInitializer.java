package zhanbao2;


import coder.MyByteToLongDecoder;
import coder.MyLongToByteEncoder;
import coder.MyServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


public class MyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) {

        ChannelPipeline pipeline = ch.pipeline();

        //入站的handler进行解码 MyByteToLongDecoder
        pipeline.addLast(new MyMessageDecoder());
        //出站的handler进行编码
        pipeline.addLast(new MyMessageEncoder());
        //自定义的handler 处理业务逻辑
        pipeline.addLast(new MyServerHandler2());

        System.out.println("服务器初始化 ok ....");
    }
}

