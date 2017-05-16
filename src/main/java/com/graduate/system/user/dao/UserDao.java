package com.graduate.system.user.dao;


import com.graduate.common.BaseDao;
import com.graduate.system.user.model.User;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by konglinghai on 2017/3/19.
 */

public interface  UserDao extends BaseDao<User> {
    User findUserByUsername(String username);//根据账号查找用户信息
}
