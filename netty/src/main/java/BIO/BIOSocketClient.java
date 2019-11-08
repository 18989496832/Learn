package BIO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


class BIOSocketClient {
    //默认的端口号
    private static int DEFAULT_PORT = 15013;
    private static String IP = "127.0.0.1";
    //单例的socket
    private static Socket socket;
    //根据传入参数设置监听端口，如果没有参数调用以下方法并使用默认值
    public static void startSocket() throws IOException{
        if(socket!=null){
            return;
        }else{
            socket = new Socket(IP, DEFAULT_PORT);
        }
    }
    public static void closeSocket() throws IOException{
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
            }
            socket =null;
        }
    }
    public synchronized  static void sendMsg(String msg) throws IOException {
        try {
            //向服务器端第一次发送字符串
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            out.writeUTF(msg);//请求
            String res = in.readUTF();//相应
            System.out.println(res);
            out.close();
            in.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }
    public static void main(String[] args) {
        for(int i=0;i<10000;i++){
            try {
                BIOSocketClient  bioSocketClient = new  BIOSocketClient();
                bioSocketClient.startSocket();
                bioSocketClient.sendMsg("I is client good  good  good  good  good  good  good  good  good  good  good  good  good  good  good  good  good  good Msg "+i+" !");
                bioSocketClient.closeSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}