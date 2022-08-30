package http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 自定义的Handler 需要继承netty 规定好的某个HandlerAdapter(规范)
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("服务器读取线程 " + Thread.currentThread().getName() + " channel =" + ctx.channel());

        Channel channel = ctx.channel();
        ChannelPipeline pipeline = ctx.pipeline(); //本质是一个双向链表


        //将 msg 转成一个 ByteBuf
        //ByteBuf 是 Netty 提供的，不是 NIO 的 ByteBuffer.
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送消息是:" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址:" + channel.remoteAddress());
    }


    //ChannelHandlerContext   保存 Channel 相关的所有上下文信息，同时关联一个 ChannelHandler 对象
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
       //发送给client
        ctx.writeAndFlush(Unpooled.copiedBuffer("HTTP/1.1 200 OK\nContent-Type: text/html;charset=utf-8\n\nhello 客户端，我是服务器....",CharsetUtil.UTF_8));
        ctx.close(); //不加服务器一直在请求 转圈
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
