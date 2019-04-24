package com.xuecheng.auth.client;

import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Auther : shindou
 * @Date : 2019/4/24
 * @Description : 功能描述
 * @Version : 1.0
 */

@FeignClient(value = XcServiceList.XC_SERVICE_UCENTER)
public interface UcenterClient {

    @RequestMapping(value = "/ucenter/getuserext",method = RequestMethod.GET)
    XcUserExt getUserByName(@RequestParam("username") String username);
}
