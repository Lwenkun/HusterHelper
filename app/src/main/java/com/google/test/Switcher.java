package com.google.test;

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

/**
 * Created by 15119 on 2015/9/30.
 */
public class Switcher extends AppCompatActivity implements View.OnClickListener,OnItemSelectedListener{

    private EditText buildNumInput;

    private EditText roomNumInput;

    private Spinner areaSelector;

    private int position;

    final String[] areaList = {"东区", "西区" , "韵苑", "紫菘"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.switcher);
        areaSelector = (Spinner) findViewById(R.id.area_selector);
        buildNumInput = (EditText) findViewById(R.id.build_num_input);
        roomNumInput = (EditText) findViewById(R.id.room_num_input);
        Button bindEmail = (Button) findViewById(R.id.confirm);
        RelativeLayout back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Switcher.this, R.layout.area_item, areaList);
        areaSelector.setAdapter(adapter);
        areaSelector.setOnItemSelectedListener(this);
        bindEmail.setOnClickListener(this);
}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.confirm:
                String area = areaList[position];
                String buildNum = buildNumInput.getText().toString();
                String roomNum = roomNumInput.getText().toString();
                check(area, buildNum, roomNum);
                break;
        }
    }

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            position = arg2;
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0){

        }

    private void check(String area, String buildNum, String roomNum) {
        if(buildNum.equals("")){
            Toast.makeText(Switcher.this, "楼栋号不可以为空哦~", Toast.LENGTH_SHORT).show();
        }else if(roomNum.equals("")){
            Toast.makeText(Switcher.this, "寝室号不可以为空哦~", Toast.LENGTH_SHORT).show();
        }else {
            SharedPreferences remRoomInfo = getSharedPreferences("RoomInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = remRoomInfo.edit();
            editor.putInt("position", position);
            editor.putString("area", area);
            editor.putString("buildNum", buildNum);
            editor.putString("roomNum", roomNum);
            editor.commit();
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
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
