package NETTY2;
 
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
 
import java.beans.beancontext.BeanContextChildComponentProxy;
import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2017/5/16.
 */
public class TimeServer {
    public static void main(String[] args) {
        int port = 9898;
        new TimeServer().bind(port);
    }
 
    public void bind(int port) {
        /**
         * interface EventLoopGroup extends EventExecutorGroup extends ScheduledExecutorService extends ExecutorService
         * 配置服务端的 NIO 线程池,用于网络事件处理，实质上他们就是 Reactor 线程组,多路复用同步模式
         * bossGroup 用于服务端接受客户端连接，
         * workerGroup 用于进行 SocketChannel 网络读写*/
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            /** ServerBootstrap 是 Netty 用于启动 NIO 服务端的辅助启动类，用于降低开发难度
             * */
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    // 指定Channel
                    .channel(NioServerSocketChannel.class)
                    //使用指定的端口设置套接字地址
                    .localAddress(new InetSocketAddress(port))
                    //服务端可连接队列数,对应TCP/IP协议listen函数中backlog参数
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //设置TCP长连接,一般如果两个小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //将小的数据包包装成更大的帧进行传送，提高网络的负载
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 配置入站、出站事件handler
                    .childHandler(new ChildChannelHandler());

            /**服务器启动辅助类配置完成后，调用 bind 方法绑定监听端口，调用 sync 方法同步等待绑定操作完成*/
            ChannelFuture f = b.bind().sync();
            if (f.isSuccess()) {
                System.out.println(Thread.currentThread().getName() + ",服务器开始监听端口，等待客户端连接.........");
            }
            /**
             * 下面会进行阻塞，等待服务器连接关闭之后 main 方法退出，程序结束
             * */
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            /**优雅退出，释放线程池资源*/
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
 
    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel arg0) throws Exception {
            arg0.pipeline().addLast(new TimeServerHandler());
        }
    }
}