package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: Shindou
 * @Date: 2019/3/20
 * @Description: com.xuecheng.manage_cms.controller
 * @Version: 1.0
 */
@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    CmsPageService cmsPageService;

    @Override
    @RequestMapping(value="/add",method = RequestMethod.POST)
    public CmsPageResult addPage(@RequestBody CmsPage cmsPage) {
        return cmsPageService.addPage(cmsPage);
    }

    @Override
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CmsPage findById(String pageId) {
        return cmsPageService.findById(pageId);
    }

    @Override
    @RequestMapping(value = "/edit/{pageId}",method = RequestMethod.PUT)
    public CmsPageResult editPage(@PathVariable String pageId, @RequestBody CmsPage cmsPage) {
        return cmsPageService.updatePage(pageId,cmsPage);
    }

    @Override
    @RequestMapping(value = "/delete/{pageId}",method = RequestMethod.DELETE)
    public ResponseResult deletePage(@PathVariable String pageId) {
        return cmsPageService.deletePage(pageId);
    }

    @Override
    @RequestMapping(value = "/post/{pageId}",method = RequestMethod.POST)
    public ResponseResult postPage(@PathVariable String pageId) {

        return cmsPageService.postPage(pageId);
    }

    @Override
    @RequestMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {

        /*QueryResult queryResult = new QueryResult();
        queryResult.setTotal(1);
        List<CmsPage> list = new ArrayList<CmsPage>();
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName("一只小绵羊");
        cmsPage.setPageId("24");
        list.add(cmsPage);
        queryResult.setList(list);
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);*/


        return cmsPageService.findCmsPageList(page,size,queryPageRequest);
    }
}
