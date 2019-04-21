package com.xuecheng.learning.client;

import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Auther : shindou
 * @Date : 2019/4/21
 * @Description : 功能描述
 * @Version : 1.0
 */

@FeignClient(value = XcServiceList.XC_SERVICE_SEARCH)
public interface MediaSearchClient {

    //调用EsSearch服务搜索teachplanMedia  by  teachplanId
    @RequestMapping(value = "/search/course/getmedia/{teachplanId}",method = RequestMethod.GET)
    TeachplanMediaPub getMedia(@PathVariable("teachplanId") String teachplanId);

}
