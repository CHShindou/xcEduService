package com.xuecheng.framework.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @Auther : shindou
 * @Date : 2019/5/1
 * @Description : 功能描述
 * @Version : 1.0
 */


public class FeignClientInterCeptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        //使用requestContextHolder工具获取request相关变量
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null){
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if(headerNames != null){
                while (headerNames.hasMoreElements()){
                    String name = headerNames.nextElement();
                    if("Authorization".equalsIgnoreCase(name)){
                        String value = request.getHeader(name);
                        requestTemplate.header(name,value);
                    }

                }
            }
        }
    }
}
