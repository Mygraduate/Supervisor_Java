package com.graduate.system.log.dao;


import com.graduate.system.log.model.Log;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by konglinghai on 2017/3/19.
 */
@Repository
public interface LogDao extends PagingAndSortingRepository<Log, Long> {
}
