package com.graduate.common;

import com.graduate.system.arrage.model.Arrage;
import com.graduate.system.arrage.service.ArrageService;
import com.graduate.system.course.dto.ImportDTO;
import com.graduate.system.course.model.Course;
import com.graduate.system.course.service.CourseService;
import com.graduate.system.evaluate.model.Evaluate;
import com.graduate.system.evaluate.service.EvaluateService;
import com.graduate.system.teacher.model.Teacher;
import com.graduate.system.teacher.service.TeacherService;
import com.graduate.system.user.model.User;
import com.graduate.system.user.service.UserService;
import com.graduate.utils.BeanMapper;
import com.graduate.utils.DateUtil;
import com.graduate.utils.excel.ExcelUtils;
import com.graduate.utils.excel.exception.FormatException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
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

    @Autowired
    ArrageService<Arrage> arrageService;

    @Autowired
    UserService<User> userService;

    @Autowired
    TimeService timeService;

    @Autowired
    EvaluateService<Evaluate> evaluateService;

    private final static String EXPROT_ARRAGE_TITLE = "教学督导听课安排表";

    private final static String EXPROT_EVALUATE_TITLE = "教学督导听课反馈表";

    //导入时将覆盖更新原来导入的课程表,返回插入的记录数
    public int importCourseByExcel(Long collegeId, InputStream inputStream) throws Exception {
        List<ImportDTO> data = ExcelUtils.startImport(inputStream, ImportDTO.class);
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
            dto.setDay(String.valueOf(DateUtil.weekDays(StringUtils.substringBefore(dto.getDay(),"("))));
        }

        List<Course> courses = BeanMapper.mapList(data,Course.class);
        for(Course course : courses){
            course.setCid(collegeId);
            course.setTime(timeService.calTimeByWeekAndDay(course.getWeek(),course.getDay()));
        }
        List<Course> mergeList = merge(original,courses);
        courseService.save(mergeList);
        return mergeList.size();
    }
    //合并集合
    private List<Course> merge(List<Course> originals,List<Course> sources){
        for(Course source : sources ){
            for(Course original : originals ){
                if(source.getWeek().equals(original.getWeek())
                        && source.getDay().equals(original.getDay())
                        && source.getScope().equals(original.getScope())
                        && source.getAddress().equals(original.getAddress())){
                    source.setId(original.getId());
                    if (original.getIsArrange()==1){
                        source.setIsArrange(1);
                    }
                    BeanMapper.copy(source,original);
                }
            }
        }
       return  (List<Course>) CollectionUtils.union(originals,sources);
    }

    public String exportArrage(Long cid,String saveName)throws Exception{
        List<Arrage> arrages = arrageService.findAllByCidAndStatus(cid,1);
        List<Double> garde = new ArrayList<>();
        for(Arrage arrage : arrages){
            arrage.setGroups(userService.findAllUserNameByIds(arrage.getGroups().split(",")));
            garde.add(0.0);
        }
        String [] header = new String[]{"序号", "课程", "授课内容", "授课方式", "专业", "教室", "教师", "周次", "听课时间", "听课人员安排","分数"};
        return ExcelUtils.startExport(saveName,arrages,EXPROT_ARRAGE_TITLE,header,garde);
    }

    public String exportEvaluate(Long cid,String saveName)throws Exception{
        List<Arrage> arrages = arrageService.findAllByCidAndStatus(cid,1);
        List<Double> garde = new ArrayList<>();
        for(Arrage arrage : arrages){
            arrage.setGroups(userService.findAllUserNameByIds(arrage.getGroups().split(",")));
            garde.add(evaluateService.calArrageGrade(arrage.getId()));
        }
        String [] header = new String[]{"序号", "课程", "授课内容", "授课方式", "专业", "教室", "教师", "周次", "听课时间", "听课人员安排","分数"};
        return ExcelUtils.startExport(saveName,arrages,EXPROT_EVALUATE_TITLE,header,garde);
    }
}
