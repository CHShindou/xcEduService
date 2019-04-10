package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    @Autowired
    RabbitTemplate rabbitTemplate;

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
            //更新dataUrl
            cmsPage1.setDataUrl(cmsPage.getDataUrl());

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

    //页面预览，根据模板和数据生成一个静态化页面
    public String getPageHtml(String pageId){
        //获取数据模型
        Map model = this.getModelByPageId(pageId);
        //获取模板信息
        String content = this.getTemplateByPageId(pageId);
        //页面静态化
        String htmlContent = this.generateHtml(content,model);
        if(StringUtils.isEmpty(htmlContent)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }

        return htmlContent;
    }

    //页面静态化
    private String generateHtml(String template,Map model){

        try {
            //生成配置类
            Configuration configuration = new Configuration(Configuration.getVersion());
            //模板加载器
            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            stringTemplateLoader.putTemplate("template",template);
            configuration.setTemplateLoader(stringTemplateLoader);
            //获取模板
            Template template1 = configuration.getTemplate("template","UTF-8");
            //执行静态化
            String htmlFile = FreeMarkerTemplateUtils.processTemplateIntoString(template1,model);
            return htmlFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //根据pageId获取数据模型
    private Map getModelByPageId(String pageId){
        //获取页面信息
        CmsPage cmsPage = this.findById(pageId);
        if(cmsPage == null){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //获取dataUrl
        String dataUrl = cmsPage.getDataUrl();
        if(StringUtils.isEmpty(dataUrl)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return body;
    }

    //根据pageId获取模板信息
    private String getTemplateByPageId(String pageId){
        //获取页面信息
        CmsPage cmsPage = this.findById(pageId);
        if(cmsPage == null){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //获取模板Id
        String templateId = cmsPage.getTemplateId();
        if(StringUtils.isEmpty(templateId)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //通过模板ID获取模板信息
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if(optional.isPresent()){
            CmsTemplate cmsTemplate = optional.get();
            //拿到templateFileId
            String fileId = cmsTemplate.getTemplateFileId();
            //通过fileId查询到文件
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
            //输出流
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //获取流对象
            GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
            //获取流中的数据
            try {
               String s = IOUtils.toString(gridFsResource.getInputStream(),"UTF-8");
               return s;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //页面发布：将静态页面存储到GridFS文件系统中，并发布消息给MQ
    public ResponseResult postPage(String pageId){
        if(StringUtils.isEmpty(pageId)){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //执行页面静态化
        String html = this.getPageHtml(pageId);
        if(StringUtils.isEmpty(html)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        //将静态页面存储到GridFS中
        CmsPage cmsPage = this.saveHtmlContent(pageId,html);
        //发送消息到rabbitmq
        this.sendPostPage(pageId);
        return new ResponseResult(CommonCode.SUCCESS);

    }

    //将静态页面存储到GridFS中，并更新cmsPage
    private CmsPage saveHtmlContent(String pageId,String html){
        CmsPage cmsPage = this.findById(pageId);
        if(cmsPage == null){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        InputStream inputStream = null;
        try {
            inputStream = IOUtils.toInputStream(html,"UTF-8");
            ObjectId objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());

            String htmlFileId = objectId.toHexString();
            cmsPage.setHtmlFileId(htmlFileId);
            cmsPage = cmsPageRepository.save(cmsPage);
            return cmsPage;
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //发送消息给mq
    private void sendPostPage(String pageId){
        CmsPage cmsPage = this.findById(pageId);
        if(cmsPage == null){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        Map<String,String> map = new HashMap<>();
        map.put("pageId",pageId);
        String msg = JSON.toJSONString(map);
        //获取站点ID当做routingkey
        String siteId = cmsPage.getSiteId();
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_CMS_POSTPAGE,siteId,msg);
    }


    //新增或者修改页面信息
    public CmsPageResult savePage(CmsPage cmsPage){
        if(cmsPage == null){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        List<CmsPage> list = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(
                cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if(list.size()>0){
            //该页面已经存在
            return this.updatePage(list.get(0).getPageId(),cmsPage);
        }else{
            //页面不存在
            return this.addPage(cmsPage);
        }
    }

}
