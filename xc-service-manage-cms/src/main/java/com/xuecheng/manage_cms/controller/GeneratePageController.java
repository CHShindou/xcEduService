package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

/**
 * @Auther: Shindou
 * @Date: 2019/3/30
 * @Description: com.xuecheng.manage_cms.controller
 * @Version: 1.0
 */
@Controller
public class GeneratePageController extends BaseController {

    @Autowired
    CmsPageService cmsPageService;

    @RequestMapping(value = "/cms/page/preview/{pageId}",method = RequestMethod.GET)
    public void preview(@PathVariable String pageId){

        try {
            String htmlContent = cmsPageService.getPageHtml(pageId);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().append(htmlContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
