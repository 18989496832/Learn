package BIO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.SocketHandler;

//信息处理线程类
class  ServerHandle  implements  Runnable{
    Socket client;
    public  ServerHandle(Socket client){
        this.client = client;
    }
    @Override
    public void run() {
        DataInputStream input = null;
        DataOutputStream output = null;
        try {
            input=new DataInputStream(client.getInputStream());
            output = new DataOutputStream(client.getOutputStream());
            //服务端收到的请求信息
            String listMsg = input.readUTF();
            System.out.println("Server Receive Data:   " + listMsg);
            //服务端的应答处理
            output.writeUTF("Server Send Data:  " + listMsg + "    \r\n Thx...");
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("服务端接收处理完成");
            if(input!=null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                input=null;
            }
            if(output!=null){
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                output=null;
            }
            if(client!=null){
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                client=null;
            }
        }

    }
}