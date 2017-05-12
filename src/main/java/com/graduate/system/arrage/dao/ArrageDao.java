package com.graduate.system.arrage.dao;


import com.graduate.system.arrage.model.Arrage;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by konglinghai on 2017/3/19.
 */
@Repository
public interface ArrageDao extends PagingAndSortingRepository<Arrage,Long> {
}
