package com.xuecheng.test.fastdfs;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class Test01 {

    public static void main(String[] args){
        System.out.println("hello world");
    }



    @Test
    public void test01(){
        StringBuffer sb = new StringBuffer();
        String s = sb.append("a").append("b").append("c").toString();
        System.out.println(s);
    }
}
