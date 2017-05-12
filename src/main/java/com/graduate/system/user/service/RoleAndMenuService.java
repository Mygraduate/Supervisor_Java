package com.graduate.system.user.service;


import com.graduate.common.BaseDao;
import com.graduate.common.BaseService;
import com.graduate.system.user.dao.RoleAndMenuDao;
import com.graduate.system.user.model.RoleAndMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by konglinghai on 2017/3/20.
 */
@Service
public class RoleAndMenuService<T> extends BaseService<T> {

    @Autowired
    private RoleAndMenuDao mapper;

    @Override
    public BaseDao init() {
        return mapper;
    }

    public List<RoleAndMenu> findMenuByRoleId(Long roleId){
        return mapper.findRoleAndMenuByRoleId(roleId);
    }
}
