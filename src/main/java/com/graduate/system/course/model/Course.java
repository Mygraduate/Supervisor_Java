package com.graduate.system.course.model;

import javax.persistence.*;

/**
 * Created by konglinghai on 2017/3/19.
 * 课程表
 */
@Table(name = "course")
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false,name = "cid")
    private Long  cid;//学院id

    @Column(nullable=false,name = "tid")
    private Long  tid;//教师id

    @Column(nullable=false,name = "teacher")
    private String teacher;//教师姓名

    @Column(nullable=false,name = "name")
    private String  name;//课程名称

    @Column(nullable=false,name = "content")
    private String  content;//授课内容

    @Column(nullable=false,name = "type")
    private String  type;//授课方式

    @Column(nullable=false,name = "address")
    private String  address;//上课地点

    @Column(name = "major")
    private String  major;//专业

    @Column(name = "week")
    private Integer week;//上课周

    @Column(name = "day")
    private Integer day;//上课天

    @Column(name = "scope")
    private String scope;//上课节次

    @Column(name = "time")
    private String time;//上课时间

    @Column(name="grade")
    private String grade;//年级

    @Column(name="classes")
    private String classes;//班级

    @Column(name = "is_arrange")
    private Integer isArrange = 0;//是否被安排 0：代表未安排 1：代表被安排,默认是0

    private int memberCount = 0;//督导成员数量

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Integer getIsArrange() {
        return isArrange;
    }

    public void setIsArrange(Integer isArrange) {
        this.isArrange = isArrange;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
