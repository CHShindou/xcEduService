package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDao {
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    CourseMapper courseMapper;
    @Test
    public void testCourseBaseRepository(){
        Optional<CourseBase> optional = courseBaseRepository.findById("402885816240d276016240f7e5000002");
        if(optional.isPresent()){
            CourseBase courseBase = optional.get();
            System.out.println(courseBase);
        }

    }

    @Test
    public void testCourseMapper(){
        CourseBase courseBase = courseMapper.findCourseBaseById("402885816240d276016240f7e5000002");
        System.out.println(courseBase);

    }


    //测试查询课程计划
    @Test
    public void testFindTeachplanTree(){
        TeachplanNode teachplanNode = courseMapper.findTeachPlanTree("4028e581617f945f01617f9dabc40000");
        System.out.println(teachplanNode);
    }


    @Test
    public void testPageHelper(){
        PageHelper.startPage(1,5);
        List<CourseBase> lists = courseMapper.findAllCourseBase();
        System.out.println(lists);
    }


    @Test
    public void testPageCourse(){
        PageHelper.startPage(1,5);
        Page<CourseInfo> pages = courseMapper.findCourseInfo(null);
        System.out.println(pages);
    }
}
