package com.graduate.api.college;

import com.alibaba.fastjson.JSON;
import com.graduate.api.course.CourseController;
import com.graduate.common.BaseController;
import com.graduate.common.BaseJsonData;
import com.graduate.system.college.model.College;
import com.graduate.system.college.service.CollegeService;
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
 * Created by Administrator on 2017/5/15.
 */
@RestController
@RequestMapping("api/college")
@Api(value = "api/college", description = "学院接口")
public class CollegeController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(CollegeController.class);

    @Autowired
    private CollegeService<College> collegeService;

    @ApiOperation(value="新增学院", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/create"}, method= RequestMethod.POST)
    public BaseJsonData createCollege(@RequestBody College college) {
        BaseJsonData data = new BaseJsonData();
        try {
            collegeService.save(college);
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }

    @ApiOperation(value="获取学院列表", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/list"}, method=RequestMethod.POST)
    public BaseJsonData getCollegeList(
            @ApiParam(value = "页数")@RequestParam(value = "pageNo") Integer pageNo,
            @ApiParam(value = "页长")@RequestParam(value = "pageSize") Integer pageSize,
            @ApiParam(value = "学院id")@RequestParam(value = "cid",required = false) Long cid,
            @ApiParam(value = "学院姓名")@RequestParam(value = "name",required = false) String name
    ) {
        try{
            HashMap<String,Object> searchVals = new HashMap<>();
            searchVals.put("id",cid);
            searchVals.put("name",name);
            HashMap<String,String> orderVals = new HashMap<>();
            orderVals.put("id","ASC");
            Page<College> collegelist = collegeService.findAll(pageNo,pageSize,orderVals,searchVals);
            return BaseJsonData.ok(JSON.toJSON(collegelist));
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return BaseJsonData.fail(e.getMessage());
        }
    }

    @ApiOperation(value="删除学院", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
    public BaseJsonData deleteCollegeList(@RequestBody List<College> college) {
        BaseJsonData data = new BaseJsonData();
        try{
            collegeService.delete(college);
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }

    @ApiOperation(value="修改学院", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/update"}, method=RequestMethod.POST)
    public BaseJsonData updateCollegeList(@RequestBody College college) {
        BaseJsonData data = new BaseJsonData();
        try{
            collegeService.save(college);
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }

//    @ApiOperation(value="根据学院id获取学院信息", notes="")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @RequestMapping(value={"/collegeinfoByid"}, method=RequestMethod.POST)
//    public BaseJsonData getcollegeInfoByid(@RequestParam("id") Long id) {
//        BaseJsonData data = new BaseJsonData();
//        try{
//            College collegeInfo = collegeService.findCollegeByid(id);
//            return data.ok(collegeInfo);
//        }catch (Exception e){
//            e.printStackTrace();
//            logger.error(e.getMessage(),e);
//            return data.fail(e.getMessage());
//        }
//    }
//
}
