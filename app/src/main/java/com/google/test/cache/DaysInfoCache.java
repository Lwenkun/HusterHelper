package com.google.test.cache;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 15119 on 2015/10/18.
 */
public class DaysInfoCache {

    private static final int DAYS_NUM = 7;

    private static SharedPreferences sharedPreferences;

    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences("DaysInfo", Context.MODE_PRIVATE);
    }

    public static void putDaysInfo(List<DayInfo> daysInfo) {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        for(int i =0; i < DAYS_NUM; i++) {
            editor.putString("weekday" + i, daysInfo.get(i).getWeekday());
            editor.putString("sDate" + i, daysInfo.get(i).getSDate());
            editor.putFloat("electricity" + i, daysInfo.get(i).getElectricity());
        }

        editor.apply();
    }

    public static List<DayInfo> getDaysInfo() {

        List<DayInfo> daysInfo = new ArrayList<>();

        for(int i = 0; i < DAYS_NUM; i++) {
            String weekday = sharedPreferences.getString("weekday" + i, "");
            String sDate = sharedPreferences.getString("sDate" + i, "");
            float electricity = sharedPreferences.getFloat("electricity", 0);
            daysInfo.add(new DayInfo(weekday, sDate, electricity));
        }

        return daysInfo;
    }
}