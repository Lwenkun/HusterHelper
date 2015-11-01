package com.google.test.net;

import android.content.Context;
import android.util.Log;

import com.google.test.Interface.CallBack;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by 15119 on 2015/10/23.
 */
public class HttpRequestRunnable implements Runnable {

    private Context context;

    private String requestMethod;

    private HashMap<String, String> map;

    private URL url;

    private CallBack callBack;

    private String params;

    public HttpRequestRunnable(Context context, String requestMethod, HashMap<String, String> map, String url, CallBack callBack) {

        this.context = context;
        this.requestMethod = requestMethod;
        this.map = map;
        this.callBack = callBack;

        Log.d("MyRunnable", url);
        try {
            this.url = new URL(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //参数组装
        if (! "GET".equals(requestMethod)) {
            StringBuilder paramsBuilder = new StringBuilder();

            Set<String> keySet = map.keySet();

            boolean isFirstParam = true;

            for (String key : keySet) {
                if (isFirstParam) {
                    paramsBuilder.append(key + "=" + map.get(key));
                    isFirstParam = false;
                } else {
                    paramsBuilder.append("&" + key + "=" + map.get(key));
                }
            }

            this.params = paramsBuilder.toString();
        }
    }

    @Override
    public void run() {
        HttpURLConnection connection = null;

        try {

            //创建http连接
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);

            //如果是POST或DELETE方法
            if (! "GET".equals(requestMethod)) {

                connection.setDoInput(true);
                connection.setDoOutput(true);

                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(params);
                out.flush();
                out.close();
            }

            //从服务器获取数据
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);

            }

            callBack.updateUI(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

}
