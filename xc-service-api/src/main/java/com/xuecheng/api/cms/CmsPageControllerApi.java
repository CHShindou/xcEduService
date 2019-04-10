package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;

/**
 * @Auther : shindou
 * @Date : 2019/3/20
 * @Description : 功能描述  cmspage 各种接口
 * @Version : 1.0
 */
public interface CmsPageControllerApi {

    //页面查询,查询所有页面
    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    //页面新增
    CmsPageResult addPage(CmsPage cmsPage);

    //根据主键ID查询单个页面信息
    CmsPage findById(String pageId);

    //修改页面信息
    CmsPageResult editPage(String pageId,CmsPage cmsPage);

    //删除页面信息
    ResponseResult deletePage(String pageId);

    //发布页面
    ResponseResult postPage(String pageId);

    //新增或者修改页面信息(页面已存在则修改，页面不存在则新增)
    CmsPageResult addOrEditPage(CmsPage cmsPage);

    //一键发布课程页面
    ResponseResult postCoursePageQuik(CmsPage cmsPage);
}
