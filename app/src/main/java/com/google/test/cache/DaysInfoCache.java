package com.google.test.cache;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 15119 on 2015/10/18.
 */
public class DaysInfoCache extends Cache{

    private final int DAYS_NUM = 7;

    public DaysInfoCache(Context context) {
        super(context, "DaysInfo");
    }

    public void putDaysInfo(ArrayList<DayInfo> daysInfo) {
        for(int i =0; i < DAYS_NUM; i++) {
            editor.putString("weekday" + i, daysInfo.get(i).getWeekday());
            editor.putString("sDate" + i, daysInfo.get(i).getSDate());
            editor.putFloat("electricity" + i, daysInfo.get(i).getElectricity());
        }
    }

    public List<DayInfo> getDaysInfo() {

        List<DayInfo> daysInfo = new ArrayList<DayInfo>();

        for(int i = 0; i < DAYS_NUM; i++) {
            String weekday = sharedPreferences.getString("weekday" + i, "");
            String sDate = sharedPreferences.getString("sDate" + i, "");
            float electricity = sharedPreferences.getFloat("electricity", 0);
            DayInfo dayInfo = new DayInfo(weekday, sDate, electricity);
            daysInfo.add(dayInfo);
        }

        return daysInfo;
    }
}