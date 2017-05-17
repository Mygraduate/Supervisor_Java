package com.graduate.system.user.dao;


import com.graduate.common.BaseDao;
import com.graduate.system.user.model.UserAndRole;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by konglinghai on 2017/3/19.
 */

public interface UserAndRoleDao extends BaseDao<UserAndRole> {

    UserAndRole findUserAndRoleByUid(Long uid);//根据用户id查询对应的角色
    List<UserAndRole>  findUserAndRoleByRoleId(Long roleId);//根据角色id查询用户
}
