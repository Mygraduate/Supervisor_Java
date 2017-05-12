package com.graduate.system.user.model;



import javax.persistence.*;

/**
 * Created by konglinghai on 2017/3/19.
 * 角色菜单表
 */
@Table(name = "role_to_menu")
@Entity
public class RoleAndMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable=false,name = "role_id")
    private Long roleId;//角色id
    @Column(nullable=false,name = "pid")
    private Long pid;//菜单父id
    @Column(nullable=false,name = "type")
    private Integer type;//节点类型，0：代表是父节点，1：代表是子节点
    @Column(nullable=false,name = "name")
    private String name;//菜单名称
    @Column(nullable=false,name = "url")
    private String url;//访问的url
    @Column(name = "des")
    private String des;//菜单描述

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
