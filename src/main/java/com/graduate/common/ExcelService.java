package com.graduate.common;

import com.graduate.system.course.dto.ImportDTO;
import com.graduate.system.course.model.Course;
import com.graduate.system.course.service.CourseService;
import com.graduate.system.teacher.model.Teacher;
import com.graduate.system.teacher.service.TeacherService;
import com.graduate.utils.BeanMapper;
import com.graduate.utils.excel.ExcelUtils;
import com.graduate.utils.excel.exception.FormatException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static com.graduate.utils.excel.ExcelUtils.startImport;
import static org.apache.commons.collections.CollectionUtils.union;

/**
 * Created by konglinghai on 2017/5/11.
 */
@Service
public class ExcelService {
    @Autowired
    CourseService<Course> courseService;

    @Autowired
    TeacherService<Teacher> teacherService;

    //导入时将覆盖更新原来导入的课程表
    public void importCourseByExcel(Long collegeId, String path) throws Exception {
        List<ImportDTO> data = ExcelUtils.startImport(path, ImportDTO.class);
        HashMap<String,Long> teacherMap = new HashMap<>();
        List<Course> original = new ArrayList<>();
        for(ImportDTO dto : data){
            teacherMap.put(dto.getTeacher(),null);
        }
        for(String name : teacherMap.keySet()){
            Teacher exists = teacherService.findTeacherByname(StringUtils.substringBefore(name,"("));

            if(exists != null ){
                teacherMap.put(name,exists.getId());
                original = courseService.findAllByTid(exists.getId());
            }else{
                Teacher teacher = new Teacher();
                teacher.setCid(collegeId);
                teacher.setName(StringUtils.substringBefore(name,"("));
                teacher.setTitle(StringUtils.substringBetween(name,"(",")"));
                teacherService.save(teacher);
                teacherMap.put(name,teacher.getId());
            }
        }

        for(ImportDTO dto : data){
            dto.setTid(teacherMap.get(dto.getTeacher()));
            dto.setDay(covert2number(StringUtils.substringBefore(dto.getDay(),"(")));
        }

        List<Course> courses = BeanMapper.mapList(data,Course.class);
        for(Course course : courses){
            course.setCollegeId(collegeId);
        }

        courseService.save(merge(original,courses));
    }

    private String covert2number(String day){
        switch (day){
            case "一":
                return "1";
            case "二":
                return "2";
            case "三":
                return "3";
            case "四":
                return "4";
            case "五":
                return "5";
            case "六":
                return "6";
            case "日":
                return "7";
            default:
                return "0";
        }
    }

    private List<Course> merge(List<Course> originals,List<Course> sources){
        for(Course source : sources ){
            for(Course original : originals ){
                if(source.getWeek().equals(original.getWeek())
                        && source.getDay().equals(original.getDay())
                        && source.getScope().equals(original.getScope())
                        && source.getAddress().equals(original.getAddress())){
                    source.setId(original.getId());
                    BeanMapper.copy(source,original);
                }
            }
        }
       return  (List<Course>) CollectionUtils.union(originals,sources);
    }
}
