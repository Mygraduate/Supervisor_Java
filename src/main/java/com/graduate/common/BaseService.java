package com.graduate.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by konglinghai on 2017/3/19.
 */

public abstract  class BaseService<T>{


    private BaseDao<T> repository;

    public abstract BaseDao<T> init();

    public BaseDao<T> getRepository() throws Exception {
        if(repository != null){
            return repository;
        }
        return  init();
    }

    public <S extends T> S save(S s) throws Exception {
        return getRepository().save(s);
    }


    public <S extends T> List<S> save(List<S> iterable) throws Exception {
        return toList(getRepository().save(iterable));
    }

    public T findOne(Long aLong) throws Exception {
        return getRepository().findOne(aLong);
    }


    public boolean exists(Long aLong) throws Exception {
        return getRepository().exists(aLong);
    }


    public List<T> findAll() throws Exception {
        return  toList(getRepository().findAll());
    }

    public Page<T> findAll(Specification specification,Pageable pageable) throws Exception {
        return  getRepository().findAll(specification,pageable);

    }


    public List<T> findAll(List <Long> iterable) throws Exception {
        return toList(getRepository().findAll(iterable));
    }


    public long count() throws Exception {

        return getRepository().count();

    }


    public void delete(Long aLong) throws Exception {

        getRepository().delete(aLong);

    }


    public void delete(T t) throws Exception {
        getRepository().delete(t);
    }


    public void delete(List<? extends T> iterable) throws Exception {

        getRepository().delete(iterable);

    }


    public void deleteAll() throws Exception {
        getRepository().deleteAll();
    }

    public  <S extends T> List toList(Iterable<S> iterable){
        List<S> list = new ArrayList<S>();
        if(iterable != null){
            Iterator iterator = iterable.iterator();
            while (iterator.hasNext()){
                S t = (S)iterator.next();
                list.add(t);
            }
        }
        return  list;
    }


}
