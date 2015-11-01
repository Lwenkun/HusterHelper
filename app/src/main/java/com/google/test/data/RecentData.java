package com.google.test.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 15119 on 2015/10/18.
 */
public class RecentData {

    public static final int AVERAGE = 0;

    public static final int DATA_MAX_ELECTRICITY = 1;

    private static final int DAYS_NUM = 7;

    private static SharedPreferences sharedPreferences;

    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences("DaysInfo", Context.MODE_PRIVATE);
    }

    public static void putRecentData(List<DayInfo> daysInfo) {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (int i = 0; i < DAYS_NUM; i++) {
            editor.putString("weekday" + i, daysInfo.get(i).getWeekday());
            editor.putString("sDate" + i, daysInfo.get(i).getSDate());
            editor.putFloat("electricity" + i, daysInfo.get(i).getElectricity());
        }

        editor.apply();
    }

    public static List<DayInfo> getRecentData() {

        List<DayInfo> daysInfo = new ArrayList<>();

        for (int i = 0; i < DAYS_NUM; i++) {
            String weekday = sharedPreferences.getString("weekday" + i, "");
            String sDate = sharedPreferences.getString("sDate" + i, "");
            float electricity = sharedPreferences.getFloat("electricity" + i, 0);
            daysInfo.add(new DayInfo(weekday, sDate, electricity));
        }

        return daysInfo;
    }

    public static float getData(int dataType) {

        List<DayInfo> daysInfo = getRecentData();

        float[] e = new float[7];
        for (int i = 0; i <= 6; i++) {
            e[i] = daysInfo.get(i).getElectricity();
        }
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6 - i; j++) {
                if (e[j] > e[j + 1]) {
                    float temp = e[j];
                    e[j] = e[j + 1];
                    e[j + 1] = temp;
                }
            }

        switch (dataType) {
            case DATA_MAX_ELECTRICITY:
                return e[6];
            case AVERAGE:
                return (e[6] - e[0])/6;
            default:
                return 0;
        }
    }
}