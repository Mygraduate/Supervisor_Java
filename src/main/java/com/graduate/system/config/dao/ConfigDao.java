package com.graduate.system.config.dao;


import com.graduate.system.config.model.Config;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by konglinghai on 2017/3/19.
 */
@Repository
public interface ConfigDao extends PagingAndSortingRepository<Config,Long> {
}
