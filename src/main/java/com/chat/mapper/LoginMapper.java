package com.chat.mapper;

import com.chat.bean.Login;
import org.apache.ibatis.annotations.Param;

public interface LoginMapper {
    //判断登录
    String justLogin(Login login);
    //根据账号查询用户ID
    String lkUseridByUsername(String username);

    Login selectByUserId(@Param("userId")String userId);
}
