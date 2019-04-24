package com.xuecheng.ucenter.Controller;

import com.xuecheng.api.ucenter.UcenterControllerApi;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.service.UcenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther : shindou
 * @Date : 2019/4/24
 * @Description : 功能描述
 * @Version : 1.0
 */
@RestController
@RequestMapping("/ucenter")
public class UcenterController implements UcenterControllerApi {

    @Autowired
    UcenterService ucenterService;

    @Override
    @RequestMapping(value = "/getuserext",method = RequestMethod.GET)
    public XcUserExt getUserExt(@RequestParam("username") String username) {
        return ucenterService.getUserExt(username);
    }
}
