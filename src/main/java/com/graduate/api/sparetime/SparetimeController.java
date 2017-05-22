package com.graduate.api.sparetime;

import com.alibaba.fastjson.JSON;
import com.graduate.common.BaseJsonData;

import com.graduate.system.sparetime.dto.SumDTO;import com.graduate.system.course.model.Course;
import com.graduate.system.course.service.CourseService;import com.graduate.system.sparetime.model.SpareTime;
import com.graduate.system.sparetime.service.SparetimeService;
import com.graduate.system.user.model.User;
import com.graduate.system.user.service.UserService;
import com.graduate.utils.DateUtil;
import com.graduate.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by Administrator on 2017/5/19.
 */
@RestController
@RequestMapping("api/sparetime")
@Api(value = "api/sparetime", description = "用户空闲时间接口")
public class SparetimeController {
    private static final Logger logger = LoggerFactory.getLogger(SparetimeController.class);

    @Autowired
    private SparetimeService<SpareTime> sparetimeService;

    @Autowired
    private UserService<User> userService;

    @Autowired
    private CourseService<Course> courseService;

    @ApiOperation(value="新增用户空闲表", notes="")
    @RequestMapping(value={"/create"}, method= RequestMethod.POST)
    public BaseJsonData createSparetime(@RequestBody List<SpareTime> sparetimes){
        BaseJsonData data = new BaseJsonData();
        try {
            sparetimeService.save(sparetimes);
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }

    @ApiOperation(value="获取用户空闲列表", notes="")
    @RequestMapping(value={"/list"}, method=RequestMethod.POST)
    public BaseJsonData getusersparetime(
            @ApiParam(value = "用户id")@RequestParam(value = "uid") Long uid,
            @ApiParam(value = "week")@RequestParam(value = "week") Integer week
    ){
        try{
            HashMap<String,Object> searchVals = new HashMap<>();
            searchVals.put("uid",uid);
            searchVals.put("week",week);
            HashMap<String,String> orderVals = new HashMap<>();
            orderVals.put("week","ASC");
            List<SpareTime> page = sparetimeService.findAll(searchVals,orderVals);
            return BaseJsonData.ok(JSON.toJSON(page));
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return BaseJsonData.fail(e.getMessage());
        }
    }

    @ApiOperation(value="删除用户空闲时间", notes="")
    @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
    public BaseJsonData deleteUserSparetime(
            @ApiParam(value = "用户id")@RequestParam(value = "uid") Long uid
    ) {
        BaseJsonData data = new BaseJsonData();
        try{
            List<SpareTime> listsp=sparetimeService.findSpareTimeByuid(uid);
            sparetimeService.delete(listsp);
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }

    @ApiOperation(value="修改空闲时间", notes="")
    @RequestMapping(value={"/update"}, method=RequestMethod.POST)
    public BaseJsonData updateUserSparetime(@RequestBody List<SpareTime> spareTimeListnew) {
        BaseJsonData data = new BaseJsonData();
        try{
            Long uid=spareTimeListnew.get(0).getUid();
            List<SpareTime> spareTimeListold=sparetimeService.findSpareTimeByuid(uid);
            for (Iterator<SpareTime> o = spareTimeListold.iterator(); o.hasNext();) {
                SpareTime os=o.next();
                for (Iterator<SpareTime> n = spareTimeListnew.iterator(); n.hasNext();) {
                    SpareTime ns=n.next();
                    if(os.getWeek()==ns.getWeek()&&os.getDay()==ns.getDay()){
                        ns.setId(os.getId());
                        o.remove();
                        break;
                    }
                }
            }
            sparetimeService.delete(spareTimeListold);
            sparetimeService.save(spareTimeListnew);
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }

    @ApiOperation(value="获取督导员已填写空闲时间", notes="")
    @RequestMapping(value={"/getsparetime"}, method=RequestMethod.POST)
    public BaseJsonData getsparetime(
            @ApiParam(value = "页数")@RequestParam(value = "pageNo") Integer pageNo,
            @ApiParam(value = "页长")@RequestParam(value = "pageSize") Integer pageSize,
            @ApiParam(value = "学院id")@RequestParam(value = "cid") Long cid
    ){
        try{
            List<SpareTime> spareTimeList=sparetimeService.findSpareTimeBycid(cid);
            HashMap<String,SumDTO> list=new HashMap<>();
            for (SpareTime s: spareTimeList) {
                if(!list.containsKey(String.valueOf(s.getUid()))){
                    SumDTO sum=new SumDTO();
                    sum.setUid(s.getUid());
                    sum.setCid(cid);
                    sum.setName(s.getUser().getTeacher().getName());
                    int[] a={s.getWeek()};
                    sum.setSpareweek(a);
                    list.put(String.valueOf(s.getUid()),sum);
                }
                else{
                    SumDTO weeks = list.get(String.valueOf(s.getUid()));
                    int[] intArray = weeks.getSpareweek();
                    String result = "";
                    for (int i = 0; i < intArray.length; i++)
                    {
                        if (result!="")
                            result += "," + intArray[i];
                        else
                            result = String.valueOf(intArray[i]);
                    }
                    List sp = new ArrayList();
                    sp = Arrays.asList(result.split(","));
                    if(!sp.contains(String.valueOf(s.getWeek()))){
                        String str=result+","+String.valueOf(s.getWeek());
                        String strr[] = str.split(",");
                        int array[] = new int[strr.length];
                        for(int i=0;i<strr.length;i++) {
                            array[i] = Integer.parseInt(strr[i]);
                        }
                        weeks.setSpareweek(array);
                    }
                }
            }

            List<SumDTO> sumDTOList =new ArrayList<>();
            for (SumDTO in : list.values()) {
                sumDTOList.add(in);
            }
            Page<SumDTO> sumDTOPage = new Page<SumDTO>() {
                @Override
                public int getTotalPages() {
                    return (int)Math.ceil((double)sumDTOList.size()/pageSize);
                }

                @Override
                public long getTotalElements() {
                    return sumDTOList.size();
                }

                @Override
                public <S> Page<S> map(Converter<? super SumDTO, ? extends S> converter) {
                    return null;
                }

                @Override
                public int getNumber() {
                    return pageNo-1;
                }

                @Override
                public int getSize() {
                    return pageSize;
                }

                @Override
                public int getNumberOfElements() {
                    if(isFirst()){
                        return (int)getTotalElements();
                    }
                    if(isLast()){
                        return (int)getTotalElements()-getNumber()*getSize();
                    }
                    return 0;
                }

                @Override
                public List<SumDTO> getContent() {
                    return sumDTOList.subList(getNumber()*getSize(),getNumber()*getSize()+getNumberOfElements());
                }

                @Override
                public boolean hasContent() {
                    if(sumDTOList==null){
                        return false;
                    }
                    return true;
                }

                @Override
                public Sort getSort() {
                    return null;
                }

                @Override
                public boolean isFirst() {
                    if(getNumber()==0){
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean isLast() {
                    if(getNumber()==getTotalPages()-1){
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public boolean hasPrevious() {
                    return false;
                }

                @Override
                public Pageable nextPageable() {
                    return null;
                }

                @Override
                public Pageable previousPageable() {
                    return null;
                }

                @Override
                public Iterator<SumDTO> iterator() {
                    return sumDTOList.iterator();
                }
            };
            return BaseJsonData.ok(sumDTOPage);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return BaseJsonData.fail(e.getMessage());
        }
    }


    @ApiOperation(value="自动填补空闲时间", notes="")
    @RequestMapping(value={"/auto/create"}, method=RequestMethod.POST)
    public BaseJsonData autoCreateUserSparetime(
            @ApiParam(value = "用户id")@RequestParam(value = "uid") Long uid,
            @ApiParam(value = "教师id没有tid传-1")@RequestParam(value = "tid") Long tid,
            @ApiParam(value = "起始周前端默认1")@RequestParam(value = "start") Integer startWeek,
            @ApiParam(value = "终止周前端默认20")@RequestParam(value = "end") Integer endWeek
    ) {
        Long cid = userService.findUserByname( UserUtil.getUserName()).getCid();
        BaseJsonData data = new BaseJsonData();
        try{
            List<Course> courses = courseService.findAllByTid(tid);
            List<SpareTime> list = sparetimeService.autoCreateSpareTime(courses,cid,uid,startWeek,endWeek);
            List<SpareTime> spareTimeListold=sparetimeService.findSpareTimeByuid(uid);
            for (Iterator<SpareTime> o = spareTimeListold.iterator(); o.hasNext();) {
                SpareTime os=o.next();
                for (Iterator<SpareTime> n = list.iterator(); n.hasNext();) {
                    SpareTime ns=n.next();
                    if(os.getWeek()==ns.getWeek()&&os.getDay()==ns.getDay()){
                        ns.setId(os.getId());
                        o.remove();
                        break;
                    }
                }
            }
            sparetimeService.delete(spareTimeListold);
            sparetimeService.save(list);
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }
}
