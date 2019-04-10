package com.xuecheng.manage_course.client;

import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Auther: Shindou
 * @Date: 2019/4/10
 * @Description: com.xuecheng.manage_course.client
 * @Version: 1.0
 */

@FeignClient(value = XcServiceList.XC_SERVICE_MANAGE_CMS)
public interface CmsPageClient {

    //调用cms服务中的查询页面信息
    @RequestMapping(value = "/cms/page/get" ,method = RequestMethod.GET)
    CmsPage findById(@RequestParam("pageId") String pageId);

    //调用新增或修改页面信息的接口
    @RequestMapping(value = "/cms/page/save",method = RequestMethod.POST)
    CmsPageResult savePage(@RequestBody CmsPage cmsPage);

    //调用cms一键发布页面接口
    @RequestMapping(value = "/cms/page/postcourse",method = RequestMethod.POST)
    ResponseResult postCoursePage(@RequestBody CmsPage cmsPage);
}
