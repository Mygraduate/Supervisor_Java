package com.graduate.system.user.model;

import com.graduate.system.college.model.College;
import com.graduate.system.teacher.model.Teacher;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;


/**
 * Created by konglinghai on 2017/3/19.
 * 用户表
 */
@Table(name = "user")
@Entity
@ApiModel()
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "表id",required = false)
    private Long id;

    @Column(name = "tid")
    @ApiModelProperty(notes = "教师id",required = false)
    private Long tid;//教师表id
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="tid",referencedColumnName = "id",insertable = false,updatable = false)
    @NotFound(action=NotFoundAction.IGNORE)
    private Teacher teacher;

    @Column(nullable=false,name = "cid")
    @ApiModelProperty(notes = "学院id",required = false)
    private Long cid;//学院表id

    @ManyToOne(cascade = CascadeType.PERSIST )
    @JoinColumn(name="cid",referencedColumnName = "id",insertable = false,updatable = false)
    @NotFound(action=NotFoundAction.IGNORE)
    private College college;

    @Column(nullable=false,name = "username",unique = true)
    @ApiModelProperty(notes = "账号")
    private String username;//账号

    @Column(nullable=false,name = "password")
    @ApiModelProperty(notes = "密码")
    private String password;//密码

    @Column(length = 100,name = "wecat")
    @ApiModelProperty(notes = "微信号")
    private String wecat;//微信号

    @Column(length = 50,name = "phone")
    @ApiModelProperty(notes = "手机号")
    private String phone;//手机号

    @Column(length = 80,name = "email")
    @ApiModelProperty(notes = "邮箱")
    private String email;//邮箱

    @Column(name = "last_password_reset_date")
    @ApiModelProperty(notes = "上次密码重置时间")
    private Date lastPasswordResetDate;//上次密码重置时间

    @Column(name = "is_synchro")
    @ApiModelProperty(notes = "是否与微信同步")
    private Integer isSynchro = 0;//0是没同步，1是同步

    @Column(name = "wecatid")
    @ApiModelProperty(notes = "微信名")
    private Long wecatid ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWecat() {
        return wecat;
    }

    public void setWecat(String wecat) {
        this.wecat = wecat;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Long getWecatid() {
        return wecatid;
    }

    public void setWecatid(Long wecatid) {
        this.wecatid = wecatid;
    }

    public User() {
    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public Integer getIsSynchro() {
        return isSynchro;
    }

    public void setIsSynchro(Integer isSynchro) {
        this.isSynchro = isSynchro;
    }
}
