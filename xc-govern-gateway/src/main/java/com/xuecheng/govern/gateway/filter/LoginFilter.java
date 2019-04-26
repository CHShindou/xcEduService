package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.govern.gateway.service.ZoolService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: Shindou
 * @Date: 2019/4/26
 * @Description: com.xuecheng.govern.gateway.filter
 * @Version: 1.0
 */

@Component
public class LoginFilter extends ZuulFilter {

    @Autowired
    ZoolService zoolService;


    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletResponse response = currentContext.getResponse();
        HttpServletRequest request = currentContext.getRequest();

        //检测是有有访问令牌
        String accessToken = zoolService.checkAccessToken(request);
        if(StringUtils.isBlank(accessToken)){
            this.access_denied();
            return null;
        }

        //检测头部是否含有jwt令牌
        String jwtFromHeader = zoolService.checkJwtFromHeader(request);
        if (StringUtils.isBlank(jwtFromHeader)){
            this.access_denied();
            return null;
        }

        //检测redis中令牌是否过期
        long jwtTime = zoolService.getJwtTime(accessToken);
        if(jwtTime<=0){
            this.access_denied();
            return null;
        }
        return null;
    }


    //拒绝访问逻辑
    private void access_denied(){
        RequestContext currentContext = RequestContext.getCurrentContext();
        //未取到Authorization信息
        currentContext.setSendZuulResponse(false);  //拒绝访问
        currentContext.setResponseStatusCode(200);
        ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHENTICATED);
        String jsonString = JSON.toJSONString(responseResult);
        currentContext.setResponseBody(jsonString);
        HttpServletResponse response = currentContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
    }
}
