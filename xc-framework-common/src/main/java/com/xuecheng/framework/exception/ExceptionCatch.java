package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Auther: Shindou
 * @Date: 2019/3/28
 * @Description: com.xuecheng.framework.exception
 * @Version: 1.0
 */

@ControllerAdvice
public class ExceptionCatch {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);

    //ImmutableMap一旦创建，不可改变，并且线程安全。用来存放异常类型和错误代码的映射
    private static ImmutableMap<Class<? extends Throwable>,ResultCode> EXCEPTIONS;

    //使用builder来创建一个异常类型和错误代码的映射
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder =
            ImmutableMap.builder();

    /***
     * 捕获自定义异常
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(CustomException.class)
    public ResponseResult exceptionMessage(CustomException e){
        LOGGER.error("catch exception : {}\r\nexception: ",e.getMessage(), e);
        ResultCode resultCode = e.getResultCode();
        ResponseResult responseResult = new ResponseResult(resultCode);
        return responseResult;
    }


    /***
     * 捕获不可预知异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult unKnowException(Exception e){
        LOGGER.error("catch exception : {}\r\nexception: ",e.getMessage(), e);
        if(EXCEPTIONS == null) {
            EXCEPTIONS = builder.build();
        }
        final ResultCode resultCode = EXCEPTIONS.get(e.getClass());
        final ResponseResult responseResult;
        if(resultCode == null){
            responseResult = new ResponseResult(CommonCode.SERVER_ERROR);
        }else{
            responseResult = new ResponseResult(resultCode);
        }
        return responseResult;
    }


    static{
        //静态代码块,里面存放一些异常和错误代码的映射
        builder.put(HttpMessageNotReadableException.class,CommonCode.INVALID_PARAM);
        builder.put(HttpMediaTypeNotSupportedException.class,CommonCode.INVALID_PARAM);
    }
}
