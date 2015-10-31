package com.google.test.data;

/**
 * Created by 15119 on 2015/10/18.
 */
public class DayInfo {

    private String weekday;

    private String sDate;

    private float electricity;

    public DayInfo(String weekday, String sDate, float electricity) {
        this.weekday = weekday;
        this.sDate = sDate;
        this.electricity = electricity;
    }

    public String getWeekday() {
        return weekday;
    }

    public String getSDate() {
        return sDate;
    }

    public float getElectricity() {
        return electricity;
    }
}
