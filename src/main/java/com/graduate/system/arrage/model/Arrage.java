package com.graduate.system.arrage.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import javax.persistence.*;
/**
 * Created by konglinghai on 2017/3/19.
 * 督导听课安排表
 */
@Table(name = "arrage")
@Entity
public class Arrage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false,name = "course_id")
    private Long courseId;//课程id

    @Column(nullable=false,name = "college_id")
    private Long  collegeId;//学院id

    @Column(nullable=false,name = "tid")
    private Long tid;//教师id

    @Column(nullable=false,name = "groups")
    private String groups;//督导组


    @Column(nullable=false,name = "status")
    private Integer status;//0：未确定，1：代表是已确定，2：代表是待执行，3：代表已执行

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(Long collegeId) {
        this.collegeId = collegeId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }
}
