package com.xuecheng.manage_cms_client.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther : shindou
 * @Date : 2019/3/31
 * @Description : 功能描述
 * @Version : 1.0
 */
@Configuration
public class RabbitmqConfig {

    //交换机名称
    public static final String EX_CMS_POSTPAGE = "ex_cms_postpage";

    //队列bean的名称(加载到容器中的名称)
    public static final String QUEUE_CMS_POSTPAGE = "queue_cms_postpage";

    //队列名称(从配置文件中获取)
    @Value("${xuecheng.mq.queue}")
    public String queue_cms_postpage_name;

    //路由名称(即站点id 从配置文件中获取)
    @Value("${xuecheng.mq.routingKey}")
    public String routingKey;

    //配置交换机
    @Bean(EX_CMS_POSTPAGE)
    public Exchange getExchange(){
        //durable:  持久化
        return ExchangeBuilder.directExchange(EX_CMS_POSTPAGE).durable(true).build();
    }
    //声明队列
    @Bean(QUEUE_CMS_POSTPAGE)
    public Queue getQueue(){
        Queue queue = new Queue(queue_cms_postpage_name);
        return queue;
    }

    /**
     * 交换机和队列绑定
     * @param queue 队列
     * @param exchange 交换机
     * @return
     */
    @Bean
    public Binding bindingQueueExchange(
            @Qualifier(QUEUE_CMS_POSTPAGE) Queue queue, @Qualifier(EX_CMS_POSTPAGE) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }
}
