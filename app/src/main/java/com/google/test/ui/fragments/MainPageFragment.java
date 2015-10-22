package com.google.test.ui.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.test.R;
import com.google.test.ui.activities.SetAlarm;
import com.google.test.cache.RoomCache;
import com.google.test.handlers.EleHandler;
import com.google.test.json.EleJSONParser;
import com.google.test.net.HttpUtil;

import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by 15119 on 2015/10/5.
 */
public class MainPageFragment extends Fragment implements OnClickListener,EleJSONParser.CallBack {

    private final RoomCache roomCache = new RoomCache(getContext());

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



    private void initView(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.activity_main_main_page, null);
        ImageView setAlarm = (ImageView) mView.findViewById(R.id.set_alarm);
        setAlarm.setOnClickListener(this);
        tv_electricity = (TextView) mView.findViewById(R.id.electricity);
        tv_average = (TextView) mView.findViewById(R.id.average);
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

    /**e
     * 等待子线程获取数据完毕后更新界面
     */
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

}