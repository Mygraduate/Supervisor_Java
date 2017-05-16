package com.graduate.system.teacher.dao;


import com.graduate.common.BaseDao;
import com.graduate.system.teacher.model.Teacher;
import com.graduate.system.user.model.User;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by konglinghai on 2017/3/19.
 */
@Repository
public interface TeacherDao extends BaseDao<Teacher> {
    Teacher findTeacherByname(String name);//根据姓名查找教师信息
    List<Teacher> findTeacherBycid(Long cid);//根据学院查找教师信息
}
