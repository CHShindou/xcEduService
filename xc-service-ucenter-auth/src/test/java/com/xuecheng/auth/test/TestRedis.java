package com.xuecheng.auth.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Auther : shindou
 * @Date : 2019/4/23
 * @Description : 功能描述
 * @Version : 1.0
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedis {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //测试redis
    @Test
    public void testRedis(){

        //定义key
        String key = "user_token:77526345";

        //定义value
        Map<String,String> map = new HashMap<>();
        map.put("id","1001");
        map.put("name","asan");
        String value = JSON.toJSONString(map);
        stringRedisTemplate.boundValueOps(key).set(value,2, TimeUnit.MINUTES);

        //获取过期时间，已过期返回-2
        Long expire = stringRedisTemplate.getExpire(key);

        //根据key获取value
        String myvalue = stringRedisTemplate.opsForValue().get(key);
        System.out.println(myvalue);

    }

}
