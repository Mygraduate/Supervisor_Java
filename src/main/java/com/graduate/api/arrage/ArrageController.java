package com.graduate.api.arrage;

import com.graduate.api.teacher.TeacherController;
import com.graduate.common.BaseController;
import com.graduate.common.BaseJsonData;
import com.graduate.system.arrage.model.Arrage;
import com.graduate.system.arrage.service.ArrageService;
import com.graduate.system.user.model.User;
import com.graduate.system.user.model.UserAndRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static antlr.build.ANTLR.root;
import static com.sun.tools.doclets.formats.html.markup.HtmlStyle.title;

/**
 * Created by konglinghai on 2017/5/20.
 */
@RestController
@RequestMapping("api/arrage")
@Api(value = "api/arrage", description = "听课安排")
public class ArrageController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ArrageController.class);

    @Autowired
    private ArrageService<Arrage> arrageService;

    @ApiOperation(value="获取听课安排列表", notes="")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    @RequestMapping(value={"/list"}, method= RequestMethod.POST)
    public BaseJsonData getUserList(@ApiParam(value = "页数")@RequestParam(value = "pageNo") Integer pageNo,
                                    @ApiParam(value = "页长")@RequestParam(value = "pageSize") Integer pageSize,
                                    @ApiParam(value = "状态0：未确定，1：代表是已确定，2：代表是待执行，3：代表已执行")@RequestParam(value = "status") Integer status,
                                    @ApiParam(value = "周数")@RequestParam(value = "week",required = false) Integer week,
                                    @ApiParam(value = "天数")@RequestParam(value = "day",required = false) Integer day,
                                    @ApiParam(value = "督导员")@RequestParam(value = "supervisor",required = false) String supervisor,
                                    @ApiParam(value = "教师")@RequestParam(value = "teacher",required = false) String teacher
    ) {
        try{
            HashMap<String,Object> searchVals = new HashMap<>();
            searchVals.put("status",status);
            searchVals.put("week",week);
            searchVals.put("day",day);
            searchVals.put("supervisor",supervisor);
            searchVals.put("teacher",teacher);
            Page<Arrage> page = arrageService.findAllByField(searchVals,pageNo,pageSize);
            return BaseJsonData.ok(page);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return BaseJsonData.fail(e.getMessage());
        }
    }

    @ApiOperation(value="更改听课状态", notes="")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    @RequestMapping(value={"/update"}, method=RequestMethod.POST)
    public BaseJsonData deleteUserList(@RequestBody List<Arrage> Arrages) {
        BaseJsonData data = new BaseJsonData();
        try{
            arrageService.save(Arrages);
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }

    @ApiOperation(value="手动添加听课安排", notes="")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    @RequestMapping(value="/create", method=RequestMethod.POST)
    public BaseJsonData createUser(@RequestBody Arrage arrage) {
        BaseJsonData data = new BaseJsonData();
        try {
            arrageService.save(arrage);
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }

    }



}
