package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
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

    //查询课程信息
    QueryResponseResult findCoursePage(int page, int size, CourseListRequest courseListRequest);
}
