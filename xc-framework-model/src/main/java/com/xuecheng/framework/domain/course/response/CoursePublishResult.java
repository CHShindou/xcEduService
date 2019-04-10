package com.xuecheng.framework.domain.course.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 页面预览的响应模型
 * @Auther: Shindou
 * @Date: 2019/4/10
 * @Description: com.xuecheng.framework.domain.course.response
 * @Version: 1.0
 */
@Data
@ToString
@NoArgsConstructor   //无参构造方法
public class CoursePublishResult extends ResponseResult {
    private String previewUrl;

    public CoursePublishResult(ResultCode resultCode,String previewUrl){
        super(resultCode);
        this.previewUrl = previewUrl;
    }

}
