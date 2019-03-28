package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * @Auther: Shindou
 * @Date: 2019/3/28
 * @Description: com.xuecheng.framework.exception  自定义异常类型
 * @Version: 1.0
 */
public class CustomException extends RuntimeException {

    private ResultCode resultCode;

    public CustomException(ResultCode resultCode){
        super("错误代码:"+resultCode.code()+"错误信息:"+resultCode.message());
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode(){
        return this.resultCode;
    }
}
