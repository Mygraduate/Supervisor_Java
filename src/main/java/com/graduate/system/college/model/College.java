package com.graduate.system.college.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import javax.persistence.*;
/**
 * Created by konglinghai on 2017/3/19.
 */
@Table(name = "college")
@Entity
public class College {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false,name = "name")
    private String name;//学院名称

    @Column(name = "des")
    private String des;//描述



    public College() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

}
