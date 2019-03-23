package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Auther: Shindou
 * @Date: 2019/3/20
 * @Description: com.xuecheng.manage_cms.dao
 * @Version: 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest{

    @Autowired
    CmsPageRepository cmsPageRepository;

    @Test
    public void testFindPage(){
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> pages = cmsPageRepository.findAll(pageable);
        System.out.println(pages);
    }

    @Test
    public void testFindByPageAliase(){
        List<CmsPage> lists = cmsPageRepository.findByPageAliase("课程详情页面");
        System.out.println(lists.size());
    }

    //测试按条件查询
    @Test
    public void testFindByParams(){
        //条件值容器
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageAliase("页面");
        //创建条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        //设置匹配方式（模糊查询，精确查询.....默认精确查询）
        //这里意思是pageAliase这个字段是模糊查询，其他的是精确查询
        exampleMatcher =
                exampleMatcher.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        //创建查询条件实例
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);
        //设置分页参数
        Pageable pageable = PageRequest.of(1,10);

        Page<CmsPage> pages = cmsPageRepository.findAll(example,pageable);
        System.out.println(pages);
    }
}
