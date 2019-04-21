package com.xuecheng.learning.service;

import com.xuecheng.framework.domain.course.TeachplanMediaPub;

import com.xuecheng.framework.domain.learning.response.GetMediaResult;
import com.xuecheng.framework.domain.learning.response.LearningCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.learning.client.MediaSearchClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther : shindou
 * @Date : 2019/4/21
 * @Description : 功能描述
 * @Version : 1.0
 */

@Service
public class LearningCourseService {

    @Autowired
    MediaSearchClient mediaSearchClient;

    public GetMediaResult getMediaUrl(String courseId,String teachplanId){

        if(StringUtils.isBlank(courseId) || StringUtils.isBlank(teachplanId)){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }

        //验证用户是否有该课程的权限


        //通过feign调用搜索服务查询media信息
        TeachplanMediaPub teachplanMediaPub = mediaSearchClient.getMedia(teachplanId);
        if(teachplanMediaPub == null || StringUtils.isEmpty(teachplanMediaPub.getMediaUrl())){
            ExceptionCast.cast(LearningCode.LEARNING_GETMEDIA_ERROR);
        }


        return new GetMediaResult(CommonCode.SUCCESS,teachplanMediaPub.getMediaUrl());
    }
}
