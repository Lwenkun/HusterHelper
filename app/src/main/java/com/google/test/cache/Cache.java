package com.google.test.cache;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 15119 on 2015/10/18.
 */
public class Cache {

    protected SharedPreferences sharedPreferences;

    protected SharedPreferences.Editor editor;

    public Cache(Context context, String name) {
        this.sharedPreferences = context.getSharedPreferences(name, 0);
        editor = sharedPreferences.edit();
    }
}
