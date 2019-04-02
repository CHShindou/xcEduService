package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther: Shindou
 * @Date: 2019/4/2
 * @Description: com.xuecheng.manage_course.dao
 * @Version: 1.0
 */
public interface TeachplanRepository extends JpaRepository<Teachplan,String> {
    List<Teachplan> findByCourseidAndParentid(String courseId, String parentId);
}
