package com.graduate.system.sparetime.model;

import javax.persistence.*;

/**
 * Created by konglinghai on 2017/3/19.
 * 督导空闲时间表
 */
@Table(name = "spare_time")
@Entity
public class SpareTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false,name = "uid")
    private Long uid;//用户id
    @Column(nullable=false,name = "cid")
    private Long  cid;//学院id

    @Column(nullable=false,name = "week")
    private Integer week;//空闲周

    @Column(nullable=false,name = "day")
    private Integer day;//空闲天

    @Column(nullable=false,name = "scope")
    private String scope;//空闲节次

    @Column(nullable=false,name = "is_arrange")
    private Integer isArrange = 0;//是否被安排 0：代表未安排 1：代表被安排,默认是0

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Integer getIsArrange() {
        return isArrange;
    }

    public void setIsArrange(Integer isArrange) {
        this.isArrange = isArrange;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }
}
