package com.google.test.cache;

import android.content.Context;

/**
 * Created by 15119 on 2015/10/18.
 */
public class RoomCache extends Cache{

    public RoomCache(Context context) {
        super(context, "RoomInfo");
    }

    public String getArea() {
       return sharedPreferences.getString("area", "");
    }

    public String getBuild() {
        return sharedPreferences.getString("build", "");
    }

    public String getRoom() {
        return sharedPreferences.getString("room", "");
    }

    public void putArea(String area) {
        editor.putString("area", area);
    }

    public void putBuild(String build) {
        editor.putString("build", build);
    }

    public void putRoom(String room) {
        editor.putString("room", room);
    }

    public void putEmail(String email) {
        editor.putString("email", email);
    }

}
