package com.graduate.system.course.dao;


import com.graduate.common.BaseDao;
import com.graduate.system.course.model.Course;
import com.graduate.system.teacher.model.Teacher;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by konglinghai on 2017/3/19.
 */

public interface CourseDao extends BaseDao<Course> {


     void deleteByTid(Long tid);

    List<Course> findAllByTid(Long tid);


}
