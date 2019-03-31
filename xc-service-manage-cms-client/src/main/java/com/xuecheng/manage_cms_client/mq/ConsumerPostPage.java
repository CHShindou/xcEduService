package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.manage_cms_client.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Auther : shindou
 * @Date : 2019/3/31
 * @Description : 功能描述 MQ消费者端，监听队列
 * @Version : 1.0
 */

@Component
public class ConsumerPostPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerPostPage.class);

    @Autowired
    PageService pageService;

    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(String msg){

        //msg是个json格式的字符串
        Map map = JSON.parseObject(msg,Map.class);
        LOGGER.info("receive cms post page:{}",msg.toString());
        String pageId = (String) map.get("pageId");

        //调用service将页面保存到物理路径
        pageService.savePageToServerPath(pageId);
    }
}
