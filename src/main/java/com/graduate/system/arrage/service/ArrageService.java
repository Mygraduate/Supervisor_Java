package com.graduate.system.arrage.service;

import com.graduate.common.BaseDao;
import com.graduate.common.BaseService;
import com.graduate.system.arrage.dao.ArrageDao;
import com.graduate.system.arrage.dto.ArrageConfig;
import com.graduate.system.arrage.dto.SpareTimeConfig;
import com.graduate.system.arrage.model.Arrage;
import com.graduate.system.college.dao.CollegeDao;
import com.graduate.system.college.model.College;
import com.graduate.system.course.model.Course;
import com.graduate.system.sparetime.model.SpareTime;
import com.graduate.system.user.model.User;
import com.graduate.utils.ArrageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;

/**
 * Created by konglinghai on 2017/5/20.
 */
@Service
public class ArrageService<T>  extends BaseService<T>{
    @Autowired
    private ArrageDao mapper;

    @Override
    public BaseDao init() {
        return mapper;
    }

    public Page<Arrage> findAllByField(HashMap<String,Object> vals,int pageNo,int pageSize){
        HashMap<String,String> orderVals = new HashMap<>();
        orderVals.put("id","ASC");
       return mapper.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();

                if (vals.get("status") != null && NumberUtils.isNumber(vals.get("status").toString())) {
                    predicate.getExpressions().add(
                            criteriaBuilder.equal(root.<Integer>get("status"),vals.get("status"))
                    );
                }


                if (vals.get("week") != null && NumberUtils.isNumber(vals.get("week").toString())) {
                    predicate.getExpressions().add(
                            criteriaBuilder.equal(root.<Course>get("course").get("week"),vals.get("week"))
                    );
                }

                if (vals.get("day") != null && NumberUtils.isNumber(vals.get("day").toString())) {
                    predicate.getExpressions().add(
                            criteriaBuilder.equal(root.<Course>get("course").get("day"),vals.get("day"))
                    );
                }

                if (vals.get("teacher") != null && StringUtils.isNotBlank(vals.get("teacher").toString())) {
                    predicate.getExpressions().add(
                            criteriaBuilder.like(root.<Course>get("course").get("teacher"), "%" + vals.get("teacher").toString() + "%")
                    );
                }
                return predicate;
            }
        },buildPage(pageNo,pageSize,orderVals));
    }

    public List<SpareTimeConfig> autoCreateArrage(List<Arrage> arrages, List<Course> courses, List<SpareTime> times, ArrageConfig config){
        return ArrageUtil.autoCreateArrage(arrages,courses,times,config);
    }

    public List<Arrage> findAllByCidAndStatus(Long cid,Integer status){
        return mapper.findAllByCollegeIdAndStatus(cid,status);
    }
}
