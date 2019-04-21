package com.xuecheng.framework.domain.learning.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Auther : shindou
 * @Date : 2019/4/21
 * @Description : 功能描述
 * @Version : 1.0
 */
@Data
@ToString
@NoArgsConstructor
public class GetMediaResult extends ResponseResult {

    //媒资文件url
    private String fileUrl;

    public GetMediaResult(ResultCode resultCode,String fileUrl){
        super(resultCode);
        this.fileUrl = fileUrl;
    }
}
