package com.graduate.utils.wecat;

import com.alibaba.fastjson.JSON;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.cp.api.*;
import me.chanjar.weixin.cp.bean.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.graduate.utils.StringUtil.levenshtein;
import static org.apache.coyote.http11.Constants.a;

/**
 * Created by konglinghai on 2017/5/25.
 */
//@Service
public class WecatService {

    private static String corpId = "wx8be71c029f5137ea";
    private static String corpSecret="E727cvcBZSMMvAvmPv8xNAZiOenIKqio1TWxwcuWQc_gv-wKVtq7SVw04WI5c0QF";
    private static WxCpConfigStorage wxCpConfigStorage;
    private static WxCpService wxCpService;

    public static void init(String corpId,String corpSecret){
        WxCpInMemoryConfigStorage config = new WxCpInMemoryConfigStorage();
        config.setCorpId(corpId);
        config.setCorpSecret(corpSecret);
        wxCpConfigStorage = config;
        wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(config);

    }

    public static void main(String[] args) {
        init(corpId,corpSecret);
        try {
//            WxCpUser user = new WxCpUser();
//            user.setName("admin");
//            user.setUserId("1");
//            user.setWeiXinId("a823894716");
//            Integer [] departIds = new Integer[]{4};
//            user.setDepartIds(departIds);
//            user.setEmail("823894716@qq.com");
//            user.setMobile("13650421544");
//            addUser(user);

//            WxCpMessage.WxArticle article1 = new WxCpMessage.WxArticle();
//            article1.setTitle("xxxx听课安排");
//            article1.setPicUrl("http://www.gdmu.edu.cn/images/01.jpg");
//            article1.setUrl("www.baidu.com");
//            WxCpMessage message  = WxCpMessage.NEWS()
//                    .toUser("1")
//                    .agentId(1)
//                    .addArticle(article1)
//                    .build();
//            sendMessage(message);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //创建一个学院
    public static Integer CreateCollege(WxCpDepart wxCpDepart) throws Exception{
        return wxCpService.departCreate(wxCpDepart);
    }

    //删除一个学院，wecatId
    public static void DeleteCollege(Integer id)throws Exception{
        wxCpService.departDelete(id);
    }

    //增加用户
    public static void addUser(WxCpUser user) throws Exception{
        wxCpService.userCreate(user);
    }

    //删除用户
    public static void deleteUser(String userId)throws Exception{
        wxCpService.userDelete(userId);
    }

    public static List<WxCpUser> getUserList(Integer departId) throws Exception{
        return wxCpService.userList(departId,true,0);
    }

    public static void sendMessage(WxCpMessage message)throws Exception{
        wxCpService.messageSend(message);
    }


}
