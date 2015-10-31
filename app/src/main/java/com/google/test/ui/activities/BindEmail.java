package com.google.test.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.test.R;
import com.google.test.common.C;

/**
 * Created by 15119 on 2015/9/30.
 */
public class BindEmail extends Activity implements View.OnClickListener{

    private EditText et_emailInput;
    
    private boolean ifBindEmail = true;

    private TextView tv_bindEmail;

    private TextView tv_unbindEmail;

    private SharedPreferences roomInfo;

   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_email);
        initView();
    }

    public void initView() {


        et_emailInput = (EditText) findViewById(R.id.email_input);
        Button btn_cancel = (Button) findViewById(R.id.cancel);
        Button btn_confirm = (Button) findViewById(R.id.confirm);
        tv_bindEmail = (TextView) findViewById(R.id.bind_email);
        tv_unbindEmail = (TextView) findViewById(R.id.unbind_email);
        tv_bindEmail.setOnClickListener(this);
        tv_unbindEmail.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        TextView tv_areaText = (TextView) findViewById(R.id.area);
        TextView tv_buildNumText = (TextView) findViewById(R.id.buildNum);
        TextView tv_roomNumText = (TextView) findViewById(R.id.roomNum);
        roomInfo = getSharedPreferences("RoomInfo",MODE_PRIVATE);
        String area = roomInfo.getString("area", "");
        String buildNum = roomInfo.getString("buildNum", "");
        String roomNum = roomInfo.getString("roomNum", "");
        String email = roomInfo.getString("email", "");
        et_emailInput.setText(email);
        tv_areaText.setText(area);
        tv_buildNumText.setText(buildNum);
        tv_roomNumText.setText(roomNum);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.confirm:
            	sendEmail();
                break;
            case R.id.bind_email:
                tv_bindEmail.setTextColor(getResources().getColor(R.color.mainColor));
                tv_unbindEmail.setTextColor(getResources().getColor(R.color.grayText));
            	ifBindEmail = true;
            	break;
            case R.id.unbind_email:

                //改变字体颜色提示用户当前操作方式
                tv_bindEmail.setTextColor(getResources().getColor(R.color.grayText));
                tv_unbindEmail.setTextColor(getResources().getColor(R.color.mainColor));
            	ifBindEmail = false;
            	break;
        }
    }

    public void sendEmail() {
    	String email = et_emailInput.getText().toString();
        if(email.equals("")) {
        	Toast.makeText(BindEmail.this, C.notice.EMAIL_NO_EMPTY, Toast.LENGTH_SHORT).show();
        }else {

            //将邮箱绑定情况反馈给上一个活动
            roomInfo.edit().putString("email", email).apply();
            Intent intent = new Intent();
            intent.putExtra("ifBindEmail",ifBindEmail);
            intent.putExtra("email", email);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}

