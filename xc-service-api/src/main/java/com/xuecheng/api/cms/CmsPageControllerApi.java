package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;

/**
 * @Auther : shindou
 * @Date : 2019/3/20
 * @Description : 功能描述
 * @Version : 1.0
 */
public interface CmsPageControllerApi {

    //页面查询
    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);


}
