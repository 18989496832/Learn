package BIO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author gq.wang
 * @version 1.0
 * @description: ${description}
 * @create: 2019-10-25 10:05
 */
public class BIOSocketBetterServer {
    //默认的端口号
    private static int DEFAULT_PORT = 15013;
    //单例的ServerSocket
    private static ServerSocket serverSocket;
    //线程池 懒汉式的单例
    //private static ExecutorService executorService = Executors.newFixedThreadPool(60);
    //1、直接提交队列：设置为SynchronousQueue队列，当任务队列为SynchronousQueue，创建的线程数大于maximumPoolSize时，直接执行了拒绝策略抛出异常
    //2、有界的任务队列：有界的任务队列可以使用ArrayBlockingQueue实现
    //3、无界的任务队列：有界任务队列可以使用LinkedBlockingQueue实现
    //4、优先任务队列：优先任务队列通过PriorityBlockingQueue实现
    private static ExecutorService executorService= new ThreadPoolExecutor(
            10,
            10,
            60L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue(10),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
            );
    //根据传入参数设置监听端口，如果没有参数调用以下方法并使用默认值
    public static void start() throws IOException{
        start(DEFAULT_PORT);
    }
    public synchronized  static void start(int prot) throws IOException {
        if(serverSocket!=null){
            return;
        }
        try {
            serverSocket = new ServerSocket(prot);
            //通过无线循环监听客户端连接
            //如果没有客户端接入，将阻塞在accept操作上。
            while (true) {
                Socket client = serverSocket.accept();//阻塞的处理
                //当有新的客户端接入时，会执行下面的代码
                //然后创建一个新的线程处理这条Socket链路
                if(client!=null){
                    executorService.execute(new ServerHandle(client));//启动一个线程处理数据的读取
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(serverSocket!=null){
                serverSocket.close();
                serverSocket=null;
            }
        }
    }
    public static void main(String[] args) {
        try {
            BIOSocketBetterServer socketServer = new BIOSocketBetterServer();
            socketServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
