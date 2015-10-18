package com.google.test.network;

import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 15119 on 2015/10/14.
 */
public class HttpUtil {

    private static URL mUrl;

    private static String mParams;

    private static Handler mHandler;

    public static String mRequestMethod;

    //设置URL
    public static void setUrl(URL url) {
        mUrl = url;
    }

    //设置参数
    public static void setParams(String params) {
        mParams = params;
    }

    //设置消息处理对象
    public static void setHandler(Handler handle) {
        mHandler = handle;
    }

    //设置请求方式
    public static void setRequestMethod(String requestMethod) {
        mRequestMethod = requestMethod;
    }

    /**
     * 对请求方式分类，并通知相应方法执行
     */
    public static void sendRequest() {
        switch (mRequestMethod) {
            case "GET":
                sendGetRequest();
                break;
            case "DELETE":
            case "POST":
                sendDeleteOrPostRequest();
                break;
        }
    }

    /**
     * 处理GET请求
     */
    public static void sendGetRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) mUrl.openConnection();
                    connection.setRequestMethod(mRequestMethod);
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Message message = new Message();
                    JSONObject data = new JSONObject(response.toString());
                    message.obj = data;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * 处理DELETE或POST请求
     */
    public static void sendDeleteOrPostRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) mUrl.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod(mRequestMethod);
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.writeBytes(mParams);
                    out.flush();
                    out.close();
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    Message message = new Message();
                    JSONObject data = new JSONObject(response.toString());
                    message.obj = data;
                    mHandler.sendMessage(message);
                }catch(Exception e) {
                    e.printStackTrace();
                }finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }


}
