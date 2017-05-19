package com.graduate.system.college.dao;


import com.graduate.common.BaseDao;
import com.graduate.system.college.model.College;
import com.graduate.system.course.model.Course;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by konglinghai on 2017/3/19.
 */
@Repository
public interface CollegeDao extends BaseDao<College> {
    College findCollegeByid(Long id);//根据id查找学院信息
}
