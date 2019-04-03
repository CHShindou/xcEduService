package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Auther: Shindou
 * @Date: 2019/4/3
 * @Description: com.xuecheng.manage_cms.dao
 * @Version: 1.0
 */
public interface SysDictionaryRepository extends MongoRepository<SysDictionary,String> {


    //根据dType查询字典
    SysDictionary findByDType(String dType);
}
