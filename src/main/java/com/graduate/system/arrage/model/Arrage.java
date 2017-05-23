package com.graduate.system.arrage.model;

import com.graduate.system.college.model.College;
import com.graduate.system.course.model.Course;
import com.graduate.system.teacher.model.Teacher;
import com.graduate.system.user.model.User;

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
    @OneToOne(cascade = {CascadeType.REFRESH} )
    @JoinColumn(name="course_id",referencedColumnName = "id",insertable = false,updatable = false)
    private Course course;


    @Column(nullable=false,name = "college_id")
    private Long  collegeId;//学院id

    @OneToOne(cascade = {CascadeType.REFRESH} )
    @JoinColumn(name="college_id",referencedColumnName = "id",insertable = false,updatable = false)
    private College college;

    @Column(nullable=false,name = "tid")
    private Long tid;//教师id
    @OneToOne(cascade = {CascadeType.REFRESH} )
    @JoinColumn(name="tid",referencedColumnName = "id",insertable = false,updatable = false)
    private Teacher teacher;

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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
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
}
