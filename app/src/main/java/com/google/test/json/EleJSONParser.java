package com.google.test.json;

import android.content.Context;
import android.widget.Toast;

import com.google.test.cache.DayInfo;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 15119 on 2015/10/17.
 */
public class EleJSONParser extends BaseJSONParser {

    private CallBack mCallBack;

    private List<DayInfo> daysInfo;

    public EleJSONParser(Context context, JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void parseJSONForDetail(){

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("EEEE");
        SimpleDateFormat format3 = new SimpleDateFormat("MM月dd日");

        daysInfo = new ArrayList<DayInfo>();
        try {
            String latest = mJSONObject.getJSONObject("data").getString("remain");
            if (latest.equals("null")) {
                Toast.makeText(, "查询电费失败了0.0", Toast.LENGTH_SHORT).show();
            } else {

                mCallBack.updateUI();
                //
                angle = Math.log10(Double.parseDouble(latest)) * 50;
                //
                drawCircle(1);
                //
                mTvElectricity.setText(latest);

                JSONObject recent = mJSONObject.getJSONObject("data").getJSONObject("recent");
                Iterator<String> jStrings = recent.keys();
                while (jStrings.hasNext()) {
                    String jString = jStrings.next();
                    JSONObject jDayInfo = recent.getJSONObject(jString);
                    Date date = format1.parse(jDayInfo.getString("updated_at"));
                    String weekday = format2.format(date);
                    String sDate = format3.format(date);
                    float electricity = Float.parseFloat(jDayInfo.getString("dianfei"));
                    DayInfo dayInfo = new DayInfo(weekday, sDate, electricity);
                    daysInfo.add(dayInfo);
                }
                float range = calRange();
                mTvAverage.setText(String.format("%.2f", range / 6));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
