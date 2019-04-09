package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseMarket;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther : shindou
 * @Date : 2019/4/3
 * @Description : 功能描述
 * @Version : 1.0
 */
public interface CourseMarketRepository extends JpaRepository<CourseMarket,String> {

    //CourseMarket的主键ID就是courseId，妈的，这是什么设计
}
