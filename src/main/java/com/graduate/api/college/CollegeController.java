package com.graduate.api.college;

import com.graduate.common.BaseController;
import com.graduate.common.BaseJsonData;
import com.graduate.system.college.model.College;
import com.graduate.system.college.service.CollegeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;


/**
 * Created by Administrator on 2017/5/15.
 */
@RestController
@RequestMapping("api/college")
@Api(value = "api/college", description = "学院接口")
public class CollegeController extends BaseController {

    @Autowired
    private CollegeService<College> collegeService;

    @ApiOperation(value="新增学院", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/create"}, method= RequestMethod.POST)
    public BaseJsonData createCollege(@RequestBody College college) {
        BaseJsonData data = new BaseJsonData();
        try {
            College college1 = collegeService.save(college);
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            return data.fail(e.getMessage());
        }
    }

    @ApiOperation(value="获取学院列表", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/list"}, method=RequestMethod.POST)
    public BaseJsonData getCollegeList() {
        BaseJsonData data = new BaseJsonData();
        HashMap<String,Object> map = new HashMap<>();
        try{
            List<College> collegelist = collegeService.findAll();
            map.put("info",collegelist);
            return data.ok(map);
        }catch (Exception e){
            e.printStackTrace();
            return data.fail(e.getMessage());
        }
    }

    @ApiOperation(value="删除学院", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
    public BaseJsonData deleteCollegeList(@RequestBody College college) {
        BaseJsonData data = new BaseJsonData();
        try{
            collegeService.delete(college);
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            return data.fail(e.getMessage());
        }
    }

    @ApiOperation(value="修改学院", notes="")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value={"/update"}, method=RequestMethod.POST)
    public BaseJsonData updateCollegeList(@RequestBody College college) {
        BaseJsonData data = new BaseJsonData();
        HashMap<String,Object> map = new HashMap<>();
        try{
            College college1=collegeService.findOne(college.getId());
            map.put("info",college1);
            return data.ok(map);
        }catch (Exception e){
            e.printStackTrace();
            return data.fail(e.getMessage());
        }
    }


}
