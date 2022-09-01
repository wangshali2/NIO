package tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 1. 创建两个线程组 bossGroup 和 workerGroup
 * 2. bossGroup 负责接收客户端连接请求 ；workerGroup：负责网络读写操作；
 * 3. 两个都是无限循环，每个 NioEventLoop 都有一个selector，用于监听绑定在其上的 socket 网络通道。
 * 4. bossGroup 和 workerGroup 含有的子线程(NioEventLoop)的个数:默认cpu核数 * 2
 *
 * 异步操作Bind、Write、Connect返回ChannelFuture
 * http://localhost:16668/
 */
public class NettyServer {
    public static void main(String[] args) throws Exception {

        //Netty 为了更好的利用多核 CPU 资源，一般会有多个 EventLoop 同时工作，每个 EventLoop 维护着一个 Selector实例。
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();


        try {
            //服务器端的启动引导类
            ServerBootstrap bootstrap = new ServerBootstrap();

            //使用链式编程来进行设置
            bootstrap
                    .group(bossGroup, workerGroup) //设置两个线程组
                    .channel(NioServerSocketChannel.class) //使用NioSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列等待连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //用来给接收到的通道添加配置 设置保持活动连接状态
//                    .handler(null) // handler-->bossGroup , childHandler-->workerGroup
                    .childHandler(new ChannelInitializer<SocketChannel>() {//业务处理类 给pipeline 设置处理器

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            System.out.println("socketChannel hashcode : " + ch.hashCode());
                            System.out.println("socketChannel pipeline : " + ch.pipeline());

                            //1 channel --> 1 channelPipeline --> n handler
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });

            System.out.println(".....Server is ready...");

            //服务端绑定端口
            final ChannelFuture channelFuture = bootstrap.bind(16668).sync();

            //通过Future、ChannelFuture 注册监听器，监控异步I/O操作是否完成
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("监听端口 6668 成功 !!!" + channelFuture.channel());
                    } else {
                        System.out.println("监听端口 6668 失败 !!!");
                    }
                }
            });


            //对关闭通道事件  进行监听   等待异步操作执行完毕
            channelFuture.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
