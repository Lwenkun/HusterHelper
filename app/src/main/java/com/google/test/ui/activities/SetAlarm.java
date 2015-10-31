package com.google.test.ui.activities;

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

import com.google.test.R;
import com.google.test.common.C;
import com.google.test.json.CallBack;
import com.google.test.net.AnsynHttpRequest;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by 15119 on 2015/9/30.
 */
public class SetAlarm extends AppCompatActivity implements View.OnClickListener, CallBack{

    private EditText notifyInput;

    private String email;

    private boolean ifBindEmail;

    private ProgressDialog progressDialog;

    private MyHandler mHandler = new MyHandler();

    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            progressDialog.dismiss();
            switch (msg.what) {
                case 200:
                    showDialogNotice();
                    break;
                case 402:
                    Toast.makeText(SetAlarm.this, C.notice.INPUT_ERROR, Toast.LENGTH_SHORT).show();
                    break;
                case 400:
                    Toast.makeText(SetAlarm.this, C.notice.EMAIL_BIND_FAILED, Toast.LENGTH_SHORT).show();
                    break;
                case 410:
                    if (ifBindEmail) {
                        Toast.makeText(SetAlarm.this, C.notice.EMAIL_ALREADY_BINDED, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SetAlarm.this, C.notice.EMAIL_HASNOT_BINDED, Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    public void init() {
        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_set_alarm);
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

  /*  public void sendRequest() {
        SharedPreferences roomInfo = getSharedPreferences("RoomInfo",MODE_PRIVATE);
        String area = roomInfo.getString("area", "");
        String buildNum = roomInfo.getString("buildNum", "");
        String roomNum = roomInfo.getString("roomNum", "");
        String notify = notifyInput.getText().toString();
        String params;
        try {
            HttpUtil.setUrl(new URL("http://api.hustonline.net/dianfei/notify"));
            params = "area=" + URLEncoder.encode(area, "UTF-8")
                    + "&build=" + buildNum
                    + "&room=" + roomNum
                    + "&email=" + email;
            if(ifBindEmail){
                HttpUtil.setRequestMethod("POST");
                params = params + "&notify=" + notify;
            }else {
                HttpUtil.setRequestMethod("DELETE");
            }
            HttpUtil.setParams(params);
        }catch (Exception e) {
            e.printStackTrace();
        }
        HttpUtil.setHandler(new Handler() {
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                msg.what = BaseJSONParser.parseJSONForCode((JSONObject) msg.obj);
                //  Log.d("test3", ""+msg.what);
                switch (msg.what) {
                    case 200:
                        showDialogNotice();
                        break;
                    case 402:
                        Toast.makeText(SetAlarm.this, C.notice.INPUT_ERROR, Toast.LENGTH_SHORT).show();
                        break;
                    case 400:
                        Toast.makeText(SetAlarm.this, C.notice.EMAIL_BIND_FAILED, Toast.LENGTH_SHORT).show();
                        break;
                    case 410:
                        if (ifBindEmail) {
                            Toast.makeText(SetAlarm.this, C.notice.EMAIL_ALREADY_BINDED, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SetAlarm.this, C.notice.EMAIL_HASNOT_BINDED, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        HttpUtil.sendRequest();

    }*/

    public void sendRequest() {

        HashMap<String, String> params = new HashMap<>();
        SharedPreferences roomInfo = getSharedPreferences("RoomInfo", MODE_PRIVATE);
        try {
            params.put("area", URLEncoder.encode(roomInfo.getString("area", ""), "UTF-8")) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.put("build", roomInfo.getString("buildNum", ""));
        params.put("room", roomInfo.getString("roomNum", ""));
        params.put("email", email);
        if (ifBindEmail) {
            params.put("notify", notifyInput.getText().toString());
            AnsynHttpRequest.doPostRequest(this, params, C.url.POST_EMAIL, false, this);
        } else{
            AnsynHttpRequest.doDeleteRequest(this, params, C.url.POST_EMAIL, false, this);
        }
    }

    @Override
    public void updateUI(String response) {
        try {
            JSONObject jResponse = new JSONObject(response);
            int code = jResponse.getInt("code");
            Message msg = new Message();
            msg.what = code;
            mHandler.sendMessage(msg);

        } catch(Exception e) {
            e.printStackTrace();
        }
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
                    progressDialog.setMessage(C.notice.EMAIL_BEING_SENT);
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
            dialog.setMessage(C.notice.EMAIL_BINDED_SENT);
        } else {
            dialog.setMessage(C.notice.EMAIL_UNBINDED_SENT);
        }
        dialog.setPositiveButton(C.notice.I_KNOW,null);
        dialog.show();
    }
}
