package com.graduate.common;

import com.graduate.system.course.model.Course;
import com.graduate.system.course.service.CourseService;
import com.graduate.utils.word.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * Created by konglinghai on 2017/5/16.
 */
@Service
public class WordService {
    @Autowired
    CourseService<Course> courseService;

    public String exportClassPlan(HashMap<String,Object> searchVals,String saveName) throws Exception {
        HashMap sortVals =  new HashMap<String, String>();
        sortVals.put("week","ASC");
        List<Course> courses = courseService.findAll(searchVals,sortVals);
        String wordFile = WordUtils.exportClassPlan(courses,saveName);
        return wordFile;
    }
}
