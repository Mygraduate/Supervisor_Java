package com.graduate.system.sparetime.dao;


import com.graduate.common.BaseDao;
import com.graduate.system.sparetime.model.SpareTime;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by konglinghai on 2017/3/19.
 */

@Repository
public interface SpareTimeDao extends BaseDao<SpareTime> {
    List<SpareTime> findSpareTimeByuid(Long uid);//根据用户查找用户空闲时间

    List<SpareTime> findSpareTimeBycid(Long cid);//查询整个学院的督导员空闲时间

    List<SpareTime> findSpareTimeByCidAndUid(Long cid,Long uid);//查询整个学院不同督导员空闲时间

    List<SpareTime> findSpareTimeByCidAndWeekAndDay(Long cid,Integer week,Integer day);//根据周次和天数查询空闲时间列表

}
