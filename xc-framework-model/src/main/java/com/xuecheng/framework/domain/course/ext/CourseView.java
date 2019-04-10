package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 课程的详细信息（包括基本信息，图片，营销，课程计划）
 * @Auther: Shindou
 * @Date: 2019/4/10
 * @Description: com.xuecheng.framework.domain.course.ext
 * @Version: 1.0
 */
@Data
@ToString
public class CourseView implements Serializable {

    private CourseBase courseBase;
    private CoursePic coursePic;
    private CourseMarket courseMarket;
    private TeachplanNode teachplanNode;
}
