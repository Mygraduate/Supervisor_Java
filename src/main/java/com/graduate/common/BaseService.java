package com.graduate.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
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

    //分页，排序，搜索
    public Page<T> findAll(int pageNo,int pageSize,HashMap<String,String> orderVals,HashMap<String,Object> searchVals) throws Exception {
        return  getRepository().findAll(buildSearch(searchVals),buildPage(pageNo,pageSize,orderVals));

    }

    //普通分页
    public Page<T> findAll(int pageNo,int pageSize) throws Exception {
        return  getRepository().findAll(buildPage(pageNo,pageSize,null));
    }

    //普通分页,排序
    public Page<T> findAll(int pageNo,int pageSize,HashMap<String,String> orderVals) throws Exception {
        return  getRepository().findAll(buildPage(pageNo,pageSize,orderVals));
    }

    //不分页多条件搜索并排序
    public List<T> findAll(HashMap<String,Object> searchVals,HashMap<String,String> orderVals) throws Exception {
        return  getRepository().findAll(buildSearch(searchVals),buildSort(orderVals));
    }

    //自定义搜索和排序,不分页
    public List<T> findAll(Specification specification,Sort sort) throws Exception {
        return  getRepository().findAll(specification,sort);
    }

    //自定义搜索和排序，分页
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


    @Modifying
    public void delete(T t) throws Exception {
        getRepository().delete(t);
    }

    @Modifying
    public void delete(List<? extends T> iterable) throws Exception {

        getRepository().delete(iterable);

    }

    @Modifying
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

    //分页和排序
    public Pageable buildPage(int pageNo,int pageSize,HashMap<String,String> orders) {
        return new PageRequest(pageNo - 1, pageSize,buildSort(orders));
    }

    public Sort buildSort(HashMap<String,String> orders){
        List<Sort.Order> list = new ArrayList<>();
        for(String key : orders.keySet()){
            String direction = orders.get(key);
            if(StringUtils.isNoneBlank(direction)){
                if(StringUtils.equals("ASC",direction)){
                    list.add(new Sort.Order(Sort.Direction.ASC,key));
                }else{
                    list.add(new Sort.Order(Sort.Direction.DESC,key));
                }
            }
        }
        return  new Sort(list);
    }


    //搜索
    public  Specification buildSearch(HashMap<String,Object> vals) {

        return new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                for (String key : vals.keySet()) {
                    Object val = vals.get(key);
                    if(val == null){
                        continue;
                    }
                    if(NumberUtils.isNumber(val.toString())){
                        predicate.getExpressions().add(
                                criteriaBuilder.equal(root.get(key).as(Integer.class),val)
                        );
                    }else if(val instanceof String){
                        if(StringUtils.isNoneBlank(val.toString())){
                            predicate.getExpressions().add(
                                    criteriaBuilder.like(root.<String>get(key), "%" + val + "%")

                            );
                        }
                    }
                }
                return predicate;
            }
        };
    }

    public Page<T> replacePageContent(List<T> replactList,Page<T> sourcePage){
        return new Page<T>() {
            @Override
            public int getTotalPages() {
                return sourcePage.getTotalPages();
            }

            @Override
            public long getTotalElements() {
                return sourcePage.getTotalElements();
            }

            @Override
            public <S> Page<S> map(Converter<? super T, ? extends S> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return sourcePage.getNumber();
            }

            @Override
            public int getSize() {
                return sourcePage.getSize();
            }

            @Override
            public int getNumberOfElements() {
                return sourcePage.getNumberOfElements();
            }

            @Override
            public List<T> getContent() {
                return replactList;
            }

            @Override
            public boolean hasContent() {
                if(replactList.size()>0){
                    return true;
                }
                return false;
            }

            @Override
            public Sort getSort() {
                return sourcePage.getSort();
            }

            @Override
            public boolean isFirst() {
                return sourcePage.isFirst();
            }

            @Override
            public boolean isLast() {
                return sourcePage.isLast();
            }

            @Override
            public boolean hasNext() {
                return sourcePage.hasNext();
            }

            @Override
            public boolean hasPrevious() {
                return sourcePage.hasPrevious();
            }

            @Override
            public Pageable nextPageable() {
                return sourcePage.nextPageable();
            }

            @Override
            public Pageable previousPageable() {
                return sourcePage.previousPageable();
            }

            @Override
            public Iterator<T> iterator() {
                return sourcePage.iterator();
            }
        };
    }
}
