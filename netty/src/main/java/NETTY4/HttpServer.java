package NETTY4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * 搭建HttpServer
 * 
 * @author Alanlee
 * @version 2018/01/11
 *
 */
public class HttpServer
{

    private final int port;

    public HttpServer(int port)
    {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException
    {
        new HttpServer(8081).start();
        System.out.println("Start http server success!");
    }

    public void start() throws InterruptedException
    {
        // 初始化channel的辅助类
        ServerBootstrap b = new ServerBootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        b.group(group).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>()
        {
            /**
             * 初始化channel
             */
            @Override
            protected void initChannel(SocketChannel ch) throws Exception
            {
                System.out.println("initChannel ch:" + ch);
                // 获取管道
                ch.pipeline()
                        .addLast("decoder", new HttpRequestDecoder())  //解码
                        .addLast("encoder", new HttpResponseEncoder()) //编码
                        /* aggregator，消息聚合器（重要）。
                        Netty4中为什么能有FullHttpRequest这个东西，
                        就是因为有他，HttpObjectAggregator，如果没有他，
                        就不会有那个消息是FullHttpRequest的那段Channel，
                        同样也不会有FullHttpResponse，HttpObjectAggregator(512 * 1024)的参数含义是消息合并的数据大小，
                        如此代表聚合的消息内容长度不超过512kb。*/
                        .addLast("aggregator", new HttpObjectAggregator(512 * 1024))
                        .addLast("handler", new HttpHandler()); // 请求的业务类
            }

        }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);

        // 创建服务器端channel的辅助类，接收connection请求
        b.bind(port).sync();
    }

}