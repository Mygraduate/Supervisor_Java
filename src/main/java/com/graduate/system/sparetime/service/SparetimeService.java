package com.graduate.system.sparetime.service;

import com.graduate.common.BaseDao;
import com.graduate.common.BaseService;
import com.graduate.system.sparetime.dao.SpareTimeDao;
import com.graduate.system.sparetime.model.SpareTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */
@Service
public class SparetimeService<T> extends BaseService<T> {
    @Autowired
    private SpareTimeDao mapper;

    @Override
    public BaseDao init(){return mapper;}

    public List<SpareTime> findSpareTimeByuid(Long uid){return mapper.findSpareTimeByuid(uid);}

    public List<SpareTime> findSpareTimeBycid(Long cid){return mapper.findSpareTimeBycid(cid);}
}
