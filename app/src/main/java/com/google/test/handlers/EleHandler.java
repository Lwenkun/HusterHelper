package com.google.test.handlers;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.google.test.json.EleJSONParser;

import org.json.JSONObject;


/**
 * Created by 15119 on 2015/10/18.
 */
public class EleHandler extends Handler {

    private Context mContext;

    public EleHandler(Context context) {
        mContext = context;
    }



    @Override
    public void handleMessage(Message msg) {

        EleJSONParser parser = new EleJSONParser((JSONObject) msg.obj);

        switch (parser.parseJSONForCode()) {
            case 200:
                parser.parseJSONForDetail();
                break;
            case 402:
                Toast.makeText(mContext, "很抱歉，没有该寝室的信息", Toast.LENGTH_SHORT).show();
                break;
            case 400:
                Toast.makeText(mContext, "操作错误，请稍后重试", Toast.LENGTH_SHORT).show();
                break;
            case 500:
                Toast.makeText(mContext, "服务器发生未知错误", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

}
