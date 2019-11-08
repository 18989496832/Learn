package NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


public class NIOServer
{
    /**
     * NIO核心1.选择器
     */
    private Selector selector;
    /**
     * NIO核心2.通道
     */
    ServerSocketChannel serverSocketChannel;
 
    public void initServer(int port) throws IOException
    {
        //1.打开一个通道
        serverSocketChannel = ServerSocketChannel.open();
 
        //2.通道设置非阻塞
        serverSocketChannel.configureBlocking(false);
 
        //3.绑定端口号
        ServerSocket serverSocket = serverSocketChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        serverSocket.bind(address);
        //serverSocketChannel.socket().bind(new InetSocketAddress("localhost", port));

        //4.打开多路复用器
        this.selector = Selector.open();

        /**
         * 5.将新打开的 ServerSocketChannels 注册到 Selector上,并制定监听事件
         * SelectionKey 代表这个通道在此 Selector 上的这个注册。
         * 当某个 Selector 通知您某个传入事件时，它是通过提供对应于该事件的 SelectionKey 来进行的。
         * SelectionKey 还可以用于取消通道的注册
         */
        SelectionKey key = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }
    //内部循环
    public void listen() throws IOException
    {
        System.out.println("server started succeed!");
        while (true)
        {
            //选择一组键，其对应的通道已准备好进行I/O操作
            //该调用会阻塞，直到至少有一个事件就绪、准备发生
            //1.多路复用器开始监听
            selector.select();
            //2.获取所有的selectedKeys结果集进行遍历
            Iterator<SelectionKey> ite = selector.selectedKeys().iterator();
            while (ite.hasNext())
            {
                SelectionKey key = ite.next();
                //7.如果是阻塞的话
                if (key.isAcceptable())
                {
                    //服务端通道处理接收到的数据
                    //9.进行等待
                    SocketChannel channel = serverSocketChannel.accept();
                    //10.设置为非阻塞的模式
                    channel.configureBlocking(false);//非阻塞
                    //11.将客户端注册到多路复用器上,监听可读事件
                    channel.register(selector, SelectionKey.OP_READ);
                }
                //12.如果是可读状态
                else if (key.isReadable())
                {
                    recvAndReply(key);
                }
                //5.从集合移除当前选择的selectionKey
                ite.remove();
            }
        }
    }

    public void recvAndReply(SelectionKey key) throws IOException
    {   //13.如果是可读状态
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(20);
        int i = channel.read(buffer);
        if (i != -1)
        {
            String msg = new String(buffer.array()).trim();
            System.out.println("NIO server received message =  " + msg);
            System.out.println("NIO server reply =  " + msg);
            //应答
            channel.write(ByteBuffer.wrap( msg.getBytes()));
        }
        //14.如果没有数据
        else
        {
            channel.close();
            key.cancel();
        }
        buffer.clear();
    }
 
    public static void main(String[] args) throws IOException
    {
        NIOServer server = new NIOServer();
        server.initServer(8080);
        server.listen();
    }
}