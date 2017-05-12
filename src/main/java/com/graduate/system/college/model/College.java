package com.graduate.system.college.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import javax.persistence.*;
/**
 * Created by konglinghai on 2017/3/19.
 */
@Table(name = "college")
@Entity
public class College {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable=false,name = "pid")
    private Long pid;//上级学院id
    @Column(nullable=false,name = "type")
    private Integer type;//0：代表是父节点，1：代表是子节点，例如，信息工程学院是父节点，下属的教研室是子节点
    @Column(nullable=false,name = "name")
    private String name;//学院名称
    @Column(name = "des")
    private String des;//描述

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
