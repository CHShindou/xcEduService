package com.xuecheng.api.cms;

import com.xuecheng.framework.model.response.QueryResponseResult;

/**
 * @Auther: Shindou
 * @Date: 2019/3/23
 * @Description: com.xuecheng.api.cms
 * @Version: 1.0
 */
public interface CmsSiteControllerApi {

    //查询所有的站点信息，不分页
    QueryResponseResult findSite();
}
