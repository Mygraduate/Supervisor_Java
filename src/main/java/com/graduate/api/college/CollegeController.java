package com.graduate.api.college;

import com.graduate.api.course.CourseController;
import com.graduate.common.BaseController;
import com.graduate.common.BaseJsonData;
import com.graduate.system.college.model.College;
import com.graduate.system.college.service.CollegeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

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
    public BaseJsonData getCollegeList() {
        BaseJsonData data = new BaseJsonData();
        try{
            List<College> collegelist = collegeService.findAll();
            return data.ok(collegelist);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
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

    @ApiOperation(value="根据学院id获取学院信息", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/collegeinfoByid"}, method=RequestMethod.POST)
    public BaseJsonData getcollegeInfoByid(@RequestParam("id") Long id) {
        BaseJsonData data = new BaseJsonData();
        try{
            College collegeInfo = collegeService.findCollegeByid(id);
            return data.ok(collegeInfo);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }


}
