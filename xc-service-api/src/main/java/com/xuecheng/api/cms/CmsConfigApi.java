package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;

/**
 * @Auther: Shindou
 * @Date: 2019/3/29
 * @Description: com.xuecheng.api.cms
 * @Version: 1.0
 */
public interface CmsConfigApi {

    //根据id获取cmsConfig里的model信息
    CmsConfig getConfigModel(String id);

}
