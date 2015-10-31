package com.google.test.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.test.json.CallBack;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by 15119 on 2015/10/22.
 */
public class AnsynHttpRequest {

    private static ProgressDialog mDialog;

    private static void doAnsynHttpRequest(Context context, String requestMethod, HashMap<String, String> map, String url, boolean showDialog, CallBack callBack) {

        //判断是否需要对话框
        if (showDialog) {
            showDialog();
        }

        ThreadPoolUtils.execute(new MyRunnable(context, requestMethod, map, url, callBack));
    }

    //处理Get请求
    public static void doGetRequest(Context context, HashMap<String, String> map, String url, boolean showDialog, CallBack callBack) {

        StringBuilder urlBuilder = new StringBuilder(url);

        boolean isFirstParam = true;

        //获取键值集
        Set<String> keySet = map.keySet();

        //组装url
        for (String key : keySet) {
            if (isFirstParam) {
                    urlBuilder.append(key + "=" + map.get(key));
                Log.d("test4", "utf-8" + urlBuilder.toString());
                isFirstParam = false;
            } else {
                urlBuilder.append("&" + key + "=" + map.get(key));
            }
        }

        url = urlBuilder.toString();

        Log.d("test4", url);

        doAnsynHttpRequest(context, "GET", null, url, showDialog, callBack);
    }

    //处理Post请求
    public static void doPostRequest(Context context, HashMap<String, String> map, String url, boolean showDialog, CallBack callBack) {

        doAnsynHttpRequest(context, "POST", map, url, showDialog, callBack);
        Log.d("test4", url);
    }

    //处理Delete请求
    public static void doDeleteRequest(Context context, HashMap<String, String> map, String url, boolean showDialog, CallBack callBack) {

        doAnsynHttpRequest(context, "DELETE", map, url, showDialog, callBack);
    }

    //设置对话框
    public static void setDialog(ProgressDialog dialog) {
        mDialog = dialog;
    }

    //显示对话框
    private static void showDialog() {
        mDialog.show();
    }
}
