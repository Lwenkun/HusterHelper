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

/**
 * Created by 15119 on 2015/9/30.
 */
public class BindEmail extends Activity implements View.OnClickListener{

    private EditText emailInput;
    
    private boolean ifBindEmail = true;

    private TextView bindEmail;

    private TextView unbindEmail;

    private SharedPreferences roomInfo;

   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_email);
        initView();
    }

    public void initView() {
        emailInput = (EditText) findViewById(R.id.email_input);
        Button cancel = (Button) findViewById(R.id.cancel);
        Button confirm = (Button) findViewById(R.id.confirm);
        bindEmail = (TextView) findViewById(R.id.bind_email);
        unbindEmail = (TextView) findViewById(R.id.unbind_email);
        bindEmail.setOnClickListener(this);
        unbindEmail.setOnClickListener(this);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
        TextView areaText = (TextView) findViewById(R.id.area);
        TextView buildNumText = (TextView) findViewById(R.id.buildNum);
        TextView roomNumText = (TextView) findViewById(R.id.roomNum);
        roomInfo = getSharedPreferences("RoomInfo",MODE_PRIVATE);
        String area = roomInfo.getString("area", "");
        String buildNum = roomInfo.getString("buildNum", "");
        String roomNum = roomInfo.getString("roomNum", "");
        String email = roomInfo.getString("email", "");
        emailInput.setText(email);
        areaText.setText(area);
        buildNumText.setText(buildNum);
        roomNumText.setText(roomNum);
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
                bindEmail.setTextColor(getResources().getColor(R.color.mainColor));
                unbindEmail.setTextColor(getResources().getColor(R.color.grayText));
            	ifBindEmail = true;
            	break;
            case R.id.unbind_email:
                bindEmail.setTextColor(getResources().getColor(R.color.grayText));
                unbindEmail.setTextColor(getResources().getColor(R.color.mainColor));
            	ifBindEmail = false;
            	break;
        }
    }

    public void sendEmail() {
    	String email = emailInput.getText().toString();
        if(email.equals("")) {
        	Toast.makeText(BindEmail.this, "邮箱不可以为空哦", Toast.LENGTH_SHORT).show();
        }else {
            roomInfo.edit().putString("email", email).apply();
            Intent intent = new Intent();
            intent.putExtra("ifBindEmail",ifBindEmail);
            intent.putExtra("email", email);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}

