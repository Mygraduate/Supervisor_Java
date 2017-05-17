package com.graduate.system.teacher.service;

import com.graduate.common.BaseDao;
import com.graduate.common.BaseService;
import com.graduate.system.course.dao.CourseDao;
import com.graduate.system.teacher.dao.TeacherDao;
import com.graduate.system.teacher.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by konglinghai on 2017/5/11.
 */
@Service
public class TeacherService<T> extends BaseService<T> {
    @Autowired
    private TeacherDao mapper;

    @Override
    public BaseDao init() {
        return mapper;
    }

    public Teacher findTeacherByname(String name){
        return mapper.findTeacherByname(name);
    }

    public Teacher findTeacherByid(Long id){
        return mapper.findTeacherByid(id);
    }

    public List<Teacher> findTeacherBycid(Long cid){
        return mapper.findTeacherBycid(cid);
    }

}
