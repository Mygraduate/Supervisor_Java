package com.graduate.api.sparetime;

import com.graduate.common.BaseJsonData;
import com.graduate.system.sparetime.model.SpareTime;
import com.graduate.system.sparetime.service.SparetimeService;
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
 * Created by Administrator on 2017/5/19.
 */
@RestController
@RequestMapping("api/sparetime")
@Api(value = "api/sparetime", description = "用户空闲时间接口")
public class SparetimeController {
    private static final Logger logger = LoggerFactory.getLogger(SparetimeController.class);

    @Autowired
    private SparetimeService<SpareTime> sparetimeService;

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
            @ApiParam(value = "页数")@RequestParam(value = "pageNo") Integer pageNo,
            @ApiParam(value = "页长")@RequestParam(value = "pageSize") Integer pageSize,
            @ApiParam(value = "用户id")@RequestParam(value = "uid") Long uid
    ){
        try{
            HashMap<String,Object> searchVals = new HashMap<>();
            searchVals.put("uid",uid);
            HashMap<String,String> orderVals = new HashMap<>();
            orderVals.put("week","ASC");
            Page<SpareTime> page = sparetimeService.findAll(pageNo,pageSize,orderVals,searchVals);
            return BaseJsonData.ok(page);

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
            for (SpareTime o:spareTimeListold) {
                for (SpareTime n:spareTimeListnew) {
                    if(o.getWeek()==n.getWeek()&&o.getDay()==o.getDay()){
                        n.setId(o.getId());
                        spareTimeListold.remove(o);
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

}
