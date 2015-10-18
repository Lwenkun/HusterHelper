package com.google.test.activities;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.test.R;
import com.google.test.fragments.ContentView;
import com.google.test.fragments.EmptyContentView;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView() {
        TextView back = (TextView) findViewById(R.id.back);
        TextView switcher = (TextView) findViewById(R.id.switcher);
        back.setOnClickListener(this);
        switcher.setOnClickListener(this);
        switchContentView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.switcher:
                Intent intent = new Intent(MainActivity.this, SwitchDormitory.class);
                startActivityForResult(intent, 1);
                break;
        }
    }

    private void switchContentView() {
    	SharedPreferences roomInfo =  getSharedPreferences("RoomInfo", MODE_PRIVATE);
        String buildNum = roomInfo.getString("buildNum", "");
        String roomNum = roomInfo.getString("roomNum", "");
        FragmentManager fragmentManager = getFragmentManager();
    	FragmentTransaction  transaction =  fragmentManager.beginTransaction();
        //这里应该判断网络状态
        if(buildNum.equals("")||roomNum.equals("")) {
            EmptyContentView defaultContentView = new EmptyContentView();
            transaction.replace(R.id.main_view_container, defaultContentView);
            transaction.commit();
        }else {
        	ContentView contentView = new ContentView();
    		transaction.replace(R.id.main_view_container, contentView);   			
            transaction.commit();
        }
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
    	case 1:
    		if (resultCode == RESULT_OK) {
    			switchContentView();
    		}
    		break;
        default:
            break;
    	}
    }

}
