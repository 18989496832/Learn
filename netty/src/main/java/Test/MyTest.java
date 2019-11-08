package Test;

/**
 * @author gw.wang
 * @version 1.0
 * @description: ${description}
 * @create: 2019-10-24 09:38
 */
public class MyTest {
    public static void main(String[] args){
        System.out.println(" My First Gradle Project !");
        add(1,2);
    }
    public static void add(int a , int b ){
        a = a + b ;
        System.out.println(a);
    }
}
