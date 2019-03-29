package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Auther: Shindou
 * @Date: 2019/3/29
 * @Description: com.xuecheng.manage_cms.dao
 * @Version: 1.0
 */
public interface CmsConfigRepository extends MongoRepository<CmsConfig,String> {
}
