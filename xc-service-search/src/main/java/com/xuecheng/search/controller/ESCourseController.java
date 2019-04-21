package com.xuecheng.search.controller;

import com.xuecheng.api.search.ESCourseControllerApi;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.search.service.EsCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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

    @Override
    @RequestMapping(value = "/getall/{id}",method = RequestMethod.GET)
    public Map<String, CoursePub> getCourseInfo(@PathVariable("id") String id) {
        return esCourseService.termById(id);
    }

    @Override
    @RequestMapping(value = "/getmedia/{teachplanId}",method = RequestMethod.GET)
    public TeachplanMediaPub getMedia(@PathVariable("teachplanId") String teachplanId) {
        String[] ids = new String[]{teachplanId};
        QueryResponseResult<TeachplanMediaPub> teachplanMediaPubs = esCourseService.searchMediaByIds(ids);
        QueryResult<TeachplanMediaPub> queryResult = teachplanMediaPubs.getQueryResult();
        if(queryResult != null && queryResult.getList() != null && queryResult.getList().size()>0){
            return queryResult.getList().get(0);
        }
        return new TeachplanMediaPub();
    }
}
