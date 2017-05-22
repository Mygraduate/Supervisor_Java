package com.graduate.utils;

import com.google.common.collect.Lists;
import org.apache.poi.ss.formula.functions.T;
import org.dozer.DozerBeanMapper;

import java.util.*;

/**
 * Created by konglinghai on 2017/5/2.
 */
public class BeanMapper {
    private static DozerBeanMapper dozer = new DozerBeanMapper();

    public BeanMapper() {
    }

    public static <T> T map(Object source, Class<T> destinationClass) {

        return dozer.map(source, destinationClass);
    }

    public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
        ArrayList destinationList = Lists.newArrayList();
        Iterator i$ = sourceList.iterator();

        while(i$.hasNext()) {
            Object sourceObject = i$.next();
            Object destinationObject = dozer.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

    public static void copy(Object source, Object destinationObject) {
        dozer.map(source, destinationObject);
    }

}

