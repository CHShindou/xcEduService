package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther : shindou
 * @Date : 2019/4/24
 * @Description : 功能描述
 * @Version : 1.0
 */
public interface XcCompanyUserRepository extends JpaRepository<XcCompanyUser,String> {

    XcCompanyUser findByUserId(String userId);
}
