package com.graduate.system.user.dao;


import com.graduate.common.BaseDao;
import com.graduate.system.user.model.RoleAndMenu;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by konglinghai on 2017/3/19.
 */

public interface RoleAndMenuDao extends BaseDao<RoleAndMenu>{

    List<RoleAndMenu> findRoleAndMenuByRoleId(Long roleId);
}
