package com.graduate.system.wecat;

import com.graduate.api.arrage.ArrageController;
import com.graduate.common.BaseController;
import com.graduate.common.BaseJsonData;
import com.graduate.system.arrage.model.Arrage;
import com.graduate.system.arrage.service.ArrageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by konglinghai on 2017/5/25.
 */
@Controller
@RequestMapping("/wecat")
public class WecatController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(WecatController.class);

    @Autowired
    private ArrageService<Arrage> arrageService;


    @RequestMapping("/arrage/detail")
    @ResponseBody
    public BaseJsonData getArrageDetail(@RequestParam("id") Long id){
        BaseJsonData data = new BaseJsonData();
        try {
            Arrage arrage = arrageService.findOne(id);
            return data.ok(arrage);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return data.fail(e.getMessage());
        }
    }

}
