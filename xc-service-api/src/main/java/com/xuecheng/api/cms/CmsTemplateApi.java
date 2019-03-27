package com.xuecheng.api.cms;

import com.xuecheng.framework.model.response.QueryResponseResult;

/**
 * @Auther: Shindou
 * @Date: 2019/3/27
 * @Description: com.xuecheng.api.cms  cms_template 各种接口
 * @Version: 1.0
 */
public interface CmsTemplateApi {

    //查询所有template信息
    QueryResponseResult findTemplate();
}
