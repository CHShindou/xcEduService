package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsConfigApi;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.service.CmsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: Shindou
 * @Date: 2019/3/29
 * @Description: com.xuecheng.manage_cms.controller
 * @Version: 1.0
 */
@RestController
@RequestMapping("/cms/config")
public class CmsConfigController implements CmsConfigApi {

    @Autowired
    CmsConfigService cmsConfigService;

    @Override
    @GetMapping("/getmodel/{id}")
    public CmsConfig getConfigModel(@PathVariable String id) {

        return cmsConfigService.getModel(id);

    }
}
