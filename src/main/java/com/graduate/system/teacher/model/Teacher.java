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
    @Column(nullable=false,name = "cid")
    private Long cid;//学院表id

    @Column(name = "uid")
    @ApiModelProperty(notes = "用户表uid")
    private Long uid;//教师号

    @Column(name = "role_id")
    private Long roleId;//角色id

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

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
