package com.graduate.system.user.service;

import com.graduate.common.BaseDao;
import com.graduate.common.BaseService;

import com.graduate.system.user.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

/**
 * Created by konglinghai on 2017/3/20.
 */
@Service
public class RoleService<T> extends BaseService<T> {
    @Autowired
    private RoleDao mapper;
    @Override
    public BaseDao init() {
        return mapper;
    }


}
