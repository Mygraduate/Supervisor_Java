package com.graduate.system.log.model;

/**
 * Created by konglinghai on 2017/3/19.
 */

import javax.persistence.*;
import java.util.Date;

/**
 * Created by konglinghai on 2017/3/19.
 * 系统日志
 */
@Table(name = "Log")
@Entity
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false,name = "type")
    private Integer type;//0,1,2,3....代表不同的日志类型后续定义

    @Column(name = "text")
    private String text;//描述

    @Column(nullable=false,name = "create_time")
    private Date createTime;//创建时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
