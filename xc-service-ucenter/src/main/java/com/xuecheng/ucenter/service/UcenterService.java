package com.xuecheng.ucenter.service;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.ucenter.dao.XcCompanyUserRepository;
import com.xuecheng.ucenter.dao.XcMenuMapper;
import com.xuecheng.ucenter.dao.XcUserRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther : shindou
 * @Date : 2019/4/24
 * @Description : 功能描述
 * @Version : 1.0
 */

@Service
public class UcenterService {

    @Autowired
    XcUserRepository xcUserRepository;

    @Autowired
    XcMenuMapper xcMenuMapper;

    @Autowired
    XcCompanyUserRepository xcCompanyUserRepository;

    public XcUserExt getUserExt(String username){

        if(StringUtils.isBlank(username)){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }

        XcUser xcUser = xcUserRepository.findByUsername(username);
        if(xcUser == null){
            return null;
        }
        //获取userId
        String userId = xcUser.getId();
        //通过userId获取companyId
        XcCompanyUser xcCompanyUser = xcCompanyUserRepository.findByUserId(userId);

        //获取该用户的权限
        List<XcMenu> xcMenus = xcMenuMapper.selectPermissionByUserId(userId);
        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(xcUser,xcUserExt);
        xcUserExt.setPermissions(xcMenus);
        if(xcCompanyUser != null){
            xcUserExt.setCompanyId(xcCompanyUser.getCompanyId());
        }
        return xcUserExt;
    }
}
