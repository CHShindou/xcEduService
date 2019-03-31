package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Auther : shindou
 * @Date : 2019/3/31
 * @Description : 功能描述
 * @Version : 1.0
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {
}
