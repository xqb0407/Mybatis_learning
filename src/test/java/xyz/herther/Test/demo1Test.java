package xyz.herther.Test;

import org.junit.Test;
import xyz.herther.junit.demo1;

public class demo1Test {
//    注入测试对象
    private demo1 demo = new demo1();
    //加入测试注解
    @Test
    public void Testadd(){
        int sum = demo.sum(5, 6);
        System.out.println(sum);
    }
}
