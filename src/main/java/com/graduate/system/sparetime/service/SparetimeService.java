package com.graduate.system.sparetime.service;

import com.graduate.common.BaseDao;
import com.graduate.common.BaseService;
import com.graduate.system.course.model.Course;
import com.graduate.system.sparetime.dao.SpareTimeDao;
import com.graduate.system.sparetime.model.SpareTime;
import com.graduate.utils.CourseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */
@Service
public class SparetimeService<T> extends BaseService<T> {
    @Autowired
    private SpareTimeDao mapper;

    @Override
    public BaseDao init(){return mapper;}

    public List<SpareTime> findSpareTimeByuid(Long uid){return mapper.findSpareTimeByuid(uid);}

    public List<SpareTime> findSpareTimeBycid(Long cid){return mapper.findSpareTimeBycid(cid);}

    public List<SpareTime> autoCreateSpareTime(List<Course> courses, Long cid, Long uid, int startWeek, int endWeek){
        List<SpareTime> times = new ArrayList<>();
        for(int week = startWeek; week<endWeek+1 ;week++){
            List<Course> list_week = CourseUtil.findListByWeek(courses,week);
            if(list_week.size()==0){
                CourseUtil.thisWeekSpare(week,times,list_week,cid,uid);
                continue;
            }
            for(int day = 1; day<8;day++){
                List<Course> list_day = CourseUtil.findListByDay(list_week,day);
                CourseUtil.thisDaySpare(week,day,times,list_day,cid,uid);
            }
        }
        return times;
    }}
