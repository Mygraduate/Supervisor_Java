package com.graduate.system.evaluate.service;

import com.graduate.common.BaseDao;
import com.graduate.common.BaseService;
import com.graduate.system.course.dao.CourseDao;
import com.graduate.system.course.model.Course;
import com.graduate.system.evaluate.dao.EvaluateDao;
import com.graduate.system.evaluate.model.Evaluate;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by konglinghai on 2017/4/30.
 */
@Service
public class EvaluateService<T> extends BaseService<T> {

    @Autowired
    private EvaluateDao mapper;

    @Override
    public BaseDao init() {
        return mapper;
    }

    public List<Evaluate> findEvaluateByArrageId(Long arrageId){
        return mapper.findEvaluateByArrageId(arrageId);
    }

}
