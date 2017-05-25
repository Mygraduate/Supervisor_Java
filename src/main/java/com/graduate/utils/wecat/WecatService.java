package com.graduate.utils.wecat;

import com.alibaba.fastjson.JSON;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.cp.api.*;
import me.chanjar.weixin.cp.bean.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.graduate.utils.StringUtil.levenshtein;
import static org.apache.coyote.http11.Constants.a;

/**
 * Created by konglinghai on 2017/5/25.
 */
@Service
public class WecatService {

    @Value("${wecat.corpId}")
    private String corpId;

    @Value("${wecat.corpSecret}")
    private String corpSecret;

    @Value("${wecat.server}")
    private String url;

    @Value("${wecat.evaluate.appId}")
    private String evaluateAppId;

    private  static  WxCpService wxCpService;

    public  void init(){
        if(wxCpService == null){
            WxCpInMemoryConfigStorage config = new WxCpInMemoryConfigStorage();
            config.setCorpId(corpId);
            config.setCorpSecret(corpSecret);
            wxCpService = new WxCpServiceImpl();
            wxCpService.setWxCpConfigStorage(config);
        }

    }

    public String getUrl(){
        return this.url;
    }

    public String getEvaluateAppId(){
        return this.evaluateAppId;
    }
    //创建一个学院
    public  Integer createCollege(WxCpDepart wxCpDepart) throws Exception{
        init();
        return wxCpService.departCreate(wxCpDepart);
    }

    //删除一个学院，wecatId
    public  void deleteCollege(Integer id)throws Exception{
        init();
        wxCpService.departDelete(id);
    }

    //增加用户
    public  void addUser(WxCpUser user) throws Exception{
        init();
        wxCpService.userCreate(user);
    }

    //删除用户
    public  void deleteUser(String userId)throws Exception{
        init();
        wxCpService.userDelete(userId);
    }

    public  List<WxCpUser> getUserList(Integer departId) throws Exception{
        init();
        return wxCpService.userList(departId,true,0);
    }

    public  void sendMessage(WxCpMessage message)throws Exception{
        init();
        wxCpService.messageSend(message);
    }


}
