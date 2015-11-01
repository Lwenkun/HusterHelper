package com.google.test.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.test.R;
import com.google.test.common.C;
import com.google.test.data.DayInfo;
import com.google.test.data.RecentData;
import com.google.test.ui.activities.SetAlarmActivity;

import net.AnsynHttpRequest;
import net.CallBack;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 15119 on 2015/10/5.
 */

public class MainPageFragment extends Fragment implements View.OnClickListener, CallBack {

    private Activity mActivity;

    private TextView tv_electricity;

    private TextView tv_average;

    private MyHandler mHandler = new MyHandler();

    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 200:
                    RecentData.init(mActivity);
                    RecentData.putRecentData((List<DayInfo>) msg.obj);
                    draw();
                    tv_average.setText(String.format("%.2f", RecentData.getData(RecentData.AVERAGE)));
                    break;

                case 402:
                    Toast.makeText(mActivity, "很抱歉，没有该寝室的信息", Toast.LENGTH_SHORT).show();
                    break;

                case 400:
                    Toast.makeText(mActivity, "操作错误，请稍后重试", Toast.LENGTH_SHORT).show();
                    break;

                case 500:
                    Toast.makeText(mActivity, "服务器发生未知错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mActivity = getActivity();
       // refresh();
        sendRequest();
        return loadContentView(inflater, container);
    }

    public View loadContentView(LayoutInflater inflater, ViewGroup container) {

        View view;

        Log.d("MainPageFragment", "loadContentView");

//        if (isNetworkAvailable()) {
//
//            view = inflater.inflate(R.layout.activity_main_no_network, container, false);
//
//        } else if ("".equals(mActivity.getSharedPreferences("RoomInfo", Context.MODE_PRIVATE).getString("area", ""))) {
//
//            view = inflater.inflate(R.layout.activity_main_first_use, container, false);
//        } else {

            view = inflater.inflate(R.layout.fragment_main_page, container, false);
            initView(view);
//        }

        return view;
    }

//    public boolean isNetworkAvailable() {
//
//        ConnectivityManager manager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (manager.getActiveNetworkInfo().isAvailable() && manager.getActiveNetworkInfo().isConnected()) return true;
//        return false;
//    }

    private void initView(View view) {

        ImageView iv_setAlarm = (ImageView) view.findViewById(R.id.set_alarm);
        iv_setAlarm.setOnClickListener(this);
      //  tv_electricity = (TextView) view.findViewById(R.id.electricity);
        tv_average = (TextView) view.findViewById(R.id.average);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_alarm:
                Intent intent = new Intent(getActivity(), SetAlarmActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void updateUI(String response) {

        Log.d("MainPageFragment", response);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("EEEE");
        SimpleDateFormat format3 = new SimpleDateFormat("MM月dd日");

        List<DayInfo> daysInfo = new ArrayList<>();
        try {

            //获得状态码和最近电量
            JSONObject jResponse = new JSONObject(response);
            int code = jResponse.getInt("code");
            String latest = jResponse.getJSONObject("data").getString("remain");

            if (latest.equals("null")) {
                Toast.makeText(mActivity, C.notice.FAIL_TO_GET_REMAINING_ELECTRICITY, Toast.LENGTH_SHORT).show();
            } else {
                Log.d("MainPageFragment", "i am here,too");

                JSONObject recent = jResponse.getJSONObject("data").getJSONObject("recent");
                Iterator<String> jStrings = recent.keys();

                while (jStrings.hasNext()) {

                    //获取下一个JSON对象
                    String jString = jStrings.next();
                    JSONObject jDayInfo = recent.getJSONObject(jString);

                    //获取每天的更新日期和剩余电量
                    Date date = format1.parse(jDayInfo.getString("updated_at"));
                    String weekday = format2.format(date);
                    String sDate = format3.format(date);
                    float electricity = Float.parseFloat(jDayInfo.getString("dianfei"));

                    //根据获取的信息组装一个dayInfo对象
                    DayInfo dayInfo = new DayInfo(weekday, sDate, electricity);
                    daysInfo.add(dayInfo);
                }

                Message msg = new Message();
                msg.what = code;
                msg.obj = daysInfo;

                mHandler.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendRequest() {
        SharedPreferences roomInfo =  mActivity.getSharedPreferences("RoomInfo", Context.MODE_PRIVATE);
        HashMap<String, String> params = new HashMap<>();
        params.put("area", roomInfo.getString("area", ""));
        params.put("room",  roomInfo.getString("roomNum", ""));
        params.put("build", roomInfo.getString("buildNum", ""));
        AnsynHttpRequest.doGetRequest(mActivity, params, C.url.GET_ELECTRICITY, false, this);
    }

    public void draw() {
        drawCircles();
        drawGraph();
    }

    public void drawCircles() {
        View circleView = mActivity.findViewById(R.id.show_electricity);
        circleView.invalidate();
    }

    public void drawGraph() {
        View graph = mActivity.findViewById(R.id.show_recent_electricity);
        graph.invalidate();
    }

}