package com.google.test;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by 15119 on 2015/9/30.
 */

/**
 * Created by 15119 on 2015/9/30.
 */
public class SetAlarm extends AppCompatActivity{

    private EditText notifyInput;

    private String email;

    private boolean ifBindEmail;

    private ProgressDialog progressDialog;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            progressDialog.dismiss();
            switch (msg.what) {
//                设置成功返回 `200` 状态码，输入错误 `402`，失败 `400`，邮箱已经被绑定过 `410`。
                case 200:
                    //Toast.makeText(BindEmail.this, "已经发送了一封绑定确认邮件到你的邮箱中", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SetAlarm.this);
                    dialog.setTitle("Note:");
                    if(ifBindEmail) {
                        dialog.setMessage("已经发送了一封绑定确认邮件到你的邮箱中");
                    } else {
                        dialog.setMessage("已经发送了一封解绑确认邮件到你的邮箱中");
                    }
                    dialog.setPositiveButton("我知道了",null);
                    dialog.show();
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
    };

    @Override
    public void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.set_alarm);
    /*    SharedPreferences roomInfo = getSharedPreferences("RoomInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = roomInfo.edit();
        editor.putString("area", "韵苑");
        editor.putString("buildNum", "16");
        editor.putString("roomNum", "529");
        editor.commit();*/
        RelativeLayout back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        notifyInput = (EditText) findViewById(R.id.notify_input);
        ImageButton bindEmail = (ImageButton) findViewById(R.id.bind_email);
        bindEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SetAlarm.this, BindEmail.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    public void sendRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL("http://api.hustonline.net/dianfei/notify");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    SharedPreferences roomInfo = getSharedPreferences("RoomInfo",MODE_PRIVATE);
                    String area = roomInfo.getString("area", "");
                    String buildNum = roomInfo.getString("buildNum", "");
                    String roomNum = roomInfo.getString("roomNum", "");
                    String notify = notifyInput.getText().toString();
                    String params = "area=" + URLEncoder.encode(area, "UTF-8")
                            + "&build=" + buildNum
                            + "&room=" + roomNum
                            + "&email=" + email;
                    if(ifBindEmail){
                        connection.setRequestMethod("POST");
                        params = params + "&notify=" + notify;
                    }else {
                        connection.setRequestMethod("DELETE");
                    }
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.writeBytes(params);
                    out.flush();
                    out.close();
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    String data = response.toString();
                    Message message = new Message();
                    message.what = parseJSONForCode(data);
                    handler.sendMessage(message);
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

    private int parseJSONForCode(String response) {
        int code=0;
        try {
            code = new JSONObject(response).getInt("code");
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return code;
    }

}
