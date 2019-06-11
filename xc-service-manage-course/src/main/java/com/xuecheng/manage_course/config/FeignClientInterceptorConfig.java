package com.xuecheng.manage_course.config;

import com.xuecheng.framework.interceptor.FeignClientInterCeptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther : shindou
 * @Date : 2019/5/1
 * @Description : 功能描述
 * @Version : 1.0
 */

@Configuration
public class FeignClientInterceptorConfig {

    @Bean
    public FeignClientInterCeptor getFeignClientInterCeptor(){
        return new FeignClientInterCeptor();
    }
}
