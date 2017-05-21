package com.graduate.system.sparetime.dto;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/5/22.
 */
public class SumDTO {
    private Long uid;
    private String spareweek;
    private  String name;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getSpareweek() {
        return spareweek;
    }

    public void setSpareweek(String spareweek) {
        this.spareweek = spareweek;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SumDTO() {
    }

    @Override
    public String toString() {
        return "SumDTO{" +
                "uid=" + uid +
                ", spareweek='" + spareweek + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
