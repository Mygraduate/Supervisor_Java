package com.graduate.system.arrage.dto;

/**
 * Created by konglinghai on 2017/5/20.
 */
//自动排课配置
public class ArrageConfig {


    //开始的星期
    private int startDay = 1;

    //起始周
    private int startWeek = 1;

    //终止周
    private int endWeek = 20;

    //督导员一周的总安排次数
    private int weekListen = 1;

    //督导员一天的总安排次数
    private int dayListen = 1;

    //本学期的听课安排数
    private int total;

    //实验：理论
    private int apercent = 50;

    //督导组最小人数
    private int minPeople = 1;

    //督导组最大人数
    private int maxPeople = 1;

    public int getStartDay() {
        return startDay;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public int getWeekListen() {
        return weekListen;
    }

    public void setWeekListen(int weekListen) {
        this.weekListen = weekListen;
    }

    public int getDayListen() {
        return dayListen;
    }

    public void setDayListen(int dayListen) {
        this.dayListen = dayListen;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double getApercent() {
        return apercent;
    }

    public void setApercent(int apercent) {
        this.apercent = apercent;
    }

    public int getMinPeople() {
        return minPeople;
    }

    public void setMinPeople(int minPeople) {
        this.minPeople = minPeople;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }


}
