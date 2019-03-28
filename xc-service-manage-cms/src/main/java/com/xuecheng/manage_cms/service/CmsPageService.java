package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

/*
    public CmsPageResult addPage(CmsPage cmspage){
        //检测该页面是否存在.....根据siteId  pageName   pageWebPath作为唯一索引检测
        List<CmsPage> lists =
                cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(
                        cmspage.getPageName(),cmspage.getSiteId(),cmspage.getPageWebPath());
        if(lists.size()>0){
            //页面已经存在
            return new CmsPageResult(CommonCode.FAIL,null);
        }
        //页面不存在,可以新增
        cmspage.setPageId(null);//主键id由mongoDB自动生成
        cmspage = cmsPageRepository.save(cmspage);
        return new CmsPageResult(CommonCode.SUCCESS,cmspage);
    }*/

    /***
     * 测试下异常处理,自定义异常以及不可预知异常
     * 异常处理  主要是反馈给前端
     * @param cmspage
     * @return
     */
    public CmsPageResult addPage(CmsPage cmspage){
        if(cmspage == null){
            //参数不合法，抛出异常

        }

        //检测该页面是否存在.....根据siteId  pageName   pageWebPath作为唯一索引检测
        List<CmsPage> lists =
                cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(
                        cmspage.getPageName(),cmspage.getSiteId(),cmspage.getPageWebPath());
        if(lists.size()>0){
            //页面已经存在,抛出异常
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);

        }
        //页面不存在,可以新增
        cmspage.setPageId(null);//主键id由mongoDB自动生成
        cmspage = cmsPageRepository.save(cmspage);
        return new CmsPageResult(CommonCode.SUCCESS,cmspage);
    }


    //查询单个页面
    public CmsPage findById(String pageId){
        Optional<CmsPage> optionalCmsPage = cmsPageRepository.findById(pageId);
        if(optionalCmsPage.isPresent()){
            return optionalCmsPage.get();
        }
        return null;
    }


    //页面修改
    public CmsPageResult updatePage(String pageId,CmsPage cmsPage){
        CmsPage cmsPage1 = this.findById(pageId);
        if(cmsPage1 != null){
            //更新别名
            cmsPage1.setPageAliase(cmsPage.getPageAliase());
            //更新模板
            cmsPage1.setTemplateId(cmsPage.getTemplateId());
            //更新站点
            cmsPage1.setSiteId(cmsPage.getSiteId());
            //更新页面名称
            cmsPage1.setPageName(cmsPage.getPageName());
            //更新访问路径
            cmsPage1.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            cmsPage1.setPagePhysicalPath(cmsPage.getPagePhysicalPath());

            //执行修改(保存)
            cmsPage1 = cmsPageRepository.save(cmsPage1);
            if(cmsPage1 != null){
                //修改成功
                return new CmsPageResult(CommonCode.SUCCESS,cmsPage1);

            }
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    //删除单个页面
    public ResponseResult deletePage(String pageId){
        //先判断ID是否存在
        CmsPage cmsPage = this.findById(pageId);
        if(cmsPage != null){
            cmsPageRepository.deleteById(pageId);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

}
