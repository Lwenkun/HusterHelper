package com.google.test.ui.activities;

import android.app.Application;

import com.google.test.data.DayInfo;

import java.util.List;

/**
 * Created by 15119 on 2015/10/29.
 */
public class MyApplication extends Application {

    private List<DayInfo> daysInfo;

    @Override
    public void onCreate() {

        super.onCreate();

    }

    public void saveDaysInfo(List<DayInfo> daysInfo) {

        this.daysInfo = daysInfo;
    }

    public List<DayInfo> getDaysInfo() {

        return daysInfo;
    }
}
