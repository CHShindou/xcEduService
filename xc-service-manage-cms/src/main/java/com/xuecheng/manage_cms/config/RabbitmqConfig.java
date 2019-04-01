package com.xuecheng.manage_cms.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
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


    //配置交换机
    @Bean(EX_CMS_POSTPAGE)
    public Exchange getExchange(){
        //durable:  持久化
        return ExchangeBuilder.directExchange(EX_CMS_POSTPAGE).durable(true).build();
    }

}
