package com.xuecheng.auth.Controller;

import com.xuecheng.api.auth.AuthControllerApi;
import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Auther : shindou
 * @Date : 2019/4/23
 * @Description : 功能描述
 * @Version : 1.0
 */

@RestController
public class AuthController implements AuthControllerApi {

    @Autowired
    AuthService authService;

    @Value("${auth.clientId}")
    String clientId;

    @Value("${auth.clientSecret}")
    String clientSecret;

    @Value("${auth.cookieDomain}")
    String cookieDomain;

    @Value("${auth.cookieMaxAge}")
    int cookieMaxAge;

    @Value("${auth.tokenValiditySeconds}")
    int tokenValiditySeconds;



    @Override
    @RequestMapping(value = "/userlogin",method = RequestMethod.POST)
    public LoginResult login(LoginRequest loginRequest) {
        if (loginRequest == null || StringUtils.isBlank(loginRequest.getUsername())){
            ExceptionCast.cast(AuthCode.AUTH_USERNAME_NONE);
        }
        if(StringUtils.isBlank(loginRequest.getPassword())){
            ExceptionCast.cast(AuthCode.AUTH_PASSWORD_NONE);
        }
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        AuthToken authToken = authService.login(username, password, clientId, clientSecret);

        //将访问令牌写入cookie中
        saveCookie(authToken.getAccess_token());

        return new LoginResult(CommonCode.SUCCESS,authToken.getAccess_token());
    }

    private void saveCookie(String token){
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        //添加cookie，最后一个参数设置为false  表示允许浏览器获取
        CookieUtil.addCookie(response,cookieDomain,"/","uid",token,cookieMaxAge,false);
    }


    @Override
    @RequestMapping(value = "/userlogout",method = RequestMethod.POST)
    public ResponseResult logout() {
        String accessToken = this.getAccessToken();
        //清空redis中的令牌
        authService.deleteJwtToken(accessToken);

        //删除cookie
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        //将cookie存活时间设置为0
        CookieUtil.addCookie(response,cookieDomain,"/","uid","",0,false);
        return new ResponseResult(CommonCode.SUCCESS);
    }


    /**
     * 从cookie中获取访问令牌  查询Jwt令牌
     * @return
     */
    @Override
    @RequestMapping(value = "/userjwt",method = RequestMethod.GET)
    public JwtResult getUserJwt() {
        //获取访问令牌
        String accessToken = this.getAccessToken();
        if(accessToken == null){
            return new JwtResult(CommonCode.FAIL,null);
        }
        AuthToken authToken = authService.getUserJwtByCookie(accessToken);
        if (authToken == null){
            return new JwtResult(CommonCode.FAIL,null);
        }
        String jwt_token = authToken.getJwt_token();

        return new JwtResult(CommonCode.SUCCESS,jwt_token);
    }

    //从cookie中获取访问令牌
    private String getAccessToken(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String> map = CookieUtil.readCookie(request, "uid");
        if(map != null){
            String accessToken = map.get("uid");
            return accessToken;
        }
        return null;
    }
}
