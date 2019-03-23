package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

/**
 * @Auther : shindou
 * @Date : 2019/3/20
 * @Description : 功能描述
 * @Version : 1.0
 */
@Service
public class CmsPageService {

    @Autowired
    CmsPageRepository cmsPageRepository;

    public QueryResponseResult findCmsPageList(int page, int size, QueryPageRequest queryPageRequest){
        if(page <= 0){
            page = 1;
        }
        if(queryPageRequest == null){
            queryPageRequest = new QueryPageRequest();
        }
        //Pageable类中，page=0表示第一页，所以要将传入的参数page-1操作
        page -= 1;
        //Pageable 分页对象
        Pageable pageable = PageRequest.of(page,size);

        //创建查询条件容器  并赋值
        CmsPage cmsPage = new CmsPage();
        //赋值站点ID
        if(StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //模板id
        if(StringUtils.isNotEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        //页面别名
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        //创建查询规则、匹配方式：站点ID和模板ID是精确查询（默认，不需要设置）,页面别名是模糊查询
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher =
                exampleMatcher.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());

        //创建查询条件实例
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);

        Page<CmsPage> pages = cmsPageRepository.findAll(example,pageable);
        QueryResult queryResult = new QueryResult();
        queryResult.setList(pages.getContent());
        queryResult.setTotal(pages.getTotalElements());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;

    }

}
