package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: Shindou
 * @Date: 2019/4/26
 * @Description: com.xuecheng.govern.gateway.filter
 * @Version: 1.0
 */

//@Component
public class LoginFilterTest extends ZuulFilter {


    /**
     * 返回字符串代表过滤器的类型，如下
     * pre：请求在被路由之前执行
     * routing：在路由请求时调用
     * post：在routing和errror过滤器之后调用
     * error：处理请求时发生错误调用
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 此方法返回整型数值，通过此数值来定义过滤器的执行顺序，数字越小优先级越高。
     * @return
     */
    @Override
    public int filterOrder() {
        return 2;
    }

    /**
     * 返回一个Boolean值，判断该过滤器是否需要执行。返回true表示要执行此过虑器，否则不执
     * 行。
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }


    /**
     * 过滤器的业务逻辑。
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletResponse response = currentContext.getResponse();
        HttpServletRequest request = currentContext.getRequest();
        //取出头部信息
        String authorization = request.getHeader("Authorization");
        if(StringUtils.isBlank(authorization)){
            //未取到Authorization信息
            currentContext.setSendZuulResponse(false);  //拒绝访问
            currentContext.setResponseStatusCode(200);
            ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHENTICATED);
            String jsonString = JSON.toJSONString(responseResult);
            currentContext.setResponseBody(jsonString);
            response.setContentType("application/json;charset=UTF-8");
        }
        return null;
    }
}
