package com.graduate.common;

import com.alibaba.fastjson.JSONObject;
import com.graduate.system.arrage.model.Arrage;
import com.graduate.system.arrage.service.ArrageService;
import com.graduate.system.course.model.Course;
import com.graduate.system.course.service.CourseService;
import com.graduate.system.user.model.User;
import com.graduate.system.user.service.UserService;
import com.graduate.utils.word.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by konglinghai on 2017/5/16.
 */
@Service
public class WordService {
    @Autowired
    CourseService<Course> courseService;

    @Autowired
    UserService<User> userService;

    public String exportClassPlan(HashMap<String,Object> searchVals,String saveName) throws Exception {
        HashMap sortVals =  new HashMap<String, String>();
        sortVals.put("week","ASC");
        List<Course> courses = courseService.findAll(searchVals,sortVals);
        String wordFile = WordUtils.exportClassPlan(courses,saveName);
        return wordFile;
    }

    public String fillChiefSuparvisorFile(Arrage arrage, JSONObject object,String saveName) throws Exception{
        HashMap<String,String> data = new HashMap<>();
            data.put("teacher",arrage.getTeacher().getName());
            data.put("title",arrage.getTeacher().getTitle());
            data.put("time",arrage.getCourse().getTime());
            data.put("address",arrage.getCourse().getAddress());
            data.put("class",arrage.getCourse().getName());
            data.put("context",arrage.getCourse().getContent());
            data.put("grade",object.getString("chief0"));
            data.put("groups",userService.findAllUserNameByIds(arrage.getGroups().split(",")));
            data.put("summar",object.getString("chief1"));
        String wordFile = WordUtils.fillChief(data,saveName);
        return wordFile;
    }

    public String fillNormalSuparvisorFile(Arrage arrage, JSONObject object,String saveName,String userId,int total)throws Exception{
        HashMap<String,String> data = new HashMap<>();
        data.put("teacher",arrage.getTeacher().getName());
        data.put("time",arrage.getCourse().getTime());
        data.put("address",arrage.getCourse().getAddress());
        data.put("class",arrage.getCourse().getGrade());
        data.put("name",arrage.getCourse().getName());
        data.put("content",arrage.getCourse().getContent());
        data.put("{0}",object.getString("normal0"));
        data.put("{1}",object.getString("normal1"));
        data.put("{2}",object.getString("normal2"));
        data.put("{3}",object.getString("normal3"));
        data.put("{4}",object.getString("normal4"));
        data.put("{5}",object.getString("normal5"));
        data.put("{6}",object.getString("normal6"));
        data.put("{7}",object.getString("normal7"));
        data.put("summar",object.getString("normal8"));
        data.put("total", String.valueOf(total));
        data.put("supervisor",userService.findAllUserNameByIds(new String[]{userId}));
        String wordFile = WordUtils.fillNormal(data,saveName);
        return wordFile;
    }
}
