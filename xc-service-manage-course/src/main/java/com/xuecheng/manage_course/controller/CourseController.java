package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.XcOauth2Util;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: Shindou
 * @Date: 2019/4/2
 * @Description: com.xuecheng.manage_course.controller
 * @Version: 1.0
 */

@RestController
@RequestMapping("/course")
public class CourseController extends BaseController implements CourseControllerApi {

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

        //设置静态companyId测试功能
//        String companyId = "1";
        //从jwt中获取companyId
        XcOauth2Util xcOauth2Util = new XcOauth2Util();
        XcOauth2Util.UserJwt userJwt = xcOauth2Util.getUserJwtFromHeader(request);
        if(userJwt == null){
            ExceptionCast.cast(CommonCode.UNAUTHENTICATED);
        }
        String companyId = userJwt.getCompanyId();
        return courseService.findCourseInfoPageAndParam(companyId,page,size,courseListRequest);
    }

    @Override
    @RequestMapping(value = "/get/{id}")
    public CourseBase findCourseById(@PathVariable("id") String id) {
        return courseService.findCourseBaseById(id);
    }

    @Override
    @RequestMapping(value = "/category/list", method = RequestMethod.GET)
    public CategoryNode findGategoryTreeList() {
        return courseService.findCategoryList();
    }

    @Override
    @RequestMapping(value = "/coursebase/add",method = RequestMethod.POST)
    public ResponseResult addCourseBase(@RequestBody CourseBase courseBase) {
        return courseService.addCourseBase(courseBase);
    }

    @Override
    @RequestMapping(value = "/edit/{courseId}", method = RequestMethod.PUT)
    public ResponseResult editCourseBase(@PathVariable("courseId") String courseId, @RequestBody CourseBase courseBase) {
        return courseService.editCourseBase(courseId,courseBase);
    }

    @Override
    @RequestMapping(value = "/coursepic/add",method = RequestMethod.POST)
    public ResponseResult saveCoursePic(
            @RequestParam("courseId") String courseId, @RequestParam("pic") String pic) {
        return courseService.saveCoursePic(courseId,pic);
    }

    @Override
    @PreAuthorize("hasAuthority('course_find_coursepic')")
    @GetMapping("/coursepic/list/{courseId}")
    public CoursePic findCoursePic(@PathVariable("courseId") String courseId) {
        return courseService.findCoursePic(courseId);
    }

    @Override
    @RequestMapping(value = "/coursepic/delete",method = RequestMethod.DELETE)
    public ResponseResult deleteCoursePic(String courseId) {
        return courseService.deleteCoursePic(courseId);
    }

    @Override
    @RequestMapping(value = "/market/{courseId}",method = RequestMethod.GET)
    public CourseMarket findCourseMarket(@PathVariable("courseId") String courseId) {
        return courseService.findCourseMarketByCourse(courseId);
    }

    @Override
    @RequestMapping(value = "/market/update/{courseId}",method = RequestMethod.POST)
    public ResponseResult updateCourseMarket(@PathVariable("courseId") String courseId,@RequestBody CourseMarket courseMarket) {
        return courseService.updateCourseMarket(courseId,courseMarket);
    }

    @Override
    @RequestMapping(value = "/courseview/{courseId}",method = RequestMethod.GET)
    public CourseView findCourseView(@PathVariable("courseId") String courseId) {
        return courseService.findCourseView(courseId);
    }

    @Override
    @RequestMapping(value = "/preview/{courseId}",method = RequestMethod.POST)
    public CoursePublishResult previewCourse(@PathVariable("courseId") String courseId) {
        return courseService.previewCourse(courseId);
    }

    @Override
    @RequestMapping(value = "/publish/{courseId}",method = RequestMethod.POST)
    public ResponseResult postCourse(@PathVariable("courseId") String courseId) {
        return courseService.postCourse(courseId);
    }

    @Override
    @RequestMapping(value = "/savemedia", method = RequestMethod.POST)
    public ResponseResult addTeachPlanMedia(@RequestBody TeachplanMedia teachplanMedia) {
        return courseService.addTeachPlanMedia(teachplanMedia);
    }
}
