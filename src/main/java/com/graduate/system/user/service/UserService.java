package com.graduate.system.user.service;

import com.graduate.common.BaseDao;
import com.graduate.common.BaseService;

import com.graduate.system.user.dao.UserDao;
import com.graduate.system.user.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashMap;

/**
 * Created by konglinghai on 2017/3/20.
 */
@Service
public class UserService<T> extends BaseService<T> {

    @Autowired
    private UserDao mapper;

    @Override
    public BaseDao init() {
        return mapper;
    }


    public User findUserByname(String username){

        return mapper.findUserByUsername(username);
    }

    public String findAllUserNameByIds(String [] ids){
        String name = "";
        for(int i = 0; i<ids.length;i++){
            User user = mapper.findOne(Long.valueOf(ids[i]));
            if(user.getTid() != null && user.getTeacher() != null){
                name = name +","+user.getTeacher().getName();
            }else{
                name = name +","+user.getUsername();
            }
        }
        return  name.substring(1);
    }

}
