package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;

import java.util.List;

/**
 * Created by Administrator.
 */
public interface CourseMapper {

   List<CourseBase> findAllCourseBase();

   CourseBase findCourseBaseById(String id);


   TeachplanNode findTeachPlanTree(String courseId);


   Page<CourseInfo> findCourseInfo(CourseListRequest courseListRequest);

   CategoryNode findCategoryList();

}
