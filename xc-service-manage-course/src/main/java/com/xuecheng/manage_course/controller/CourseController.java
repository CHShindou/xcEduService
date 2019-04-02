package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: Shindou
 * @Date: 2019/4/2
 * @Description: com.xuecheng.manage_course.controller
 * @Version: 1.0
 */

@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {

    @Autowired
    CourseService courseService;

    @Override
    @RequestMapping(value = "/teachplan/list/{courseId}",method = RequestMethod.GET)
    public TeachplanNode findTeachplanTreeList(@PathVariable("courseId") String courseId) {
        return courseService.findTeachplanTreeList(courseId);
    }

    @Override
    @RequestMapping(value = "/teachplan/add",method = RequestMethod.POST)
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
        return courseService.addTeachplan(teachplan);
    }

    @Override
    @RequestMapping(value = "/coursebase/list/{page}/{size}",method = RequestMethod.GET)
    public QueryResponseResult findCoursePage(
            @PathVariable("page") int page, @PathVariable("size") int size,CourseListRequest courseListRequest) {
        return courseService.findCourseInfoPageAndParam(page,size,courseListRequest);
    }
}
