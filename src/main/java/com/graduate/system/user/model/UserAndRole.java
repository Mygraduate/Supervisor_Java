package com.graduate.system.user.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

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
    @OneToOne(cascade = { CascadeType.MERGE,CascadeType.PERSIST, CascadeType.REMOVE } )
    @JoinColumn(name="uid",referencedColumnName = "id",insertable = false,updatable = false)
    @NotFound(action= NotFoundAction.IGNORE)
    private User user;

    @Column(name = "role_id")
    private Long roleId;//角色id
    @ManyToOne(cascade = CascadeType.PERSIST  )
    @JoinColumn(name="role_id",referencedColumnName = "id",insertable = false,updatable = false)
    @NotFound(action=NotFoundAction.IGNORE)
    private Role role;

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


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserAndRole() {
    }

    @Override
    public String toString() {
        return "UserAndRole{" +
                "id=" + id +
                ", uid=" + uid +
                ", user=" + user +
                ", roleId=" + roleId +
                ", role=" + role +
                '}';
    }
}
