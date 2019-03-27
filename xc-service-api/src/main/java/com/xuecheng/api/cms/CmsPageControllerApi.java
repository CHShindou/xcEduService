package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;

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
}
