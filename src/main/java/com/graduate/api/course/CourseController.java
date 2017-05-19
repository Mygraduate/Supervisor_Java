package com.graduate.api.course;

import com.graduate.api.excel.ExcelController;
import com.graduate.common.BaseController;
import com.graduate.common.BaseJsonData;
import com.graduate.system.course.model.Course;
import com.graduate.system.course.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static com.sun.tools.doclint.Entity.ne;

/**
 * Created by konglinghai on 2017/5/16.
 */
@RestController
@RequestMapping("api/course")
@Api(value = "api/course", description = "课程信息")
public class CourseController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    CourseService<Course> courseService;

    @ApiOperation(value="获取课程列表", notes="")
    @RequestMapping(value={"/page"}, method= RequestMethod.POST)
    public BaseJsonData getPage(
            @ApiParam(value = "页数")@RequestParam(value = "pageNo") Integer pageNo,
            @ApiParam(value = "页长")@RequestParam(value = "pageSize") int pageSize,
            @ApiParam(value = "学院id")@RequestParam(value = "cid",required = false) Long cid,
            @ApiParam(value = "周数")@RequestParam(value = "week",required = false) Integer week,
            @ApiParam(value = "天数")@RequestParam(value = "day",required = false) Long day,
            @ApiParam(value = "节次")@RequestParam(value = "scope",required = false) String scope,
            @ApiParam(value = "教师姓名")@RequestParam(value = "teacher",required = false) String teacher,
            @ApiParam(value = "是否有安排")@RequestParam(value = "isArrange",required = false) Integer isArrange,
            @ApiParam(value = "年级")@RequestParam(value = "grade",required = false) String grade,
            @ApiParam(value = "班级")@RequestParam(value = "classes",required = false) String classes,
            @ApiParam(value = "上课地点")@RequestParam(value = "address",required = false) String address){
        try {
           HashMap<String,Object> searchVals = new HashMap<>();
            searchVals.put("cid",cid);
            searchVals.put("week",week);
            searchVals.put("day",day);
            searchVals.put("scope",scope);
            searchVals.put("teacher",teacher);
            searchVals.put("isArrange",isArrange);
            searchVals.put("grade",grade);
            searchVals.put("classes",classes);
            searchVals.put("address",address);
           HashMap<String,String> orderVals = new HashMap<>();
           orderVals.put("week","ASC");
           Page<Course> page =  courseService.findAll(pageNo,pageSize,orderVals,searchVals);
            return BaseJsonData.ok(page);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return BaseJsonData.fail(e.getMessage());
        }
    }
}
