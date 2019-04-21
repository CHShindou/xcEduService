package com.xuecheng.learning.controller;

import com.xuecheng.api.learning.CourseLearningControllerApi;
import com.xuecheng.framework.domain.learning.response.GetMediaResult;
import com.xuecheng.learning.service.LearningCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther : shindou
 * @Date : 2019/4/21
 * @Description : 功能描述
 * @Version : 1.0
 */

@RestController
@RequestMapping("/learning/course")
public class CourseLearningController implements CourseLearningControllerApi {

    @Autowired
    LearningCourseService learningCourseService;


    @Override
    @RequestMapping(value = "/getmedia/{courseId}/{teachplanId}",method = RequestMethod.GET)
    public GetMediaResult getMediaUrl(@PathVariable("courseId") String courseId,@PathVariable("teachplanId") String teachplanId) {
        return learningCourseService.getMediaUrl(courseId,teachplanId);
    }
}
