package coder;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler  extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {


        System.out.println("收到服务器消息 ：" + ctx.channel().remoteAddress() + " 读取到long " + msg);

    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("01234567890123456789", CharsetUtil.UTF_8));
        //ctx.writeAndFlush(1234567890123456789L); //发送的是一个long
    }
}

