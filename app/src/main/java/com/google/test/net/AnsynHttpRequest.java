package com.google.test.net;

import android.app.ProgressDialog;
import android.content.Context;

import com.google.test.json.CallBack;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by 15119 on 2015/10/22.
 */
public class AnsynHttpRequest {

    private static ProgressDialog mDialog;

    private static void doAnsynHttpRequest(Context context, String requestMethod, HashMap<String, String> map, String url, boolean showDialog, CallBack callBack) {

        if (showDialog) {
            showDialog();
        }

        ThreadPoolUtils.execute(new MyRunnable(context, requestMethod, map, url, callBack));
    }

    public static void doGetRequest(Context context, HashMap<String, String> map, String url, boolean showDialog, CallBack callBack) {

        StringBuilder urlBuilder = new StringBuilder(url);

        boolean isFirstParam = true;

        Set<String> keySet = map.keySet();

        for (String key : keySet) {
            if (isFirstParam) {
                urlBuilder.append(map.get(key));
                isFirstParam = false;
            } else {
                urlBuilder.append(map.get(key));
            }
        }

        url = urlBuilder.toString();

        doAnsynHttpRequest(context, "GET", null, url, showDialog, callBack);
    }

    public static void doPostRequest(Context context, HashMap<String, String> map, String url, boolean showDialog, CallBack callBack) {

        doAnsynHttpRequest(context, "POST", map, url, showDialog, callBack);
    }

    public static void doDeleteRequest(Context context, HashMap<String, String> map, String url, boolean showDialog, CallBack callBack) {

        doAnsynHttpRequest(context, "POST", map, url, showDialog, callBack);
    }

    public static void  setDialog(ProgressDialog dialog) {
       mDialog = dialog;
    }

    private static void showDialog() {
        mDialog.show();
    }
}
