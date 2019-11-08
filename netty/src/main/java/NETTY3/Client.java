package NETTY3;

import NETTY.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;

import java.net.InetSocketAddress;

/**
 * 客户端
 */
public class Client {
    //线程组
    private static final EventLoopGroup group = new NioEventLoopGroup();
    //启动类
    private static final Bootstrap bootstrap = new Bootstrap();
    private static final int PORT = 8081;
    private static final String HOST = "127.0.0.1";
    public static void start() throws InterruptedException {
        try {
            bootstrap.group(group)
                    .remoteAddress(new InetSocketAddress(HOST, PORT))
                    //长连接
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer() {
                        protected void initChannel(Channel channel) throws Exception {
                            //包含编码器和解码器
                            channel.pipeline().addLast(new HttpClientCodec());
                            //聚合
                            channel.pipeline().addLast(new HttpObjectAggregator(1024 * 10 * 1024));
                            //解压
                            channel.pipeline().addLast(new HttpContentDecompressor());
                            channel.pipeline().addLast(new ClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect().sync();
            //发送消息
            String msg = "<html><head><title>test</title></head><body>客户端请求</body></html>";
            channelFuture.channel().writeAndFlush(msg);
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Client.start();
    }
}