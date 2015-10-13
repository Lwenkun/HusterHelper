package com.google.test;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 15119 on 2015/10/5.
 */
public class ContentView extends Fragment implements OnClickListener {
	
	private String area;

    private String buildNum;

    private String roomNum;

    private TextView electricity;

    private TextView average;

    private double angle;

    private ProgressDialog progressDialog;

    private List<DayInfo> daysInfo;

    private  float range;

    private float maxElectricity;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            progressDialog.dismiss();
            switch (msg.what) {
                case 200:
                    String response = (String) msg.obj;
                    Log.d("test4", "response");
                    parseJSONForDetail(response);
                    break;
                case 402:
                    Toast.makeText(getActivity(),"很抱歉，没有该寝室的信息", Toast.LENGTH_SHORT).show();
                    electricity.setText("T_T");
                    break;
                case 400:
                    Toast.makeText(getActivity(),"操作错误，请稍后重试",Toast.LENGTH_SHORT).show();
                    break;
                case 500:
                    Toast.makeText(getActivity(),"服务器发生未知错误",Toast.LENGTH_SHORT).show();
                    break;
                default:break;
            }
        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getActivity());
    //    progressDialog.setProgressStyle();
        progressDialog.setMessage("更新数据中，请稍后...");
        progressDialog.show();
     //   progressDialog.setVisibility(View.VISIBLE);
 
        View view = inflater.inflate(R.layout.main_content_view, null);
        ImageView setAlarm = (ImageView) view.findViewById(R.id.set_alarm);
        setAlarm.setOnClickListener(this);
        electricity = (TextView) view.findViewById(R.id.electricity);
        average = (TextView) view.findViewById(R.id.average);
        sendRequest();
        drawCircle(view, 0);
        return view;
    }


        
   @Override
   public void onClick(View v) {
	   switch (v.getId()) {
	   case R.id.set_alarm:
		   Intent intent = new Intent(getActivity(),SetAlarm.class);
		   startActivity(intent);
	   }
   }
    
    public void sendRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                SharedPreferences roomInfo = getActivity().getSharedPreferences("RoomInfo",getActivity().MODE_PRIVATE);
                area = roomInfo.getString("area","");
                buildNum = roomInfo.getString("buildNum", "");
                roomNum = roomInfo.getString("roomNum", "");
                try {
                    URL url = new URL("http://ct.hustonline.net/dianfei?area=" + URLEncoder.encode(area, "UTF-8") + "&build=" + buildNum + "&room=" + roomNum);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    Message message = new Message();
                    String data =response.toString();
             //       Log.d("test4", data);
                    message.what = parseJSONForCode(data);
                    message.obj = data;
                    handler.sendMessage(message);
                }catch(Exception e) {
                	//Log.d("test4", "Re");
                    e.printStackTrace();
                }finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
    
    private int parseJSONForCode(String response) {
        int code=0;
        try {
            code = new JSONObject(response).getInt("code");
        }catch (JSONException e) {
        //	Log.d("test4", "code");
            e.printStackTrace();
        }
        return code;
    }
    
    private void parseJSONForDetail(String response) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("EEEE");
        SimpleDateFormat format3 = new SimpleDateFormat("MM月dd日");
            daysInfo = new ArrayList<DayInfo>();
        try {
            JSONObject data = new JSONObject(response).getJSONObject("data");
            String latest = data.getString("remain");
          //  Log.d("test4", "latest"+ latest);
            if (latest.equals("null")) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction =  fragmentManager.beginTransaction();
                EmptyContentView defaultContentView = new EmptyContentView();
                transaction.replace(R.id.main_view_container, defaultContentView);
                transaction.commit();
                Toast.makeText(getActivity(), "查询电费失败了0.0", Toast.LENGTH_SHORT).show();
            } else {
            //    Log.d("test4", latest);
                double latestToDouble = Double.parseDouble(latest);
                angle = Math.log10(latestToDouble) * 50;
             //   Log.d("angle", "" + angle);
                drawCircle(getView(), 1);
                electricity.setText(latest);
                //  String test = data.getString("recent");
                //   Log.d("test4", test);
                JSONObject recent = data.getJSONObject("recent");
//
                Iterator<String> jStrings = recent.keys();
                //// Log.d("test4", jStrings.next()) ;
                //   Log.d("test4", jStrings.next());
                while (jStrings.hasNext()) {
                    String jString = jStrings.next();
                //    Log.d("test4", jString);
                    JSONObject jDayInfo = recent.getJSONObject(jString);
                    Date date = format1.parse(jDayInfo.getString("updated_at"));
                    String weekday = format2.format(date);
                    String sDate = format3.format(date);
                //    Log.d("test4", date);
                //    Log.d("test4", weekday);
               //     Log.d("test4", sDate);
                    float electricity = Float.parseFloat(jDayInfo.getString("dianfei"));
                   // Log.d("test4", electricity);
                    DayInfo dayInfo = new DayInfo(weekday, sDate, electricity);
                    daysInfo.add(dayInfo);
                }
                range = calRange();
                drawRecentElectricity(getView());
                // - daysInfo.get(0).getElectricity();
                average.setText(String.format("%.2f", range / 6));

                }
            }catch(Exception e) {
       //         Log.d("test4", "fordetail");
                e.printStackTrace();
            }

        }

    public float calRange() {
        float[] e = new float[7];
        for(int i = 0; i <= 6; i++) {
            e[i] = daysInfo.get(i).getElectricity();
        }
        for(int i = 0; i < 6; i++)
            for(int j = 0; j < 6 - i; j++){
                if (e[j] > e[j+1]) {
                    float temp = e[j];
                    e[j] = e[j+1];
                    e[j+1] = temp;
                }
            }
        maxElectricity = e[6];
        return e[6] - e[0];
    }
    

    public class DayInfo {

        private String weekday;

        private String sDate;

        private float electricity;

        public DayInfo(String weekday, String sDate, float electricity) {
            this.weekday = weekday;
            this.sDate = sDate;
            this.electricity = electricity;
        }

        public String getWeekday() {
            return weekday;
        }

        public String getSDate() {
            return sDate;
        }

        public float getElectricity() {
            return electricity;
        }
    }

    public void drawCircle(View view, int v) {

        RelativeLayout showElectricity  = (RelativeLayout) view.findViewById(R.id.show_electricity);

        if (v == 0){
            final DrawGrayCircle drawer = new DrawGrayCircle(getActivity());
            drawer.setMinimumHeight(250);
            drawer.setMinimumWidth(300);
            drawer.invalidate();
            showElectricity.addView(drawer);
        }else {
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
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int canvasW = canvas.getHeight();
            p.setStrokeWidth(30);
            p.setStyle(Paint.Style.STROKE);
            p.setColor(Color.LTGRAY);
            canvas.drawCircle(canvasW / 2, canvasW / 2, canvasW / 2 - 25, p);
        }
    }

    public class DrawGreenCircle extends View{

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

    public class  DrawRecentElectricity extends View{

        private final  Paint p = new Paint();

        private final Path path = new Path();

        private float circleX[];

        private float circleY[];

        private float interval;

        private float k;

        private int r;

        private float[] Y;

        private int position = 6;

        private  boolean i;

        private int canvasH;

        private  int canvasW;

        public DrawRecentElectricity(Context context) {
            super(context);
            p.setAntiAlias(true);
            Y = new float[7];
            circleX = new float[7];
            circleY = new float[7];
            i = true;
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if(i) {
                canvasH = canvas.getHeight();
                canvasW = canvas.getWidth();
                r = canvasH/4;
                interval = (canvasW - 2*r) / 6;
                for(int i = 0; i<=6; i++){
                    circleX[i] = r + i*interval;
                }
                k = (canvasH*2/5) / maxElectricity;
                for(int i=0; i<=6; i++) {
                    Y[6 - i] = canvasH - daysInfo.get(i).getElectricity() * k ;
                }
                for(int i = 0; i <=6; i++) {
                    circleY[i] = Y[i] - r - 50;
                }
                i = false;
            }
            p.setStyle(Paint.Style.FILL_AND_STROKE);
            p.setColor(Color.rgb(70, 222, 235));
            path.moveTo(0, canvasH);
            path.lineTo(0, Y[0]);
            path.lineTo(r, Y[0]);

            float startX = r;
           // canvas.drawLine(canvasW - startX, canvasH/2, canvasW, canvasH/2, p);
            for(int i=0; i<=5; i++) {
//                path.moveTo(startX, Y[i]);
                path.quadTo(startX +interval/2+20   ,(Y[i+1]+Y[i])/2, startX + interval, Y[i+1]);
               // path.quadTo(startX + interval, Y[i], startX + interval*2, Y[i+1]);
             //   startX += interval;
                startX += interval;
            }
            path.lineTo(canvasW , Y[6]);
            path.lineTo(canvasW, canvasH);
            path.close();
            canvas.drawPath(path, p);
            // p.reset();
          //  p.drawText(get)
            p.setStyle(Paint.Style.FILL);
            canvas.drawCircle(circleX[position], circleY[position], r, p);
            p.setColor(Color.WHITE);
            p.setTextSize(r / 4);
            p.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(daysInfo.get(6 - position).getWeekday(), circleX[position], circleY[position] - r / 2, p);
            canvas.drawText(daysInfo.get(6 - position).getSDate(), circleX[position], circleY[position] - r/8, p);
            canvas.drawText("kw-h", circleX[position], circleY[position] + r*3/4, p);
            p.setTextSize(r / 3);
            canvas.drawText(String.valueOf(daysInfo.get(6 - position).getElectricity()), circleX[position], circleY[position] + r/3, p);
            canvas.drawCircle(circleX[position], circleY[position] + r + 50, 35, p);
            p.setColor(Color.rgb(70, 222, 235));
            canvas.drawCircle(circleX[position], circleY[position] + r + 50, 30, p);
        }
        @Override
        public boolean onTouchEvent(MotionEvent event) {

            float x = event.getX();
            position = (int) ((x - r + interval/2) / interval);
            if(position < 0) {
              position = 0;
            }else if(position > 6) {
                position = 6;
            }
            this.invalidate();
            return true;

        }
    }
}
