package com.graduate.system.evaluate.service;

import com.graduate.common.BaseDao;
import com.graduate.common.BaseService;
import com.graduate.system.arrage.model.Arrage;
import com.graduate.system.course.dao.CourseDao;
import com.graduate.system.course.model.Course;
import com.graduate.system.evaluate.dao.EvaluateDao;
import com.graduate.system.evaluate.model.Evaluate;
import com.graduate.system.teacher.model.Teacher;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by konglinghai on 2017/4/30.
 */
@Service
public class EvaluateService<T> extends BaseService<T> {

    @Autowired
    private EvaluateDao mapper;

    @Override
    public BaseDao init() {
        return mapper;
    }

    public List<Evaluate> findEvaluateByArrageId(Long arrageId){
        return mapper.findEvaluateByArrageId(arrageId);
    }

    public Page<Evaluate> findByField(HashMap<String,Object> searchVals, int pageNo, int pageSize, HashMap<String,String> orderVals){
        Specification specification = buildSearchByField(searchVals);
        return mapper.findAll(specification,buildPage(pageNo,pageSize,orderVals));
    }

    public Specification buildSearchByField(HashMap<String,Object> vals){
        return new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();

                if(vals.get("teacher")!=null&& StringUtils.isNotBlank(vals.get("teacher").toString())){
                    predicate.getExpressions().add(
                            criteriaBuilder.like(root.<Arrage>get("arrage").get("teacher").get("name"),"%"+vals.get("teacher").toString()+"%")
                    );
                }
                if(vals.get("creator")!=null&& NumberUtils.isNumber(vals.get("creator").toString())){
                    predicate.getExpressions().add(
                            criteriaBuilder.equal(root.<String>get("creator"),NumberUtils.toLong(vals.get("creator").toString()))
                    );
                }
                if(vals.get("startime")!=null){
                    Date star=(Date) vals.get("startime");
                    predicate.getExpressions().add(
                            criteriaBuilder.greaterThanOrEqualTo(root.<String>get("createTime"),star)
                    );
                }
                if(vals.get("endtime")!=null){
                    Date end =(Date) vals.get("endtime");
                    predicate.getExpressions().add(
                            criteriaBuilder.lessThanOrEqualTo(root.<String>get("createTime"),end)
                    );
                }

                return predicate;
            }
        };
    }

}
