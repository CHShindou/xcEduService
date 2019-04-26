package com.xuecheng.govern.gateway.service;

import com.xuecheng.framework.utils.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Auther: Shindou
 * @Date: 2019/4/26
 * @Description: com.xuecheng.govern.gateway.service
 * @Version: 1.0
 */

@Service
public class ZoolService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //查询身份令牌
    public String checkAccessToken(HttpServletRequest request){
        Map<String, String> map = CookieUtil.readCookie(request, "uid");
        String access_token = map.get("uid");
        if(StringUtils.isBlank(access_token)){
            return null;
        }
        return access_token;

    }


    //从header查询jwt令牌
    public String checkJwtFromHeader(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        if(StringUtils.isBlank(authorization) || !authorization.startsWith("Bearer ")){
            return null;
        }
        return authorization;
    }


    //查询redis中令牌的有效期
    public long getJwtTime(String access_token){
        String key = "user_token:" + access_token;
        Long expire = stringRedisTemplate.getExpire(key);
        return expire;
}
}
