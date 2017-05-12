package com.graduate.common;

import com.graduate.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by konglinghai on 2017/4/23.
 */
@Service
public class TimeService {
    private static final Logger logger = LoggerFactory.getLogger(TimeService.class);

    @Value("${time.semester.start}")
    private String start;

    //通过周次和星期计算具体的年月日
    public String calTimeByWeekAndDay(int week,int day){
        try{
            return DateUtil.getDate(start,week,day);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return "第"+week+"周"+","+"第"+day+"天";
    }

    //计算是星期几，例如2017年4月23日，是星期日
    public String calWeekDayByTime(String date){
        try {
            return DateUtil.CaculateWeekDay(date);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return date;
    }
}
