package com.graduate.system.course.service;

import com.graduate.common.BaseDao;
import com.graduate.common.BaseService;
import com.graduate.system.course.dao.CourseDao;
import com.graduate.system.course.model.Course;
import com.graduate.system.sparetime.model.SpareTime;
import com.graduate.system.teacher.dao.TeacherDao;
import com.graduate.system.teacher.model.Teacher;
import com.graduate.system.user.dao.UserDao;
import com.graduate.utils.CourseUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.graduate.utils.CourseUtil.findListByWeek;

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


    public List<Course> findAllByTid(Long tid){
        return mapper.findAllByTid(tid);
    }

    public void updateStautsById(Integer status ,Long id){
        Course course = mapper.findOne(id);
        course.setIsArrange(status);
        mapper.save(course);
    }

    public Course findCourseById(Long id){
        return mapper.findOne(id);
    }

    @Transactional
    public void deleteCourseByTid(List<Teacher> teachers){
        for(Teacher teacher : teachers) {
            Long id = teacher.getId();
            if(id != null){
                mapper.deleteByTid(id);
            }
        }

    }

}
