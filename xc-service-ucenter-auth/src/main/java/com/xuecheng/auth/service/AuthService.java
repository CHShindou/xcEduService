package com.xuecheng.auth.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.exception.ExceptionCast;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Auther : shindou
 * @Date : 2019/4/23
 * @Description : 功能描述
 * @Version : 1.0
 */

@Service
public class AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Value("${auth.tokenValiditySeconds}")
    int tokenValiditySeconds;

    public AuthToken login(String username,String password,String clientId,String clientSecret){

        //申请令牌
        AuthToken authToken = this.applyToken(username, password, clientId, clientSecret);
        if(authToken == null){
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
        }

        //将令牌信息存储到redis中，redis 的key为access_token访问令牌，值为authToken的JSON串
        boolean saveTokenResult = this.saveToken(authToken,tokenValiditySeconds);
        if(!saveTokenResult){
            //保存到redis失败
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_TOKEN_SAVEFAIL);
        }

        return authToken;

    }


    //将令牌存储到redis中
    private boolean saveToken(AuthToken authToken,int validityTimes){

        //设置key的值
        String key = "user_token:" + authToken.getAccess_token();

        //设置value的值
        String value = JSON.toJSONString(authToken);

        //保存到redis
        stringRedisTemplate.boundValueOps(key).set(value,validityTimes, TimeUnit.SECONDS);

        return stringRedisTemplate.getExpire(key)>0;

    }


    //申请令牌，通过restTemplate调用auth服务接口
    private AuthToken applyToken(String username,String password,String clientId,String clientSecret){

        //获取url  从eureka中获取
        ServiceInstance serviceInstance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        if(serviceInstance == null){
            LOGGER.error("choose an auth instance fail");
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_AUTHSERVER_NOTFOUND);
        }
        URI uri = serviceInstance.getUri();
        String authUri = uri + "/auth/oauth/token";

        //请求内容分为两部分
        //1、header  包括http basic认证信息
        MultiValueMap<String,String> header = new LinkedMultiValueMap<>();
        String authorization = this.httpBasic(clientId,clientSecret);
        header.add("Authorization",authorization);
        //2、body信息
        MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("username",username);
        body.add("password",password);

        HttpEntity<MultiValueMap<String,String>> httpEntity = new HttpEntity<>(body,header);

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
        ResponseEntity<Map> exchange = restTemplate.exchange(authUri, HttpMethod.POST, httpEntity, Map.class);

        Map exchangeBody = exchange.getBody();
        if(exchangeBody == null || exchangeBody.get("access_token") == null ||
                exchangeBody.get("refresh_token") == null || exchangeBody.get("jti") == null){

            String error_des = (String) exchangeBody.get("error_description");
            if("坏的凭证".equals(error_des)){
                ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
            }else if(error_des.indexOf("UserDetailsService returned null")>=0){
                ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);
            }
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
        }
        AuthToken authToken = new AuthToken();

        //获取JWT令牌
        String jwt_token = (String) exchangeBody.get("access_token");
        //获取刷新令牌
        String refresh_token = (String) exchangeBody.get("refresh_token");
        //获取访问令牌，作为redis的key  存储在cookie中
        String access_token = (String) exchangeBody.get("jti");

        authToken.setAccess_token(access_token);
        authToken.setRefresh_token(refresh_token);
        authToken.setJwt_token(jwt_token);

        return authToken;

    }

    //将clientId和clientSecret进行base64编码
    private String httpBasic(String clientId,String clientSecret){
        //按id：secret拼接
        String httpString = clientId + ":" + clientSecret;

        byte[] encode = Base64.encode(httpString.getBytes());
        return "Basic " + new String(encode);
    }


    //根据cookie查询redis中的jwt
    public AuthToken getUserJwtByCookie(String cookie){
        String key = "user_token:" + cookie;
        String tokenString = stringRedisTemplate.opsForValue().get(key);
        if(tokenString != null){
            AuthToken authToken = null;
            try{
                authToken = JSON.parseObject(tokenString, AuthToken.class);
            }catch (Exception e){
                LOGGER.error("getUserToken from redis and Execute JSON.parseObject error{}",e.getMessage());
                e.printStackTrace();
            }
            return authToken;
        }
        return null;
    }


    //从redis中删除令牌
    public boolean deleteJwtToken(String cookie){
        String key = "user_token:" + cookie;
        Boolean delete = stringRedisTemplate.delete(key);
        return delete;
    }
}
