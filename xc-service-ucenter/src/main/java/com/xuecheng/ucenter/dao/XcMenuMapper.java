package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Auther : shindou
 * @Date : 2019/5/1
 * @Description : 功能描述
 * @Version : 1.0
 */

//@Mapper
public interface XcMenuMapper {

    List<XcMenu> selectPermissionByUserId(String userId);
}
