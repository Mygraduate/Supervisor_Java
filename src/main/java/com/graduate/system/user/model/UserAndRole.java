package com.graduate.system.user.model;

import javax.persistence.*;

/**
 * Created by konglinghai on 2017/3/19.
 * 用户角色表
 */
@Table(name = "user_to_role")
@Entity
public class UserAndRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "uid")
    private Long uid;//用户id
    @Column(name = "role_id")
    private Long roleId;//角色id

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

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }


    public UserAndRole() {
    }

    @Override
    public String toString() {
        return "UserAndRole{" +
                "id=" + id +
                ", uid=" + uid +
                ", roleId=" + roleId +
                '}';
    }
}
