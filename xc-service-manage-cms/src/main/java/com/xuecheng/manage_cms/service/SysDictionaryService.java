package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_cms.dao.SysDictionaryRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: Shindou
 * @Date: 2019/4/3
 * @Description: com.xuecheng.manage_cms.service
 * @Version: 1.0
 */
@Service
public class SysDictionaryService {

    @Autowired
    SysDictionaryRepository sysDictionaryRepository;

    public SysDictionary findDictionaryByType(String dType){
        if(StringUtils.isEmpty(dType)){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        SysDictionary sysDictionary = sysDictionaryRepository.findByDType(dType);
        return sysDictionary;
    }
}
