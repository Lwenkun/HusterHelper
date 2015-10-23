package com.google.test.net;

import android.content.Context;

import com.google.test.json.CallBack;

import java.net.URL;
import java.util.HashMap;

/**
 * Created by 15119 on 2015/10/22.
 */
public class AnsynHttpRequest  {

    private static void doAnsynHttpRequest(Context context,String requestMethod, HashMap<String, String> map, URL url, boolean showDialog, CallBack callBack ) {

        if(showDialog) {

        }
        Runnable r = MyRunnable.newRunnable(context, map, url, callBack);
        ThreadPoolUtils.execute(r);
    }

    private static void doGetRequest(Context context, HashMap<String, String> map, URL url, boolean showDialog, CallBack callBack) {

        doAnsynHttpRequest(context, "GET", map, url, showDialog, callBack);
    }

    private static void doPostRequest(Context context, HashMap<String, String> map, URL url, boolean showDialog, CallBack callBack) {

        doAnsynHttpRequest(context, "POST", map, url, showDialog, callBack);
    }
}
