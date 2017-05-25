package com.graduate.system.evaluate.dao;


import com.graduate.common.BaseDao;
import com.graduate.common.BaseService;
import com.graduate.system.evaluate.model.Evaluate;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by konglinghai on 2017/3/19.
 */
@Repository
public interface EvaluateDao extends BaseDao<Evaluate> {

    List<Evaluate> findEvaluateByArrageId(Long arrageId);
}
