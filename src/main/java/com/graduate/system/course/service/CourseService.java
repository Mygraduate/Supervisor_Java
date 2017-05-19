package com.graduate.system.course.service;

import com.graduate.common.BaseDao;
import com.graduate.common.BaseService;
import com.graduate.system.course.dao.CourseDao;
import com.graduate.system.course.model.Course;
import com.graduate.system.sparetime.model.SpareTime;
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

    @Transactional
    public void deleteByTid(Long tid){
        mapper.deleteByTid(tid);
    }

    public List<Course> findAllByTid(Long tid){
        return mapper.findAllByTid(tid);
    }

    public List<SpareTime> autoCreateSpareTime(List<Course> courses,Long cid,Long uid){
       List<SpareTime> times = new ArrayList<>();
       for(int week = 1; week<21 ;week++){
           List<Course> list_week = CourseUtil.findListByWeek(courses,week);
           if(list_week.size()==0){
               CourseUtil.thisWeekSpare(week,times,list_week,cid,uid);
               continue;
           }
           for(int day = 1; day<8;day++){
               List<Course> list_day = CourseUtil.findListByDay(courses,day);
               CourseUtil.thisDaySpare(week,day,times,list_day,cid,uid);
           }
       }
       return times;
    }

}
