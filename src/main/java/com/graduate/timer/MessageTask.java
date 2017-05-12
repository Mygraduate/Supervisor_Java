package com.graduate.timer;


import com.graduate.common.TimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by konglinghai on 2017/4/14.
 * 微信消息推送定时器
 */
@Component
public class MessageTask implements SchedulingConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(MessageTask.class);

    @Value("${time.message.push}")
    private String cron;

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

    //开始
    public void start(){
        job.start();
    }

    //停止
    public void stop(){
        try {
            job.join();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }


    public  class MyJob extends Thread{

        @Override
        public void run() {
            System.out.println("5秒执行一次");
        }
    }

}
