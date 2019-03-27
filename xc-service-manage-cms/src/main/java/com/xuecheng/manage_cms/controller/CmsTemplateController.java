package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsTemplateApi;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_cms.service.CmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: Shindou
 * @Date: 2019/3/27
 * @Description: com.xuecheng.manage_cms.controller
 * @Version: 1.0
 */
@RestController
@RequestMapping("/cms/template")
public class CmsTemplateController implements CmsTemplateApi {

    @Autowired
    CmsTemplateService cmsTemplateService;


    @Override
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public QueryResponseResult findTemplate() {
        return cmsTemplateService.findTemplates();
    }
}
