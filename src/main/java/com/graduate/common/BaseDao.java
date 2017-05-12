package com.graduate.common;

import com.graduate.system.user.model.RoleAndMenu;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

/**
 * Created by konglinghai on 2017/5/13.
 */
@NoRepositoryBean
public  interface BaseDao<T> extends PagingAndSortingRepository<T, Long>,JpaSpecificationExecutor<T>{
}
