package com.xuecheng.api.search;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;

import java.util.Map;

/**
 * @Auther : shindou
 * @Date : 2019/4/14
 * @Description : 功能描述  课程搜索接口
 * @Version : 1.0
 */
public interface ESCourseControllerApi {

    //搜索所有课程
    QueryResponseResult<CoursePub> courseSearch(int page, int size, CourseSearchParam courseSearchParam);

    //根据课程Id搜索课程详细信息
    Map<String,CoursePub> getCourseInfo(String id);

    //根据课程计划ID搜索对应的媒资url
    TeachplanMediaPub getMedia(String teachplanId);
}
