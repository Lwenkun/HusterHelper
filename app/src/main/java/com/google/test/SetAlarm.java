package com.google.test;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;

import json.JSONParser;
import network.RequestHandler;

/**
 * Created by 15119 on 2015/9/30.
 */
public class SetAlarm extends AppCompatActivity implements View.OnClickListener{

    private EditText notifyInput;

    private String email;

    private boolean ifBindEmail;

    private ProgressDialog progressDialog;

    @Override
    public void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_alarm);
        RelativeLayout back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
        notifyInput = (EditText) findViewById(R.id.notify_input);
        ImageButton bindEmail = (ImageButton) findViewById(R.id.bind_email);
        bindEmail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bind_email :
                Intent intent= new Intent(SetAlarm.this, BindEmail.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    public void sendRequest() {
        SharedPreferences roomInfo = getSharedPreferences("RoomInfo",MODE_PRIVATE);
        String area = roomInfo.getString("area", "");
        String buildNum = roomInfo.getString("buildNum", "");
        String roomNum = roomInfo.getString("roomNum", "");
        String notify = notifyInput.getText().toString();
        String params;
        try {
            RequestHandler.setUrl(new URL("http://api.hustonline.net/dianfei/notify"));
            params = "area=" + URLEncoder.encode(area, "UTF-8")
                    + "&build=" + buildNum
                    + "&room=" + roomNum
                    + "&email=" + email;
            if(ifBindEmail){
                RequestHandler.setRequestMethod("POST");
                params = params + "&notify=" + notify;
            }else {
                RequestHandler.setRequestMethod("DELETE");
            }
            RequestHandler.setParams(params);
        }catch (Exception e) {
            e.printStackTrace();
        }
        RequestHandler.setHandler(new Handler(){
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                msg.what = JSONParser.parseJSONForCode((JSONObject)msg.obj);
                Log.d("test3", ""+msg.what);
                switch (msg.what) {
                    case 200:
                        showDialogNotice();
                        break;
                    case 402:
                        Toast.makeText(SetAlarm.this, "输入错误，请重试", Toast.LENGTH_SHORT).show();
                        break;
                    case 400:
                        Toast.makeText(SetAlarm.this, "邮箱绑定失败，请稍后重试", Toast.LENGTH_SHORT).show();
                        break;
                    case 410:
                        if(ifBindEmail) {
                            Toast.makeText(SetAlarm.this, "亲，该邮箱已经绑定过了哦~", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(SetAlarm.this, "亲，邮箱还未绑定呢~", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:break;
                }
            }
        });
        RequestHandler.sendRequest();
     /*  new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL("http://api.hustonline.net/dianfei/notify");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);

                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.writeBytes(params);
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
                    message.what = JSONParser.parseJSONForCode(data);
                    handler.sendMessage(message);
                }catch(Exception e) {
                    e.printStackTrace();

                }finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK) {
                    email = data.getStringExtra("email");
                    ifBindEmail = data.getBooleanExtra("ifBindEmail",true);
                    progressDialog = new ProgressDialog(SetAlarm.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("正在发送证邮件，请稍后...");
                    progressDialog.show();
                    sendRequest();
                }
                break;
            default:
                break;
        }
    }

    public void showDialogNotice() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(SetAlarm.this);
        dialog.setTitle("Note:");
        if(ifBindEmail) {
            dialog.setMessage("已经发送了一封绑定确认邮件到你的邮箱中");
        } else {
            dialog.setMessage("已经发送了一封解绑确认邮件到你的邮箱中");
        }
        dialog.setPositiveButton("我知道了",null);
        dialog.show();
    }
}
