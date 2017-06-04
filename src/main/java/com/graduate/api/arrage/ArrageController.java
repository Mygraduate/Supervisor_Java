package com.graduate.api.arrage;

import com.alibaba.fastjson.JSON;
import com.graduate.api.teacher.TeacherController;
import com.graduate.common.BaseController;
import com.graduate.common.BaseJsonData;
import com.graduate.system.arrage.dto.*;
import com.graduate.system.arrage.model.Arrage;
import com.graduate.system.arrage.service.ArrageService;
import com.graduate.system.course.model.Course;
import com.graduate.system.course.service.CourseService;
import com.graduate.system.sparetime.model.SpareTime;
import com.graduate.system.sparetime.service.SparetimeService;
import com.graduate.system.user.model.User;
import com.graduate.system.user.model.UserAndRole;
import com.graduate.system.user.service.UserAndRoleService;
import com.graduate.system.user.service.UserService;
import com.graduate.utils.BeanMapper;
import com.graduate.utils.wecat.WecatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.chanjar.weixin.cp.bean.WxCpMessage;
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
import java.util.Comparator;
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

    @Autowired
    private WecatService wecatService;

    @Autowired
    private UserAndRoleService<UserAndRole> userAndRoleService;

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
            arrages.sort(new Comparator<Arrage>() {
                @Override
                public int compare(Arrage o1, Arrage o2) {
                    return o1.getCourse().getWeek()-o2.getCourse().getWeek();
                }
            });
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


    @ApiOperation(value="发送安排到微信", notes="")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    @RequestMapping(value="/sent", method=RequestMethod.POST)
    public BaseJsonData sentArragewx(@RequestBody List<Long> ids) {
        BaseJsonData data = new BaseJsonData();
        try{
            List<UserAndRole> master = new ArrayList<>();
            for(Long id : ids){
                Arrage arrage = arrageService.findOne(id);
                String [] wxlist=arrage.getGroups().split(",");
                int index = 0;
                if(index == 0){
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("roleId",2);
                    map.put("cid",arrage.getCollegeId());
                    master = userAndRoleService.findAllByField(map);
                }
                for (String wx: wxlist) {
                    WxCpMessage.WxArticle article1 = new WxCpMessage.WxArticle();
                    article1.setTitle(arrage.getTeacher().getName()+"听课安排");
                    article1.setPicUrl("http://www.gdmu.edu.cn/images/01.jpg");
                    String url = "";
                    if(index == 0){
                        url = wecatService.getUrl()+"/chief?id="+arrage.getId()+"&"+"userId="+wx;
                    }else{
                        url = wecatService.getUrl()+"/normal?id="+arrage.getId()+"&"+"userId="+wx;
                    }
                    article1.setUrl(url);
                    article1.setDescription(arrage.getCourse().getTime()+"  "+arrage.getCourse().getAddress()+"  "+arrage.getCourse().getName());
                    WxCpMessage message  = WxCpMessage.NEWS()
                            .toUser(wx)
                            .agentId(Integer.valueOf(wecatService.getEvaluateAppId()))
                            .addArticle(article1)
                            .build();
                    wecatService.sendMessage(message);
                    sendMessageToMaster(arrage,master);
                    index ++;
                }
            }
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }

    private void sendMessageToMaster(Arrage arrage,List<UserAndRole> masters){
        try{
            String masterIds = "";
            for(UserAndRole userAndRole : masters){
                masterIds = masterIds+"|"+userAndRole.getUser().getId();
            }
            String content = arrage.getTeacher().getName()+"的课程安排"+"已经发送";
                WxCpMessage message  = WxCpMessage.TEXT()
                        .toUser(masterIds.substring(1))
                        .agentId(Integer.valueOf(wecatService.getEvaluateAppId()))
                        .content(content)
                        .build();
                wecatService.sendMessage(message);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
    }

    @ApiOperation(value="获取听课安排人员听课统计信息", notes="")
    @RequestMapping(value="/summary", method=RequestMethod.POST)
    public BaseJsonData getArrageSummary(
            @ApiParam(value = "学院id")@RequestParam(value = "cid") Long cid,
            @ApiParam(value = "状态stauts")@RequestParam(value = "status") Integer status){
        List<Arrage> arrages = arrageService.findAllByCidAndStatus(cid,status);
        HashMap<String,Object> searchVals = new HashMap<>();
        searchVals.put("roleId",3);
        searchVals.put("cid",cid);
        List<UserAndRole> userAndRoleList=userAndRoleService.findAllByField(searchVals);//该学院的全部督导员
        HashMap<String,ArrageSummary> map = new HashMap<>();
        //初始化统计信息
        for(UserAndRole role : userAndRoleList){
            ArrageSummary summary = new ArrageSummary();
            summary.setUser(role.getUser());
            summary.setWeekSummaries(new ArrayList<>());
            summary.setTotal(0);
            map.put(role.getUid().toString(),summary);
        }

        for(UserAndRole role: userAndRoleList){
            for(Arrage arrage : arrages){
                String uid = role.getUid().toString();
                String [] groups = arrage.getGroups().split(",");
                for(int i=0;i<groups.length;i++){
                    if(uid.equals(groups[i])){
                        ArrageSummary summary =  map.get(uid);;
                        summary.addWeekNum(arrage.getCourse().getWeek());
                        summary.addTotal();
                    }
                }
            }
        }
        return BaseJsonData.ok(JSON.toJSON(map.values()));
    }

    @ApiOperation(value="修改听课安排人员", notes="")
    @RequestMapping(value="/edit/groups", method=RequestMethod.POST)
    public BaseJsonData editArrageGroups(
            @ApiParam(value = "听课安排id")@RequestParam(value = "id") Long id,
            @RequestBody List<Long> ids){
        try {
            if(ids.size()==0){
                return BaseJsonData.fail("请传入督导员用户id");
            }
            String groups = "";
            for(Long uid : ids){
                groups = groups +","+uid.toString();
            }
            groups = groups.substring(1);
            Arrage arrage = arrageService.findOne(id);
            sparetimeService.updateByUidAndTime(arrage.getGroups().split(","),arrage.getCollegeId(),arrage.getCourse().getWeek(),arrage.getCourse().getDay(),0);
            sparetimeService.updateByUidAndTime(groups.split(","),arrage.getCollegeId(),arrage.getCourse().getWeek(),arrage.getCourse().getDay(),1);
            arrage.setGroups(groups);
            arrageService.save(arrage);
            return BaseJsonData.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return BaseJsonData.fail(e);
        }
    }

    @ApiOperation(value="根据arrageId返回督导组", notes="")
    @RequestMapping(value="/find/groups", method=RequestMethod.POST)
    public BaseJsonData editArrageGroups(
            @ApiParam(value = "听课安排id")@RequestParam(value = "id") Long id){
        try {
            Arrage arrage = arrageService.findOne(id);
            String [] groups = arrage.getGroups().split(",");
            List<ArrageGroup> map = new ArrayList<>();
            for(int i=0;i<groups.length;i++){
                ArrageGroup group = new ArrageGroup();
                group.setId(groups[i]);
                String [] ids = new String[]{groups[i]};
                group.setName(userService.findAllUserNameByIds(ids));
                map.add(group);
            }
            return BaseJsonData.ok(map);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return BaseJsonData.fail(e);
        }
    }

}
