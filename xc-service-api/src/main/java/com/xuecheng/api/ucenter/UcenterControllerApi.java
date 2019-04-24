package com.xuecheng.api.ucenter;

import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;

/**
 * @Auther : shindou
 * @Date : 2019/4/24
 * @Description : 功能描述  用户中心接口——提供用户信息的增删改查
 * @Version : 1.0
 */
public interface UcenterControllerApi {

    //根据用户名查询用户信息
    XcUserExt getUserExt(String username);
}
