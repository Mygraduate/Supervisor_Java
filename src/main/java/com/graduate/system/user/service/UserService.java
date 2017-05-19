package com.graduate.system.user.service;

import com.graduate.common.BaseDao;
import com.graduate.common.BaseService;

import com.graduate.system.user.dao.UserDao;
import com.graduate.system.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

/**
 * Created by konglinghai on 2017/3/20.
 */
@Service
public class UserService<T> extends BaseService<T> {

    @Autowired
    private UserDao mapper;

    @Override
    public BaseDao init() {
        return mapper;
    }


    public User findUserByname(String username){

        return mapper.findUserByUsername(username);
    }
    public User findUserByid(Long id){

        return mapper.findUserById(id);
    }

}
