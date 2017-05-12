package com.graduate.system.config.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by konglinghai on 2017/3/19.
 * 系统设置表
 */
@Table(name = "config")
@Entity
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false,name = "begin_time")
    private Date beginTime;//开学时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }
}
