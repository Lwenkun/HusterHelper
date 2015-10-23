package com.google.test.net;

import android.content.Context;
import android.util.Log;

import com.google.test.json.CallBack;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by 15119 on 2015/10/23.
 */
public class MyRunnable implements Runnable{

    private Context context;

    private String requestMethod;

    private HashMap<String, String> map;

    private URL url;

    private CallBack callBack;

    private String params;

    public MyRunnable(Context context, String requestMethod, HashMap<String, String> map, String url, CallBack callBack) {

        this.context = context;
        this.requestMethod = requestMethod;
        this.map = map;
        this.callBack = callBack;

        try{
            this.url = new URL(URLEncoder.encode(url, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("test", "URL Transformation error");
        }

        if(map != null) {
            StringBuilder paramsBuilder = new StringBuilder(url);

            map.keySet();
            Set<String> keySet = map.keySet();

            boolean isFirstParam = true;

            for(String key : keySet) {
                if(isFirstParam) {
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

            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setRequestMethod(requestMethod);

            //如果是POST或DELETE方法
            if(params != null) {
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
            while ((line = reader.readLine()) != null){
                response.append(line);
            }
            //Message message = new Message();
            Log.d("test", "response:" + response.toString());
            callBack.updateUI(response.toString());

//            JSONObject data = new JSONObject(response.toString());
//            message.obj = data;
//            mHandler.sendMessage(message);
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

}
