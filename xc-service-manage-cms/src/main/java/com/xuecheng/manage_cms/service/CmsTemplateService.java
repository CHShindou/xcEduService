package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: Shindou
 * @Date: 2019/3/27
 * @Description: com.xuecheng.manage_cms.service
 * @Version: 1.0
 */
@Service
public class CmsTemplateService {

    @Autowired
    CmsTemplateRepository cmsTemplateRepository;

    public QueryResponseResult findTemplates(){
       List<CmsTemplate> templates = cmsTemplateRepository.findAll();
       QueryResult queryResult = new QueryResult();
       queryResult.setList(templates);
       return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }
}
