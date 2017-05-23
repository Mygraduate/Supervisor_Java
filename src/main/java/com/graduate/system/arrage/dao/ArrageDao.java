package com.graduate.system.arrage.dao;


import com.graduate.common.BaseDao;
import com.graduate.system.arrage.model.Arrage;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by konglinghai on 2017/3/19.
 */
@Repository
public interface ArrageDao extends BaseDao<Arrage> {
    List<Arrage> findAllByCidAndStatus(Long cid, Integer status);
}
