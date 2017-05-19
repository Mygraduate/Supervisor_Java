package com.graduate.system.college.service;

import com.graduate.common.BaseDao;
import com.graduate.common.BaseService;
import com.graduate.system.college.dao.CollegeDao;
import com.graduate.system.college.model.College;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/5/15.
 */
@Service
public class CollegeService<T> extends BaseService<T> {
    @Autowired
    private CollegeDao mapper;

    @Override
    public BaseDao init() {
        return mapper;
    }

    public College findCollegeByid(Long id){
        return mapper.findCollegeByid(id);
    }

}
