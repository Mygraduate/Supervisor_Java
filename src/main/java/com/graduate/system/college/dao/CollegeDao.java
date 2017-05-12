package com.graduate.system.college.dao;


import com.graduate.system.college.model.College;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by konglinghai on 2017/3/19.
 */
@Repository
public interface CollegeDao extends PagingAndSortingRepository<College,Long> {
}
