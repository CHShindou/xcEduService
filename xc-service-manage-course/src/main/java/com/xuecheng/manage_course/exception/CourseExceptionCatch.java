package com.xuecheng.manage_course.exception;

import com.xuecheng.framework.exception.ExceptionCatch;
import com.xuecheng.framework.model.response.CommonCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @Auther : shindou
 * @Date : 2019/4/26
 * @Description : 功能描述
 * @Version : 1.0
 */

@ControllerAdvice
public class CourseExceptionCatch extends ExceptionCatch {

    static {
        builder.put(AccessDeniedException.class, CommonCode.NO_AUTHORISE);
    }
}
