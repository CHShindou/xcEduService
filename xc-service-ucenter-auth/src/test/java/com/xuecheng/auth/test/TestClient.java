package com.xuecheng.auth.test;

import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.HeaderAssertions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @Auther : shindou
 * @Date : 2019/4/23
 * @Description : 功能描述  使用resttemplate测试认证服务接口来申请令牌
 * @Version : 1.0
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestClient {


    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    RestTemplate restTemplate;

    @Test
    public void testClient(){

        //采用客户端负载均衡，从eureka获取认证服务的ip和端口
        ServiceInstance serviceInstance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        URI uri = serviceInstance.getUri();
        String authUrl = uri + "/auth/oauth/token";

        //请求的内容分为两部分
        //1、header信息，包括http basic认证信息
        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        String httpBasic = httpbasic("XcWebApp","XcWebApp");
        headers.add("Authorization",httpBasic);

        //2、body  包括grant_type,username,password
        MultiValueMap<String,String> body = new LinkedMultiValueMap<>();

        body.add("grant_type","password");
        body.add("username","itcast");
        body.add("password","123");

        HttpEntity<MultiValueMap<String,String>> multiValueMapHttpEntity = new HttpEntity<>(body,headers);

        //指定restTemmlate在遇到400 401响应时不要抛出异常，正常返回值
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401){
                    super.handleError(response);
                }
            }

        });

        //远程调用申请令牌
        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, multiValueMapHttpEntity, Map.class);

        Map body1 = exchange.getBody();


        System.out.println(body1);

    }

    private String httpbasic(String clientId,String clientSecret){
        //将客户端id和客户端密码拼接，按 id：secret
        String string = clientId + ":" + clientSecret;

        //进行base64编码
        byte[] encode = Base64.encode(string.getBytes());
        return "Basic "+ new String(encode);

    }
}
