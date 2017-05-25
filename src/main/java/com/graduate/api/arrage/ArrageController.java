package com.graduate.api.arrage;

import com.alibaba.fastjson.JSON;
import com.graduate.api.teacher.TeacherController;
import com.graduate.common.BaseController;
import com.graduate.common.BaseJsonData;
import com.graduate.system.arrage.dto.ArrageConfig;
import com.graduate.system.arrage.dto.ArrageStatus;
import com.graduate.system.arrage.dto.SpareTimeConfig;
import com.graduate.system.arrage.model.Arrage;
import com.graduate.system.arrage.service.ArrageService;
import com.graduate.system.course.model.Course;
import com.graduate.system.course.service.CourseService;
import com.graduate.system.sparetime.model.SpareTime;
import com.graduate.system.sparetime.service.SparetimeService;
import com.graduate.system.user.model.User;
import com.graduate.system.user.model.UserAndRole;
import com.graduate.system.user.service.UserService;
import com.graduate.utils.BeanMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
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
import static org.apache.coyote.http11.Constants.a;

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

    @Autowired
    private CourseService<Course> courseService;

    @Autowired
    private SparetimeService<SpareTime> sparetimeService;

    @Autowired
    private UserService<User> userService;

    @ApiOperation(value="获取听课安排列表", notes="")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    @RequestMapping(value={"/list"}, method= RequestMethod.POST)
    public BaseJsonData getArrageList(@ApiParam(value = "页数")@RequestParam(value = "pageNo") Integer pageNo,
                                    @ApiParam(value = "页长")@RequestParam(value = "pageSize") Integer pageSize,
                                    @ApiParam(value = "状态0：未确定，1：代表是已确定，2：代表是待执行，3：代表已执行")@RequestParam(value = "status") Integer status,
                                    @ApiParam(value = "周数")@RequestParam(value = "week",required = false) Integer week,
                                    @ApiParam(value = "天数")@RequestParam(value = "day",required = false) Integer day,
                                    @ApiParam(value = "教师")@RequestParam(value = "teacher",required = false) String teacher
    ) {
        try{
            HashMap<String,Object> searchVals = new HashMap<>();
            searchVals.put("status",status);
            searchVals.put("week",week);
            searchVals.put("day",day);
            searchVals.put("teacher",teacher);
            Page<Arrage> page = arrageService.findAllByField(searchVals,pageNo,pageSize);
            for(Arrage arrage : page.getContent()){
                arrage.setGroups(userService.findAllUserNameByIds(arrage.getGroups().split(",")));
            }
            return BaseJsonData.ok(JSON.toJSON(page));
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return BaseJsonData.fail(e.getMessage());
        }
    }

    @ApiOperation(value="更改听课状态", notes="")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    @RequestMapping(value={"/update"}, method=RequestMethod.POST)
    public BaseJsonData updateArrage(@RequestBody List<ArrageStatus> ArrageStatus) {
        BaseJsonData data = new BaseJsonData();
        try{
            List<Arrage> arrages = new ArrayList<>();
            for(ArrageStatus status : ArrageStatus){
                Arrage arrage = arrageService.findOne(status.getId());
                arrage.setStatus(status.getStatus());
                arrages.add(arrage);
            }

            //更新相应的课程和空闲时间列表
            for(Arrage arrage : arrages){
                courseService.updateStautsById(arrage.getStatus(),arrage.getCourseId());
                Course course = arrage.getCourse();
                if( course == null){
                    course = courseService.findCourseById(arrage.getCourseId());
                }
                sparetimeService.updateByUidAndTime(arrage.getGroups().split(","),arrage.getCollegeId(),course.getWeek(),course.getDay(),arrage.getStatus());
            }
            arrageService.save(arrages);
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
    public BaseJsonData createArrage(@RequestBody Arrage arrage) {
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

    @ApiOperation(value="算法听课安排", notes="")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    @RequestMapping(value="/auto/create", method=RequestMethod.POST)
    public BaseJsonData autoCreateArrage(
            @ApiParam(value = "学院id")@RequestParam(value = "cid") Long cid,
            @ApiParam(value = "mode create:在现有追加 replace：重新生成")@RequestParam(value = "mode") String mode,
            @RequestBody ArrageConfig config
            ) {
        BaseJsonData data = new BaseJsonData();

        try {
            if(StringUtils.equals(mode,"replace")){
                arrageService.delete(arrageService.findAllByCidAndStatus(cid,0));
            }
            HashMap<String,Object> searchVals = new HashMap<>();
            searchVals.put("cid",cid);
            HashMap<String,String> orderVals = new HashMap<>();
            orderVals.put("week","ASC");
            List<Arrage> arrages = new ArrayList<>();
            List<Course> courses = new ArrayList<>();
            List<SpareTime> spareTimes = new ArrayList<>();
            BeanMapper.copy(courseService.findAll(searchVals,orderVals),courses);
            BeanMapper.copy(sparetimeService.findSpareTimeBycid(cid),spareTimes);
            List<SpareTimeConfig> spareTimeConfigs = arrageService.autoCreateArrage(arrages,courses,spareTimes,config);
            arrageService.save(arrages);
            return data.ok(spareTimeConfigs);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }

    }

    @ApiOperation(value="删除听课安排", notes="")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    @RequestMapping(value="/delete", method=RequestMethod.POST)
    public BaseJsonData createArrage(@RequestBody List<Long> ids) {
        BaseJsonData data = new BaseJsonData();
        try{
            List<Arrage> arrages = new ArrayList<>();
            for(Long id : ids){
                Arrage arrage = arrageService.findOne(id);
                arrage.setStatus(0);
                arrages.add(arrage);
                arrageService.delete(arrage);
            }

            //更新相应的课程和空闲时间列表
            for(Arrage arrage : arrages){
                courseService.updateStautsById(arrage.getStatus(),arrage.getCourseId());
                Course course = arrage.getCourse();
                if( course == null){
                    course = courseService.findCourseById(arrage.getCourseId());
                }
                sparetimeService.updateByUidAndTime(arrage.getGroups().split(","),arrage.getCollegeId(),course.getWeek(),course.getDay(),arrage.getStatus());
            }
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }



}
