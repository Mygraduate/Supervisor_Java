package com.graduate.system.evaluate.dao;


import com.graduate.system.evaluate.model.Evaluate;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by konglinghai on 2017/3/19.
 */
@Repository
public interface EvaluateDao extends PagingAndSortingRepository<Evaluate,Long> {
}
