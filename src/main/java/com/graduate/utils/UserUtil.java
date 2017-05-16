package com.graduate.utils;

import com.graduate.system.user.model.User;
import com.graduate.system.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by konglinghai on 2017/5/16.
 *
 * 获取spring Security 当前登录的用户信息
 */
public class UserUtil {


    /**
     *
     * 获取spring Security 当前登录的用户信息
     */
    public static UserDetails getUser(){
        return  (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    /**
     *
     * 获取spring Security 当前登录的用户的账号名
     */
    public static String getUserName(){
        return  ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()).getUsername();

    }
}
