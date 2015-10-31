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
import com.google.test.common.C;

/**
 * Created by 15119 on 2015/9/30.
 */
public class SwitchDormitory extends AppCompatActivity implements View.OnClickListener, OnItemSelectedListener{

    private EditText et_buildNumInput;

    private EditText et_roomNumInput;

    private Spinner sp_areaSelector;

    private int position;

    private final String[] areaList = C.AREA_LIST;

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

        sp_areaSelector = (Spinner) findViewById(R.id.area_selector);
        sp_areaSelector.setOnItemSelectedListener(this);

        et_buildNumInput = (EditText) findViewById(R.id.build_num_input);

        et_roomNumInput = (EditText) findViewById(R.id.room_num_input);

        Button bindEmail = (Button) findViewById(R.id.confirm);
        bindEmail.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SwitchDormitory.this, R.layout.item_area, areaList);
        sp_areaSelector.setAdapter(adapter);
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
        String buildNum = et_buildNumInput.getText().toString();
        String roomNum = et_roomNumInput.getText().toString();

        if(buildNum.isEmpty()){

            Toast.makeText(SwitchDormitory.this, C.notice.BUILD_NUM_NO_EMPTY, Toast.LENGTH_SHORT).show();

        }else if(roomNum.isEmpty()){

            Toast.makeText(SwitchDormitory.this, C.notice.ROOM_NUM_NO_EMPTY, Toast.LENGTH_SHORT).show();

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

        sp_areaSelector.setSelection(position);
        et_buildNumInput.setText(buildNum);
        et_roomNumInput.setText(roomNum);
        this.position = position;
    }
}
