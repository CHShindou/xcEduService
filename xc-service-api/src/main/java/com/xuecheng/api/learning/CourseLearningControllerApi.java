package com.xuecheng.api.learning;

import com.xuecheng.framework.domain.learning.response.GetMediaResult;

/**
 * @Auther : shindou
 * @Date : 2019/4/21
 * @Description : 功能描述
 * @Version : 1.0
 */
public interface CourseLearningControllerApi {


    /**
     * 通过teachplanId获取该课程计划的媒资url
     * @param courseId   用于验证用户是否有该课程的权限
     * @param teachplanId   用于搜索teachplanMedia
     * @return
     */
    GetMediaResult getMediaUrl(String courseId,String teachplanId);
}
