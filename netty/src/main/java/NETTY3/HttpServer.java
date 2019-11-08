package NETTY3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * netty server
 * 2018/11/1.
 * server端代码就这么多，看起来很长，但是这就是一个样板代码，你需要着重留意的就是childHandler(new HttpServerInitializer());这一行。如果你对netty还不是十分熟悉，那么你不需要着急把每一行的代码都看懂。这段代码翻译成可以理解的文字是这样的：
 * 1.bootstrap为启动引导器。
 * 2.指定了使用两个时间循环器。EventLoopGroup
 * 3.指定使用Nio模式。（NioServerSocketChannel.class）
 * 4.初始化器为HttpServerInitializer
 * server启动代码就是这么多，我们注意看 HttpServerInitializer 做了什么
 */
public class HttpServer {
    int port ;
    public HttpServer(int port){
        this.port = port;
    }
    public void start() throws Exception{
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        bootstrap.group(boss,work)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .channel(NioServerSocketChannel.class)
                .childHandler(new HttpServerInitializer());
        ChannelFuture f = bootstrap.bind(new InetSocketAddress(port)).sync();
        System.out.println(" server start up on port : " + port);
        f.channel().closeFuture().sync();

    }
}