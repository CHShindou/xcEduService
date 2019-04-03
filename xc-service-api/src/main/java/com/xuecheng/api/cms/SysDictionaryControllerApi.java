package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.system.SysDictionary;

/**
 * @Auther: Shindou
 * @Date: 2019/4/3
 * @Description: com.xuecheng.api.cms
 * @Version: 1.0
 */
public interface SysDictionaryControllerApi {

    //根据数据字典的Type类型查询数据字典
    SysDictionary findByDType(String dType);
}
