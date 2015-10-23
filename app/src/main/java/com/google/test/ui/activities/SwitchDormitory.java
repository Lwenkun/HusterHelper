package com.google.test.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.test.R;

/**
 * Created by 15119 on 2015/9/30.
 */
public class SwitchDormitory extends AppCompatActivity implements View.OnClickListener,OnItemSelectedListener{

    private EditText buildNumInput;

    private EditText roomNumInput;

    private Spinner areaSelector;

    private int position;

    private final String[] areaList = {"东区", "西区" , "韵苑", "紫菘"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_dormitory);
        init();
    }

    public void init() {
        initView();

        initData();
    }

    public void initData() {

    }

    public void initView() {

        RelativeLayout back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(this);

        areaSelector = (Spinner) findViewById(R.id.area_selector);
        areaSelector.setOnItemSelectedListener(this);

        buildNumInput = (EditText) findViewById(R.id.build_num_input);

        roomNumInput = (EditText) findViewById(R.id.room_num_input);

        Button bindEmail = (Button) findViewById(R.id.confirm);
        bindEmail.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SwitchDormitory.this, R.layout.item_area, areaList);
        areaSelector.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.confirm:
                check();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        position = arg2;
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    private void check() {

        String area = areaList[position];
        String buildNum = buildNumInput.getText().toString();
        String roomNum = roomNumInput.getText().toString();

        if(buildNum.isEmpty()){

            Toast.makeText(SwitchDormitory.this, "楼栋号不可以为空哦~", Toast.LENGTH_SHORT).show();

        }else if(roomNum.isEmpty()){

            Toast.makeText(SwitchDormitory.this, "寝室号不可以为空哦~", Toast.LENGTH_SHORT).show();

        }else {

            SharedPreferences remRoomInfo = getSharedPreferences("RoomInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = remRoomInfo.edit();

            editor.putInt("position", position);
            editor.putString("area", area);
            editor.putString("buildNum", buildNum);
            editor.putString("roomNum", roomNum);
            editor.apply();

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);

            finish();
        }
    }

    @Override
    protected void onStart() {

        super.onStart();
        restoreInfo();
    }

    public void restoreInfo() {

        SharedPreferences remRoomInfo = getSharedPreferences("RoomInfo", MODE_PRIVATE);
        int position = remRoomInfo.getInt("position", 0);
        String buildNum = remRoomInfo.getString("buildNum", "");
        String roomNum = remRoomInfo.getString("roomNum","");

        areaSelector.setSelection(position);
        buildNumInput.setText(buildNum);
        roomNumInput.setText(roomNum);
        this.position = position;
    }
}
