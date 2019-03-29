package com.xuecheng.manage_cms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @Auther: Shindou
 * @Date: 2019/3/29
 * @Description: com.xuecheng.manage_cms.config
 * @Version: 1.0
 */

@Configuration
public class RestTempConfig {


    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
