package com.google.test.cache;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 15119 on 2015/10/18.
 */
public class RoomInfo{

    protected static SharedPreferences sharedPreferences;

    private getInstance() {
    return
    }
}

    protected SharedPreferences.Editor editor;

    public Cache(Context context, String name) {
        this.sharedPreferences = context.getSharedPreferences(name, 0);
        editor = sharedPreferences.edit();

    public static String getArea() {
       return sharedPreferences.getString("area", "");
    }

    public static String getBuild() {
        return sharedPreferences.getString("build", "");
    }

    public static String getRoom() {
        return sharedPreferences.getString("room", "");
    }

    public static void putArea(String area) {
        editor.putString("area", area);
    }

    public static void putBuild(String build) {
        editor.putString("build", build);
    }

    public static void putRoom(String room) {
        editor.putString("room", room);
    }

    public static void putEmail(String email) {
        editor.putString("email", email);
    }

}
