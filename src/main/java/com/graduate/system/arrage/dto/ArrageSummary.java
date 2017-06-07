package com.graduate.system.arrage.dto;

import com.graduate.system.user.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konglinghai on 2017/6/3.
 */
public class ArrageSummary {
    private User user;
    private int total = 0;
    private int chiefNum = 0;
    private List<WeekSummary> weekSummaries = new ArrayList<>();

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<WeekSummary> getWeekSummaries() {
        return weekSummaries;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addTotal(){
        this.total++;
    }

    public void addWeekNum(int week,boolean isChief){
        Boolean isFind = false;

        for(WeekSummary weekSummary : this.weekSummaries){
            if(weekSummary.getWeek() == week){
                weekSummary.addNum();
                weekSummary.setChief(isChief);
                isFind = true;
            }
        }
        if(!isFind){
            addWeekSummary(week,isChief);
        }
    }

    private void addWeekSummary(int week,Boolean isChief){
        WeekSummary weekSummary = new WeekSummary();
        weekSummary.setWeek(week);
        weekSummary.addNum();
        weekSummary.setChief(isChief);
        this.weekSummaries.add(weekSummary);
    }
    public void setWeekSummaries(List<WeekSummary> weekSummaries) {
        this.weekSummaries = weekSummaries;
    }

    public class WeekSummary{
        private int num = 0;
        private int week;
        private Boolean isChief = false;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getWeek() {
            return week;
        }

        public void setWeek(int week) {
            this.week = week;
        }

        public void addNum(){
            this.num++;
        }

        public void isChief(){
            this.isChief = true;
        }

        public Boolean getChief() {
            return isChief;
        }

        public void setChief(Boolean chief) {
            if(chief){
                isChief = chief;
            }
        }
    }

    public int getChiefNum() {
        return chiefNum;
    }

    public void setChiefNum(int chiefNum) {
        this.chiefNum = chiefNum;
    }

    public void addChiefNum(){
        this.chiefNum++;
    }
}
