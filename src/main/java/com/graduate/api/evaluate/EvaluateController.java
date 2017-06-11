package com.graduate.api.evaluate;

import com.alibaba.fastjson.JSON;
import com.graduate.common.BaseController;
import com.graduate.common.BaseJsonData;
import com.graduate.common.WordService;
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


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/5/25.
 */

@Controller
@RequestMapping("api/evaluate")
@Api(value = "api/evaluate", description = "反馈模块")
public class EvaluateController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(EvaluateController.class);

    @Autowired
    private EvaluateService<Evaluate> evaluateService;

    @Autowired
    private WordService wordService;

    @ApiOperation(value="查询反馈信息", notes="")
    @PreAuthorize("hasRole('ROLE_MASTER') or hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_TEACHER')")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public BaseJsonData getEvaluateList(
            @ApiParam(value = "页数")@RequestParam(value = "pageNo") Integer pageNo,
            @ApiParam(value = "页长")@RequestParam(value = "pageSize") Integer pageSize,
            @ApiParam(value = "授课老师姓名")@RequestParam(value = "teacher",required = false) String teacher,
            @ApiParam(value = "督导员id")@RequestParam(value = "creator",required = false) Long creator,
            @ApiParam(value = "起始时间")@RequestParam(value = "startime",required = false) String startime,
            @ApiParam(value = "结束时间")@RequestParam(value = "endtime",required = false) String endtime
    ) {
        try {
        HashMap<String,Object> searchVals = new HashMap<>();
        searchVals.put("teacher",teacher);
        searchVals.put("creator",creator);
        if(startime!=null&&startime.trim().length()>0){
            searchVals.put("startime",startime);
        }
         if(endtime!=null&&endtime.trim().length()>0){
             searchVals.put("endtime",endtime);
        }
        HashMap<String,String> orderVals = new HashMap<>();
        orderVals.put("id","ASC");
        Page<Evaluate> evaluatePage=evaluateService.findByField(searchVals,pageNo,pageSize,orderVals);
        return  BaseJsonData.ok(JSON.toJSON(evaluatePage));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return BaseJsonData.fail(e.getMessage());
        }
    }

    @ApiOperation(value="下载听课反馈结果", notes="")
    @RequestMapping(value = "/download",method = RequestMethod.GET)
    public void  exportCourse(
            @ApiParam(value = "arrageId(听课安排id)")@RequestParam(value = "arrageId") Long arrageId,
            HttpServletRequest request, HttpServletResponse response) {
        try{
            List<Evaluate> evaluates = evaluateService.findEvaluateByArrageId(arrageId);
            Evaluate eval = evaluates.get(0);
            String saveName = eval.getArrage().getTeacher().getName()+"-"+eval.getArrage().getCourse().getTime();
            List<String> fileNames = new ArrayList<>();
            for(Evaluate evaluate:evaluates){
                fileNames.add(evaluate.getPath());
            }
            String savePath = wordService.makeZip(saveName,fileNames);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/x-zip-compressed");
            response.setHeader("Content-Disposition", "attachment;filename="+convertFileName(request, saveName)+".zip");

            FileInputStream in = null;
            OutputStream out = null;
            try {
                in = (new FileInputStream(savePath));
                out = response.getOutputStream();

                byte[] buffer = new byte[2048];
                int n = -1;
                while ((n = in.read(buffer)) > 0) {
                    out.write(buffer, 0, n);
                }
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) in.close();
                    if (out != null) out.close();
                } catch(IOException ignore) {}
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }

    }

    private static String convertFileName(HttpServletRequest request, String fileName) throws Exception {
        String agent = request.getHeader("User-Agent");
        if (agent == null) {
            return new String(fileName.getBytes(), "UTF-8");
        }
        // 火狐浏览器
        agent = agent.toLowerCase();
        if (agent.indexOf("firefox") != -1) {
            return new String(fileName.getBytes(), "ISO-8859-1");
        }
        return URLEncoder.encode(fileName, "UTF-8");
    }
}
