package com.xuecheng.api.search;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;

/**
 * @Auther : shindou
 * @Date : 2019/4/14
 * @Description : 功能描述  课程搜索接口
 * @Version : 1.0
 */
public interface ESCourseControllerApi {

    QueryResponseResult<CoursePub> courseSearch(int page, int size, CourseSearchParam courseSearchParam);
}
