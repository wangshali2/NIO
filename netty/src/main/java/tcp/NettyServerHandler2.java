package tcp;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 自定义的Handler 需要继承netty 规定好的某个HandlerAdapter(规范)
 */
public class NettyServerHandler2 extends ChannelInboundHandlerAdapter {

    //当通道有读取事件时会触发 自已看到
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("服务器读取线程 " + Thread.currentThread().getName() + " channel =" + ctx.channel());

        //比如这里我们有一个非常耗时长的业务-> 异步执行 -> 提交该channel对应的NIOEventLoop的taskQueue中
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000L);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("我是 2 ......",CharsetUtil.UTF_8));
                    System.out.println("channel code=" + ctx.channel().hashCode());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //提交到scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                ctx.writeAndFlush(Unpooled.copiedBuffer("我是 3 ......",CharsetUtil.UTF_8));
                System.out.println("channel code=" + ctx.channel().hashCode());
            }
        },5, TimeUnit.SECONDS);
    }


    //发送给Client
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
