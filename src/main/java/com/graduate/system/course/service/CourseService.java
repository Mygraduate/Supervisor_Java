package com.graduate.system.course.service;

import com.graduate.common.BaseDao;
import com.graduate.common.BaseService;
import com.graduate.system.course.dao.CourseDao;
import com.graduate.system.course.model.Course;
import com.graduate.system.user.dao.UserDao;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by konglinghai on 2017/4/30.
 */
@Service
public class CourseService<T> extends BaseService<T> {

    @Autowired
    private CourseDao mapper;

    @Override
    public BaseDao init() {
        return mapper;
    }

    @Transactional
    public void deleteByTid(Long tid){
        mapper.deleteByTid(tid);
    }

    public List<Course> findAllByTid(Long tid){
        return mapper.findAllByTid(tid);
    }

}
