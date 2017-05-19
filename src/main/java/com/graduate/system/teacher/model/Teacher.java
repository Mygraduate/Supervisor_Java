package com.graduate.system.teacher.model;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * Created by konglinghai on 2017/3/19.
 * 教师信息表
 */
@Table(name = "teacher")
@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "cid")
    private Long cid;//学院号

    @Column(nullable=false,name = "name")
    private String name;//教师姓名

    @Column(name = "title")
    private String title;//职称

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Teacher() {
    }
}
