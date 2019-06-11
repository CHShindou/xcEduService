package com.xuecheng.manage_cms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * @Auther: Shindou
 * @Date: 2019/4/23
 * @Description: com.xuecheng.manage_course.config
 * @Version: 1.0
 */

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)  //激活方法上的preAuthorize注解
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    //公钥
    private static final String PUBLIC_KEY = "publickey.txt";

    //定义jwt TokenStore，使用jwt令牌
    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter){
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    //定义JwtAccessTokenConverter,使用jwt令牌
    @Bean
    public JwtAccessTokenConverter getJwtAccessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setVerifierKey(this.getPubkey());
        return converter;
    }

    /**
     * 获取非对称加密公钥key
     * @return 公钥key
     */
    private String getPubkey(){
        Resource resource = new ClassPathResource(PUBLIC_KEY);

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    //Http安全配置，对每个到达系统的http请求链接进校验
    @Override
    public void configure(HttpSecurity http) throws Exception {

        //所有请求必须认证通过   .antMatchers内的路径放行
        http.authorizeRequests()
                .antMatchers("/v2/api‐docs", "/swagger‐resources/configuration/ui",
                        "/swagger‐resources","/swagger‐resources/configuration/security",
                        "/swagger‐ui.html","/webjars/**").permitAll()
                .anyRequest().authenticated();

    }
}
