package com.graduate.system.wecat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.graduate.api.arrage.ArrageController;
import com.graduate.common.BaseController;
import com.graduate.common.BaseJsonData;
import com.graduate.common.WordService;
import com.graduate.system.arrage.model.Arrage;
import com.graduate.system.arrage.service.ArrageService;
import com.graduate.system.evaluate.model.Evaluate;
import com.graduate.system.evaluate.service.EvaluateService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

/**
 * Created by konglinghai on 2017/5/25.
 */
@Controller
@RequestMapping("/wecat")
public class WecatController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(WecatController.class);

    @Autowired
    private ArrageService<Arrage> arrageService;

    @Autowired
    private EvaluateService<Evaluate> evaluateService;

    @Autowired
    private WordService wordService;


    @RequestMapping("/arrage/detail")
    @ResponseBody
    public BaseJsonData getArrageDetail(@RequestParam("id") Long id,@RequestParam("userId")Long userId){
        BaseJsonData data = new BaseJsonData();
        try {
            Arrage arrage = arrageService.findOne(id);
            List<Evaluate> evaluates = evaluateService.findEvaluateByArrageId(id);
            String evaluateContent ="";
            for(Evaluate evaluate :evaluates){
                if (evaluate.getCreator().equals(userId)) {
                    evaluateContent = evaluate.getContent();
                    break;
                }
            }
            HashMap<String,Object> result = new HashMap<>();
            result.put("arrage",arrage);
            result.put("evaluate",evaluateContent);
            return data.ok(result);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }

    @RequestMapping("/evaluate/normal")
    @ResponseBody
    public BaseJsonData saveEvaluateNormal(@RequestParam("id")Long id,@RequestParam("userId")Long userId,@RequestBody String body){
        BaseJsonData data = new BaseJsonData();
        try{
            JSONObject object = JSON.parseObject(body);
            int normal0 = Integer.parseInt(object.getString("normal0"));
            int normal1 = Integer.parseInt(object.getString("normal1"));
            int normal2 = Integer.parseInt(object.getString("normal2"));
            int normal3 = Integer.parseInt(object.getString("normal3"));
            int normal4 = Integer.parseInt(object.getString("normal4"));
            int normal5 = Integer.parseInt(object.getString("normal5"));
            int normal6 = Integer.parseInt(object.getString("normal6"));
            int normal7 = Integer.parseInt(object.getString("normal7"));
            int grade = normal0+normal1+normal2+normal3+normal4+normal5+normal6+normal7;


            List<Evaluate> evaluates = evaluateService.findEvaluateByArrageId(id);
            Evaluate evaluate = null;
            for(Evaluate e :evaluates){
                if (e.getCreator().equals(userId)) {
                    evaluate = e;
                    break;
                }
            }

            //生成word文档
            String savePath = "";
            String saveName = "";
            if(evaluate == null){
                saveName = String.valueOf(new Date().getTime());
                evaluate = new Evaluate();
            }else{
                saveName = StringUtils.substringBefore(StringUtils.substringAfterLast(evaluate.getPath(),"\\"),".");
            }

            Arrage arrage = arrageService.findOne(id);
            savePath = wordService.fillNormalSuparvisorFile(arrage,object,saveName,String.valueOf(userId),grade);

            evaluate.setCreateTime(new Date());
            evaluate.setCreator(userId);
            evaluate.setArrageId(id);
            evaluate.setContent(body);
            evaluate.setPath(savePath);
            evaluate.setGrade(grade);
            evaluateService.save(evaluate);
            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }

    @RequestMapping("/evaluate/chief")
    @ResponseBody
    public BaseJsonData saveEvaluateChief(@RequestParam("id")Long id,@RequestParam("userId")Long userId,@RequestBody String body){
        BaseJsonData data = new BaseJsonData();
        try{
            JSONObject object = JSON.parseObject(body);
            int chief0 = Integer.parseInt(object.getString("chief0"));

            List<Evaluate> evaluates = evaluateService.findEvaluateByArrageId(id);
            Evaluate evaluate = null;
            for(Evaluate e :evaluates){
                if (e.getCreator().equals(userId)) {
                    evaluate = e;
                    break;
                }
            }
            String savePath = "";
            //生成word文档
            String saveName = "";
            if(evaluate == null){
                saveName = String.valueOf(new Date().getTime());
                evaluate = new Evaluate();
            }else{
                saveName = StringUtils.substringBefore(StringUtils.substringAfterLast(evaluate.getPath(),"\\"),".");
            }
            Arrage arrage = arrageService.findOne(id);
            savePath = wordService.fillChiefSuparvisorFile(arrage,object,saveName);

            evaluate.setCreateTime(new Date());
            evaluate.setCreator(userId);
            evaluate.setArrageId(id);
            evaluate.setContent(body);
            evaluate.setPath(savePath);
            evaluate.setGrade(chief0);
            evaluateService.save(evaluate);

            return data.ok();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }

}
