package com.graduate.system.user.service;

import com.graduate.common.BaseDao;
import com.graduate.common.BaseService;

import com.graduate.system.user.dao.UserAndRoleDao;
import com.graduate.system.user.model.User;
import com.graduate.system.user.model.UserAndRole;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by konglinghai on 2017/3/20.
 */
@Service
public class UserAndRoleService<T> extends BaseService<T> {

    @Autowired
    private UserAndRoleDao mapper;
    @Override
    public BaseDao init() {
        return mapper;
    }

    public UserAndRole findRoleByUid(Long uid){
        return mapper.findUserAndRoleByUid(uid);
    }


    public Page<UserAndRole>  findAllByField(HashMap<String,Object> searchVals,int pageNo,int pageSize,HashMap<String,String> orderVals){
        Specification specification = buildSearchByField(searchVals);
        return mapper.findAll(specification,buildPage(pageNo,pageSize,orderVals));
    }

    public List<UserAndRole> findAllByField(HashMap<String,Object> searchVals){
        Specification specification = buildSearchByField(searchVals);
        return mapper.findAll(specification);
    }

    public Specification buildSearchByField(HashMap<String,Object> vals){
        return new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();

                if(vals.get("uid")!=null&&NumberUtils.isNumber(vals.get("uid").toString())){
                    predicate.getExpressions().add(
                            criteriaBuilder.equal(root.<String>get("uid"),NumberUtils.toLong(vals.get("uid").toString()))
                    );
                }
                if(vals.get("roleId")!=null&& NumberUtils.isNumber(vals.get("roleId").toString())){
                    predicate.getExpressions().add(
                            criteriaBuilder.equal(root.<String>get("roleId"),NumberUtils.toLong(vals.get("roleId").toString()))
                    );
                }
                if(vals.get("cid")!=null&&NumberUtils.isNumber(vals.get("cid").toString())){
                    predicate.getExpressions().add(
                            criteriaBuilder.equal(root.<User>get("user").get("cid"),NumberUtils.toLong(vals.get("cid").toString()))
                    );
                }
                if(vals.get("username")!=null&& StringUtils.isNotBlank(vals.get("username").toString())){
                    predicate.getExpressions().add(
                            criteriaBuilder.like(root.<User>get("user").get("username"),"%"+vals.get("username").toString()+"%")
                    );
                }
                if(vals.get("name")!=null&&StringUtils.isNotBlank(vals.get("name").toString())){
                    predicate.getExpressions().add(
                            criteriaBuilder.like(root.<User>get("user").get("teacher").get("name"),"%"+vals.get("name").toString()+"%")
                    );
                }
                if(vals.get("title")!=null&&StringUtils.isNotBlank(vals.get("title").toString())){
                    predicate.getExpressions().add(
                            criteriaBuilder.like(root.<User>get("user").get("teacher").get("title"),"%"+vals.get("title").toString()+"%")
                    );
                }
                return predicate;
            }
        };
    }

}
