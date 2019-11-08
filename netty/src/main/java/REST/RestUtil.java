package REST;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestUtil {
 
  //get
  public void getMethod(String url) throws IOException {
    URL restURL = new URL(url);
 
    HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
 
    conn.setRequestMethod("GET"); // POST GET PUT DELETE
    conn.setRequestProperty("Accept", "application/json");
 
    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String line;
    while((line = br.readLine()) != null ){
      System.out.println("读取结果："+line);
    }
 
    br.close();
  }
 
  //post 
  public void postMethod(String url, String query) throws IOException {
    URL restURL = new URL(url);
 
    HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Content-Type", "application/json");
    conn.setDoOutput(true);
 
    PrintStream ps = new PrintStream(conn.getOutputStream());
    ps.print(query);
    ps.close();
 
    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String line;
    while((line = br.readLine()) != null ){
      System.out.println("发送JSON数据："+line);
    }
 
    br.close();
  }
 
  public static void main(String[] args) {
    RestUtil restUtil = new RestUtil();
    try {
      restUtil.getMethod("http://www.baidu.com");
      String jsonString = "{\"sex\":\"男\",\"name\":\"张三\",\"age\":25}";
      //发送json数据到REST接口
      String url = "http://www.baidu.com";
      String query =jsonString; //json格式
      restUtil.postMethod(url, query);
 
    }catch (Exception e){
      System.out.println(e.getMessage());
    }
  }
 
}