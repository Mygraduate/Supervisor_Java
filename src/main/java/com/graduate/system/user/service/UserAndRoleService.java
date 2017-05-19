package com.graduate.system.user.service;

import com.graduate.common.BaseDao;
import com.graduate.common.BaseService;

import com.graduate.system.user.dao.UserAndRoleDao;
import com.graduate.system.user.model.UserAndRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by konglinghai on 2017/3/20.
 */
@Service
public class UserAndRoleService<T> extends BaseService<T> {

    @Autowired
    private UserAndRoleDao mapper;
    @Override
    public BaseDao init() {
        return mapper;
    }

    public UserAndRole findRoleByUid(Long uid){
        return mapper.findUserAndRoleByUid(uid);
    }

    public List<UserAndRole>  finduserandroleByRoleId (Long roleId){
        return mapper.findUserAndRoleByRoleId(roleId);
    }
}
