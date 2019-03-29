package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Auther: Shindou
 * @Date: 2019/3/29
 * @Description: com.xuecheng.manage_cms.service
 * @Version: 1.0
 */

@Service
public class CmsConfigService {

    @Autowired
    CmsConfigRepository cmsConfigRepository;

    public CmsConfig getModel(String id){
        Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;

    }
}
