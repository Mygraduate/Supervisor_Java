package com.graduate.system.arrage.dto;

/**
 * Created by konglinghai on 2017/5/20.
 */
public class SpareTimeConfig {
    private Long uid;
    private int weekListen;
    private int dayListen;
    private int total;

    public SpareTimeConfig(Long uid, int weekListen, int dayListen, int total) {
        this.uid = uid;
        this.weekListen = weekListen;
        this.dayListen = dayListen;
        this.total = total;
    }



    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
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

    public void add(){
        this.total++;
        this.dayListen++;
        this.weekListen++;
    }

    public void restWeek(){
        this.weekListen=0;
    }

    public void restDay(){
        this.dayListen=0;
    }
}
