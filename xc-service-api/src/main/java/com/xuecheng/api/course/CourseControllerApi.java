package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;

/**
 * @Auther: Shindou
 * @Date: 2019/4/2
 * @Description: com.xuecheng.api.course
 * @Version: 1.0
 */
public interface CourseControllerApi {

    //查询课程计划
    TeachplanNode findTeachplanTreeList(String courseId);

    //添加课程计划
    ResponseResult addTeachplan(Teachplan teachplan);

    //分类查询所有课程信息
    QueryResponseResult findCoursePage(int page, int size, CourseListRequest courseListRequest);

    //根据课程Id查询单个课程信息
    CourseBase findCourseById(String id);

    //查询所有课程分类信息
    CategoryNode findGategoryTreeList();

    //添加课程
    ResponseResult addCourseBase(CourseBase courseBase);

    //课程修改
    ResponseResult editCourseBase(String id,CourseBase courseBase);

    //保存课程图片
    ResponseResult saveCoursePic(String courseId,String pic);

    //查询课程图片
    CoursePic findCoursePic(String courseId);

    //删除课程图片
    ResponseResult deleteCoursePic(String courseId);

    //查询课程营销信息
    CourseMarket findCourseMarket(String courseId);

    //新增或者更新课程营销信息
    ResponseResult updateCourseMarket(String courseId,CourseMarket courseMarket);

    //课程视图查询（查询课程的所有信息）
    CourseView findCourseView(String courseId);

    //课程预览
    CoursePublishResult previewCourse(String courseId);

    //课程发布
    ResponseResult postCourse(String courseId);

}
