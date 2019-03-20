package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @Auther: Shindou
 * @Date: 2019/3/20
 * @Description: com.xuecheng.manage_cms.dao
 * @Version: 1.0
 */

public interface CmsPageRepository extends MongoRepository<CmsPage,String> {

   List<CmsPage> findByPageAliase(String pageAliase);
}
