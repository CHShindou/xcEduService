package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.CourseMapper;
import com.xuecheng.manage_course.dao.CoursePicRepository;
import com.xuecheng.manage_course.dao.TeachplanRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @Auther: Shindou
 * @Date: 2019/4/2
 * @Description: com.xuecheng.manage_course.service
 * @Version: 1.0
 */
@Service
public class CourseService {

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    TeachplanRepository teachplanRepository;

    @Autowired
    CourseBaseRepository courseBaseRepository;

    @Autowired
    CoursePicRepository coursePicRepository;


    //查询课程计划
    public TeachplanNode findTeachplanTreeList(String courseId){
        TeachplanNode teachPlanTree = courseMapper.findTeachPlanTree(courseId);
        return teachPlanTree;
    }


    //添加课程计划
    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan){
        if(teachplan == null ||
                StringUtils.isEmpty(teachplan.getCourseid()) ||
                StringUtils.isEmpty(teachplan.getPname())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //获取parentId
        String parentId = teachplan.getParentid();
        if(StringUtils.isEmpty(parentId)){
            //根节点未填，获取根节点
            parentId = this.getTeachplanRoot(teachplan.getCourseid());
        }
        //获取根节点的信息
        Optional<Teachplan> optional = teachplanRepository.findById(parentId);
        if(!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Teachplan parent = optional.get();
        teachplan.setParentid(parent.getId());
        if("1".equals(parent.getGrade())){
            teachplan.setGrade("2");
        }else if("2".equals(parent.getGrade())){
            teachplan.setGrade("3");
        }
        //保存
        teachplanRepository.save(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //添加课程计划时，根节点未填，获取根节点
    private String getTeachplanRoot(String courseId){
        //通过courseId获取课程的信息
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()){
            return null;
        }
        CourseBase courseBase = optional.get();
        //用户未填parentId,设置为0
        String parentId = "0";
        //通过courseId查询课程计划
        List<Teachplan> lists =
                teachplanRepository.findByCourseidAndParentid(courseId,parentId);
        if(lists == null || lists.size()<=0){
            //该课程未添加入课程计划，要将该课程当做根节点添加入课程计划
            Teachplan teachplanRoot = new Teachplan();
            teachplanRoot.setCourseid(courseId);
            teachplanRoot.setPname(courseBase.getName());
            teachplanRoot.setParentid("0");
            teachplanRoot.setGrade("1");
            teachplanRoot.setStatus("0");

            teachplanRepository.save(teachplanRoot);
            return teachplanRoot.getId();
        }
        //获取到了根节点信息,返回根节点ID
        return lists.get(0).getId();
    }

    //查询课程信息，分页查询
    public QueryResponseResult findCourseInfoPageAndParam(
           int page, int size, CourseListRequest courseListRequest){

        //courseListRequest搜索参数吧。
        PageHelper.startPage(page,size);
        Page<CourseInfo> pages = courseMapper.findCourseInfo(courseListRequest);
        QueryResult queryResult = new QueryResult();
        queryResult.setList(pages.getResult());
        queryResult.setTotal(pages.getTotal());
        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);

    }

    //查询单个课程详细信息
    public CourseBase findCourseBaseById(String id){
        if(StringUtils.isEmpty(id)){
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        Optional<CourseBase> optional = courseBaseRepository.findById(id);
        if(!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        CourseBase courseBase = optional.get();
        return courseBase;
    }


    //查询课程分类信息
    public CategoryNode findCategoryList(){
        return courseMapper.findCategoryList();
    }

    //添加课程信息
    public ResponseResult addCourseBase(CourseBase courseBase){
        if(courseBase == null ||
                StringUtils.isEmpty(courseBase.getName()) ||
                StringUtils.isEmpty(courseBase.getGrade()) ||
                StringUtils.isEmpty(courseBase.getStudymodel())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //新增的课程设置为制作中
        courseBase.setStatus("202001");
        courseBaseRepository.save(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);

    }

    //修改课程信息
    @Transactional
    public ResponseResult editCourseBase(String courseId,CourseBase courseBase){
        if(courseBase == null ||
                StringUtils.isEmpty(courseId) ||
                StringUtils.isEmpty(courseBase.getName()) ||
                StringUtils.isEmpty(courseBase.getGrade()) ||
                StringUtils.isEmpty(courseBase.getStudymodel())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //通过课程ID查询原本的数据
        CourseBase old = this.findCourseBaseById(courseId);
        if(old == null){
            return new ResponseResult(CommonCode.FAIL);
        }
        old.setName(courseBase.getName());
        old.setUsers(courseBase.getUsers());
        old.setMt(courseBase.getMt());
        old.setSt(courseBase.getSt());
        old.setGrade(courseBase.getGrade());
        old.setStudymodel(courseBase.getStudymodel());
        old.setDescription(courseBase.getDescription());
        courseBaseRepository.save(old);
        return new ResponseResult(CommonCode.SUCCESS);
    }


    //保存课程图片
    public ResponseResult saveCoursePic(String courseId,String pic){
        if(StringUtils.isEmpty(courseId) || StringUtils.isEmpty(pic)){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        CoursePic coursePic = new CoursePic();
        //个人认为这里不需要判断之前该课程是否存在图片。
//        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
//        if(optional.isPresent()){
//
//        }
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //查询课程图片
    public CoursePic findCoursePic(String courseId){
        if(StringUtils.isEmpty(courseId)){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }


    //删除课程图片
    public ResponseResult deleteCoursePic(String courseId){
        if(StringUtils.isEmpty(courseId)){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }

        //如果删除失败会报异常
        coursePicRepository.deleteById(courseId);
        return new ResponseResult(CommonCode.SUCCESS);

    }



}
