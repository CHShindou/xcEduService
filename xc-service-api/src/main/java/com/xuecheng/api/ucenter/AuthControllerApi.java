package com.xuecheng.api.ucenter;

import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.model.response.ResponseResult;

/**
 * @Auther : shindou
 * @Date : 2019/4/23
 * @Description : 功能描述   用户认证接口
 * @Version : 1.0
 */


public interface AuthControllerApi {


    //登录
    LoginResult login(LoginRequest loginRequest);

    //退出登录
    ResponseResult logout();
}
