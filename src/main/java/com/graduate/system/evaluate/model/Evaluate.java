package com.graduate.system.evaluate.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by konglinghai on 2017/3/19.
 * 督导评价表
 */
@Table(name = "evaluate")
@Entity
public class Evaluate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false,name = "tid")
    private Long tid;//教师id

    @Column(nullable=false,name = "creator")
    private Long creator;//创建人id

    @Column(name = "grade")
    private Integer grade = 0;//分数

    @Column(name = "path")
    private String path;//评价表保存路径

    @Column(nullable=false,name = "create_time")
    private Date createTime;//创建时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
