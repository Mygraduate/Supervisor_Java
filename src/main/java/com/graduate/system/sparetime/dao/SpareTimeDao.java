package com.graduate.system.sparetime.dao;


import com.graduate.system.sparetime.model.SpareTime;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by konglinghai on 2017/3/19.
 */

@Repository
public interface SpareTimeDao extends PagingAndSortingRepository<SpareTime, Long> {
}
