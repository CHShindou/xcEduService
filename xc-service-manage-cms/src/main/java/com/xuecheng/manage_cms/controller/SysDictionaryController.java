package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.SysDictionaryControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.service.SysDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: Shindou
 * @Date: 2019/4/3
 * @Description: com.xuecheng.manage_cms.controller
 * @Version: 1.0
 */

@RestController
@RequestMapping("/sys/dictionary")
public class SysDictionaryController implements SysDictionaryControllerApi {

    @Autowired
    SysDictionaryService sysDictionaryService;

    @Override
    @RequestMapping(value = "/get/{dType}",method = RequestMethod.GET)
    public SysDictionary findByDType(@PathVariable("dType") String dType) {
        return sysDictionaryService.findDictionaryByType(dType);
    }
}
