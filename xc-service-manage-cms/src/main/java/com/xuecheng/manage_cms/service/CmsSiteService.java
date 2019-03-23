package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsSiteRepostory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: Shindou
 * @Date: 2019/3/23
 * @Description: com.xuecheng.manage_cms.service
 * @Version: 1.0
 */
@Service
public class CmsSiteService {

    @Autowired
    CmsSiteRepostory cmsSiteRepostory;

    public QueryResponseResult findAllSite(){
        List<CmsSite> lists = cmsSiteRepostory.findAll();
        QueryResult queryResult = new QueryResult();
        queryResult.setList(lists);
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }
}
