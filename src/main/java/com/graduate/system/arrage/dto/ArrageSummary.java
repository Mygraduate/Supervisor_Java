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

    public void addWeekNum(int week){
        Boolean isFind = false;
        for(WeekSummary weekSummary : this.weekSummaries){
            if(weekSummary.getWeek() == week){
                weekSummary.addNum();
                isFind = true;
            }
        }
        if(!isFind){
            addWeekSummary(week);
        }
    }

    private void addWeekSummary(int week){
        WeekSummary weekSummary = new WeekSummary();
        weekSummary.setWeek(week);
        weekSummary.addNum();
        this.weekSummaries.add(weekSummary);
    }
    public void setWeekSummaries(List<WeekSummary> weekSummaries) {
        this.weekSummaries = weekSummaries;
    }

    public class WeekSummary{
        private int num = 0;
        private int week;

        public int getnum() {
            return num;
        }

        public void setnum(int num) {
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
    }
}
