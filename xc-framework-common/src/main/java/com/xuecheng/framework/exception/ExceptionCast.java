package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * @Auther: Shindou
 * @Date: 2019/3/28
 * @Description: com.xuecheng.framework.exception  自定义异常抛出类
 * @Version: 1.0
 */
public class ExceptionCast {

    public static void cast(ResultCode resultCode){
        throw new CustomException(resultCode);
    }
}
