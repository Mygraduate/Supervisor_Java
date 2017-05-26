package com.graduate.api.evaluate;

import com.alibaba.fastjson.JSON;
import com.graduate.common.BaseController;
import com.graduate.common.BaseJsonData;
import com.graduate.system.evaluate.model.Evaluate;
import com.graduate.system.evaluate.service.EvaluateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.Date;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/5/25.
 */

@Controller
@RequestMapping("api/evaluate")
@Api(value = "api/evaluate", description = "反馈模块")
public class EvaluateController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(EvaluateController.class);

    @Autowired
    private EvaluateService<Evaluate> evaluateEvaluateService;


    @ApiOperation(value="查询反馈信息", notes="")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public BaseJsonData getEvaluateList(
            @ApiParam(value = "页数")@RequestParam(value = "pageNo") Integer pageNo,
            @ApiParam(value = "页长")@RequestParam(value = "pageSize") Integer pageSize,
            @ApiParam(value = "授课老师姓名")@RequestParam(value = "teacher",required = false) String teacher,
            @ApiParam(value = "督导员id")@RequestParam(value = "creator",required = false) Long creator,
            @ApiParam(value = "起始时间")@RequestParam(value = "startime",required = false) Date startime,
            @ApiParam(value = "结束时间")@RequestParam(value = "endtime",required = false) Date endtime
    ) {
        try {
        HashMap<String,Object> searchVals = new HashMap<>();
        searchVals.put("teacher",teacher);
        searchVals.put("creator",creator);
        searchVals.put("startime",startime);
        searchVals.put("endtime",endtime);
        HashMap<String,String> orderVals = new HashMap<>();
        orderVals.put("id","ASC");
        Page<Evaluate> evaluatePage=evaluateEvaluateService.findByField(searchVals,pageNo,pageSize,orderVals);
        return  BaseJsonData.ok(JSON.toJSON(evaluatePage));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return BaseJsonData.fail(e.getMessage());
        }
    }
}
