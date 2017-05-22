package com.graduate.system.sparetime.dto;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/5/22.
 */
public class SumDTO {
    private Long uid;
    private Long cid;
    private int[] spareweek;
    private  String name;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public int[] getSpareweek() {
        return spareweek;
    }

    public void setSpareweek(int[] spareweek) {
        this.spareweek = spareweek;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public SumDTO() {
    }

    @Override
    public String toString() {
        return "SumDTO{" +
                "uid=" + uid +
                ", cid=" + cid +
                ", spareweek=" + Arrays.toString(spareweek) +
                ", name='" + name + '\'' +
                '}';
    }
}
