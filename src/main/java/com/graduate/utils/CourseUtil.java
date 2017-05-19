package com.graduate.utils;

import com.graduate.system.course.model.Course;
import com.graduate.system.sparetime.model.SpareTime;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konglinghai on 2017/5/20.
 */
public class CourseUtil {
    public static List<Course> findListByWeek(List<Course> courses, int week){
        List<Course> results = new ArrayList<>();
        for(Course course : courses){
            if(course.getWeek().equals(week)){
                results.add(course);
            }
        }
        return results;
    }

    public static List<Course> findListByDay(List<Course> courses,int day){
        List<Course> results = new ArrayList<>();
        for(Course course : courses){
            if(course.getDay().equals(day)){
                results.add(course);
            }
        }
        return results;
    }


    public static String formatClass(Course course){
        String info = course.getContent()+"\r"+course.getType()+"\r"+course.getScope()+"\r"+course.getAddress();
        return info;
    }

    public static boolean isSeries(String scopes,int scope){
        int start = Integer.parseInt(StringUtils.substringBefore(scopes,"-"));
        int end = Integer.parseInt(StringUtils.substringAfter(scopes,"-"));
        for(int i=start;i<end+1;i++){
            if(i == scope){
                return true;
            }
        }
        return false;
    }

    public static void thisWeekSpare(int week,List<SpareTime> times,List<Course> courses,Long cid,Long uid){
        for(int day=1;day<8;day++){
            times.add(autoCreateSpareDay(week,day,times,courses,cid,uid));
        }
    }

    public static  void thisDaySpare(int week,int day,List<SpareTime> times,List<Course> courses,Long cid,Long uid){
        times.add(autoCreateSpareDay(week,day,times,courses,cid,uid));
    }

    private static SpareTime autoCreateSpareDay(int week,int day,List<SpareTime> times,List<Course> courses,Long cid,Long uid){
        SpareTime time = new SpareTime();
        time.setCid(cid);
        time.setDay(day);
        time.setUid(uid);
        time.setWeek(week);
        String scopes = "";
        if(courses.size() == 0){
            for(int scope = 1;scope<12;scope++){
                scopes = scopes +","+scope;
            }
            if(StringUtils.isNoneBlank(scopes)){
                time.setScope(scopes.substring(1));
            }
            times.add(time);
        }else{
            for(Course course : courses){
                for(int scope = 1;scope<12;scope++){
                    if(CourseUtil.isSeries(course.getScope(),scope)){
                        scopes = scopes +","+scope;
                    }
                }
                if(StringUtils.isNoneBlank(scopes)){
                    time.setScope(scopes.substring(1));
                }
                times.add(time);
            }
        }

        return time;
    }
}
