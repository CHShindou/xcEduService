package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Auther: Shindou
 * @Date: 2019/3/23
 * @Description: com.xuecheng.manage_cms.dao
 * @Version: 1.0
 */
public interface CmsSiteRepostory extends MongoRepository<CmsSite,String> {
}
