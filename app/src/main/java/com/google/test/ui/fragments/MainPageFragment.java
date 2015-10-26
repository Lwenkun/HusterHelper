package com.google.test.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.test.R;
import com.google.test.cache.DayInfo;
import com.google.test.common.C;
import com.google.test.handlers.EleHandler;
import com.google.test.json.CallBack;
import com.google.test.net.AnsynHttpRequest;
import com.google.test.ui.activities.SetAlarm;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 15119 on 2015/10/5.
 */
/*public class MainPageFragment extends Fragment implements OnClickListener {

    private View mView;

    private TextView tv_electricity;

    private TextView tv_average;

    private ProgressDialog progressDialog;

    private double angle;

    private float maxElectricity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("更新数据中，请稍后...");
        progressDialog.show();
        initView(inflater);
        sendRequest();
     //   drawCircle(mView, 0);
        return mView;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_alarm:
                Intent intent = new Intent(getActivity(), SetAlarm.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void updateUI() {

    }

    public void sendRequest() {
        SharedPreferences roomInfo = getActivity().getSharedPreferences("RoomInfo", 0);
        String area = roomInfo.getString("area", "");
        String buildNum = roomInfo.getString("buildNum", "");
        String roomNum = roomInfo.getString("roomNum", "");
        URL url = null;
        try {
            url = new URL("http://api.hustonline.net/dianfei?area="
                    + URLEncoder.encode(area, "UTF-8")
                    + "&build=" + buildNum
                    + "&room=" + roomNum);
        }catch (Exception e) {
            e.printStackTrace();
        }
        HttpUtil.setUrl(url);
        HttpUtil.setRequestMethod("GET");
        HttpUtil.setHandler(new EleHandler(getContext()));
        HttpUtil.sendRequest();
    }

    public void drawCircle(int v) {

        RelativeLayout showElectricity = (RelativeLayout) mView.findViewById(R.id.show_electricity);

        if (v == 0) {
            final DrawGrayCircle drawer = new DrawGrayCircle(getActivity());
            drawer.setMinimumHeight(250);
            drawer.setMinimumWidth(300);
            drawer.invalidate();
            showElectricity.addView(drawer);
        } else {
            final DrawGreenCircle drawer = new DrawGreenCircle(getActivity());
            drawer.setMinimumWidth(300);
            drawer.setMinimumHeight(250);
            drawer.invalidate();
            showElectricity.addView(drawer);
        }

    }

    public void drawRecentElectricity(View view) {

        LinearLayout showRecentElectricity = (LinearLayout) view.findViewById(R.id.show_recent_electricity);
        final DrawRecentElectricity drawer = new DrawRecentElectricity(getActivity());
        drawer.setMinimumHeight(300);
        drawer.setMinimumWidth(400);
        drawer.invalidate();
        showRecentElectricity.addView(drawer);
    }

    public class DrawGrayCircle extends View {

        private final Paint p = new Paint();

        public DrawGrayCircle(Context context) {
            super(context);
            p.setStrokeWidth(30);
            p.setStyle(Paint.Style.STROKE);
            p.setColor(Color.LTGRAY);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int canvasW = canvas.getHeight();
            canvas.drawCircle(canvasW / 2, canvasW / 2, canvasW / 2 - 25, p);
    }
    }

    public class DrawGreenCircle extends View {

        private final Paint p = new Paint();

        private final RectF oval = new RectF(25, 25, 0, 0);

        public DrawGreenCircle(Context context) {
            super(context);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int canvasH = canvas.getHeight();
            oval.right = canvasH - 25;
            oval.bottom = canvasH - 25;
            p.setColor(getResources().getColor(R.color.mainColor));
            p.setStrokeWidth(45);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawArc(oval, -90, (float) angle, false, p);
        }
    }

}*/
public class MainPageFragment extends Fragment implements View.OnClickListener, CallBack {

    private Context mContext;

    private TextView tv_electricity;

    private TextView tv_average;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = getContext();
     //   refresh();
        return loadContentView(inflater);
    }

    public View loadContentView(LayoutInflater inflater) {

        View view;

        if (! isNetworkAvailable()) {

            view = inflater.inflate(R.layout.activity_main_no_network, null);

        } else if (getContext().getSharedPreferences("RoomInfo", 0) == null) {

            view = inflater.inflate(R.layout.activity_main_first_use, null);
        } else {
            view = inflater.inflate(R.layout.fragment_main_page, null);
            initView(view);
        }

        return view;
    }

    public boolean isNetworkAvailable() {

        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo().isAvailable();
    }

    private void initView(View view) {

        ImageView iv_setAlarm = (ImageView) view.findViewById(R.id.set_alarm);
        iv_setAlarm.setOnClickListener(this);
        tv_electricity = (TextView) view.findViewById(R.id.electricity);
        tv_average = (TextView) view.findViewById(R.id.average);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_alarm:
                Intent intent = new Intent(getActivity(), SetAlarm.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void updateUI(String response) {

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("EEEE");
        SimpleDateFormat format3 = new SimpleDateFormat("MM月dd日");

        List<DayInfo> daysInfo = new ArrayList<>();
        try {
            JSONObject jResponse = new JSONObject(response);
            int code = jResponse.getInt("code");
            String latest = jResponse.getJSONObject("data").getString("remain");
            if (latest.equals("null")) {
                Toast.makeText(mContext, C.notice.FAIL_TO_GET_REMAINING_ELECTRICITY, Toast.LENGTH_SHORT).show();
            } else {


//                angle = Math.log10(Double.parseDouble(latest)) * 50;
//
//                drawCircle(1);
//
//                mTvElectricity.setText(latest);


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

                EleHandler handler = new EleHandler(mContext);
                handler.sendMessage(msg);
//                float range = calRange();
//                mTvAverage.setText(String.format("%.2f", range / 6));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendRequest() {
        AnsynHttpRequest.doGetRequest(mContext, null, C.url.GET_ELECTRICITY, true, this);
    }


}