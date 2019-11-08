package BIO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author gq.wang
 * @version 1.0
 * @description: ${description}
 * @create: 2019-10-25 10:05
 */
public class BIOSocketServer {
    //默认的端口号
    private static int DEFAULT_PORT = 15012;
    //单例的ServerSocket
    private static ServerSocket serverSocket;
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
            while (true) {
                Socket client = serverSocket.accept();//阻塞的处理
                if(client!=null){
                    new Thread(new ServerHandle(client)).start();//启动一个线程处理数据的读取
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
            BIOSocketServer socketServer = new  BIOSocketServer();
            socketServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
