package com.graduate.timer;


import com.graduate.common.TimeService;
import com.graduate.system.arrage.model.Arrage;
import com.graduate.system.arrage.service.ArrageService;
import com.graduate.system.user.model.User;
import com.graduate.system.user.model.UserAndRole;
import com.graduate.system.user.service.UserAndRoleService;
import com.graduate.system.user.service.UserService;
import com.graduate.utils.DateUtil;
import com.graduate.utils.wecat.WecatService;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by konglinghai on 2017/4/14.
 * 微信消息推送定时器
 */
@Component
public class MessageTask implements SchedulingConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(MessageTask.class);

    @Value("${time.message.push}")
    private String cron;

    @Autowired
    ArrageService<Arrage> arrageService;

    @Autowired
    WecatService wecatService;

    @Autowired
    UserService<User> userService;

    @Autowired
    UserAndRoleService<UserAndRole> userAndRoleService;

    private MyJob job = new MyJob();

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(
                job, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                // 定时任务触发，可修改定时任务的执行周期
                CronTrigger trigger = new CronTrigger(cron);
                Date nextExecDate = trigger.nextExecutionTime(triggerContext);
                return nextExecDate;
            }
        });
    }

    public String getCorn() {
        return cron;
    }

    public void setCorn(String cron) {
        this.cron = cron;
    }


    public  class MyJob extends Thread{

        @Override
        public void run() {
            String currentTime = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
            List<Arrage> arrages = arrageService.findAllByTime(currentTime);
            List<UserAndRole> master = new ArrayList<>();
            int arrageIndex = 0;
            for(Arrage arrage : arrages){
                String [] wxlist=arrage.getGroups().split(",");
                if(arrageIndex == 0){
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("roleId",2);
                    map.put("cid",arrage.getCollegeId());
                    master = userAndRoleService.findAllByField(map);
                }
                int index = 0;
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
                    try{
                        wecatService.sendMessage(message);
                        sendMessageToMaster(arrage,master);
                    }catch (Exception e){
                        e.printStackTrace();
                        logger.error(e.getMessage(),e);
                    }
                    index ++;
                }
                arrageIndex ++;
            }
        }

        private void sendMessageToMaster(Arrage arrage,List<UserAndRole> masters) throws Exception{
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

        }
    }

}
