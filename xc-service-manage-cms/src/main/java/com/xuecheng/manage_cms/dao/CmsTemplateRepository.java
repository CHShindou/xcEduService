package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Auther: Shindou
 * @Date: 2019/3/27
 * @Description: com.xuecheng.manage_cms.dao
 * @Version: 1.0
 */
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String> {
}
