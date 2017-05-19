package com.graduate.api.teacher;

import com.graduate.api.course.CourseController;
import com.graduate.common.BaseJsonData;
import com.graduate.system.teacher.model.Teacher;
import com.graduate.system.teacher.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/5/16.
 */

@RestController
@RequestMapping("api/teacher")
@Api(value = "api/teacher", description = "老师接口")
public class TeacherController {
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private TeacherService<Teacher> teacherService;

    @ApiOperation(value="新增老师", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/create"}, method= RequestMethod.POST)
    public BaseJsonData createTeacher(@RequestBody Teacher teacher) {
        BaseJsonData data = new BaseJsonData();
        try {
            teacherService.save(teacher);
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }

    @ApiOperation(value="获取老师列表", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/list"}, method=RequestMethod.POST)
    public BaseJsonData getTeacherList(
            @ApiParam(value = "页数")@RequestParam(value = "pageNo") Integer pageNo,
            @ApiParam(value = "页长")@RequestParam(value = "pageSize") Integer pageSize,
            @ApiParam(value = "学院id")@RequestParam(value = "cid",required = false) Long cid,
            @ApiParam(value = "老师id")@RequestParam(value = "tid",required = false) Long tid,
            @ApiParam(value = "老师姓名")@RequestParam(value = "tname",required = false) String tname,
            @ApiParam(value = "职称")@RequestParam(value = "title",required = false) String title
    ) {
        try{
            HashMap<String,Object> searchVals = new HashMap<>();
            searchVals.put("id",tid);
            searchVals.put("cid",cid);
            searchVals.put("name",tname);
            searchVals.put("title",title);
            HashMap<String,String> orderVals = new HashMap<>();
            orderVals.put("id","ASC");
            Page<Teacher> page = teacherService.findAll(pageNo,pageSize,orderVals,searchVals);
            return BaseJsonData.ok(page);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return BaseJsonData.fail(e.getMessage());
        }
    }

    @ApiOperation(value="删除老师", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
    public BaseJsonData deleteTeacherList(@RequestBody List<Teacher> teacher) {
        BaseJsonData data = new BaseJsonData();
        try{
            teacherService.delete(teacher);
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }

    @ApiOperation(value="修改老师", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/update"}, method=RequestMethod.POST)
    public BaseJsonData updateTeacherList(@RequestBody Teacher teacher) {
        BaseJsonData data = new BaseJsonData();
        try{
            teacherService.save(teacher);
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }

//    @ApiOperation(value="根据老师姓名获取老师信息", notes="")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @RequestMapping(value={"/teacherinfo"}, method=RequestMethod.POST)
//    public BaseJsonData getTeacherInfo(@RequestParam("teachername") String teachername) {
//        BaseJsonData data = new BaseJsonData();
//        try{
//            Teacher teacherInfo = teacherService.findTeacherByname(teachername);
//            return data.ok(teacherInfo);
//        }catch (Exception e){
//            e.printStackTrace();
//            logger.error(e.getMessage(),e);
//            return data.fail(e.getMessage());
//        }
//    }

//    @ApiOperation(value="根据老师id获取老师信息", notes="")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @RequestMapping(value={"/teacherinfoByid"}, method=RequestMethod.POST)
//    public BaseJsonData getTeacherInfoByid(@RequestParam("id") Long id) {
//        BaseJsonData data = new BaseJsonData();
//        try{
//            Teacher teacherInfo = teacherService.findTeacherByid(id);
//            return data.ok(teacherInfo);
//        }catch (Exception e){
//            e.printStackTrace();
//            logger.error(e.getMessage(),e);
//            return data.fail(e.getMessage());
//        }
//    }

//    @ApiOperation(value="根据学院获取学院老师信息", notes="")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @RequestMapping(value={"/teacherlistbycid"}, method=RequestMethod.POST)
//    public BaseJsonData getTeacherInfoBycid(@RequestParam("cid")Long cid) {
//        BaseJsonData data = new BaseJsonData();
//        try{
//            List<Teacher> teacherInfo = teacherService.findTeacherBycid(cid);
//            return data.ok(teacherInfo);
//        }catch (Exception e){
//            e.printStackTrace();
//            logger.error(e.getMessage(),e);
//            return data.fail(e.getMessage());
//        }
//    }
}
