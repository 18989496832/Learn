package BIO2;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 * @author 闪电侠
 */
public class IOClient {

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 8000);
                try {
                    socket.getOutputStream().write((new Date() + ": hello world").getBytes());
                    socket.getOutputStream().flush();
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    System.out.println(in.readUTF());
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
            } catch (IOException e) {
            }
        }).start();
    }
}