package com.xuecheng.search.controller;

import com.xuecheng.api.search.ESCourseControllerApi;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.search.service.EsCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther : shindou
 * @Date : 2019/4/14
 * @Description : 功能描述
 * @Version : 1.0
 */
@RestController
@RequestMapping("/search/course")
public class ESCourseController implements ESCourseControllerApi {

    @Autowired
    EsCourseService esCourseService;

    @Override
    @RequestMapping(value = "/list/{page}/{size}",method = RequestMethod.GET)
    public QueryResponseResult<CoursePub> courseSearch(@PathVariable("page") int page, @PathVariable("size") int size, CourseSearchParam courseSearchParam) {
        return esCourseService.searchCourse(page,size,courseSearchParam);
    }
}
