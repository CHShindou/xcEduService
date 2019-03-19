package com.xuecheng.framework.domain.cms.request;

import com.xuecheng.framework.model.request.RequestData;
import lombok.Data;

/**
 * @Auther : shindou
 * @Date : 2019/3/19
 * @Description : 将查询条件封装（不是很明白为什么要这么麻烦）
 * @Version : 1.0
 */
@Data
public class QueryPageRequest extends RequestData {

    //站点id
    private String siteId;

    //页面id
    private String pageId;

    //页面名称
    private String pageName;

    //别名
    private String pageAliase;

    //模板Id
    private String templateId;


}
