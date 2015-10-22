package com.google.test.ui.activities;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.test.R;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        initView();
    }

    public void initView() {

        loadContentView();

        TextView back = (TextView) findViewById(R.id.back);
        TextView switcher = (TextView) findViewById(R.id.switcher);

        back.setOnClickListener(this);
        switcher.setOnClickListener(this);

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

    private void loadContentView() {

        View view;

        //这里判断网络状态
        if (! isNetworkAvailable()) {

            view = LayoutInflater.from(this).inflate(R.layout.activity_main_no_network, null);

        } else if (getSharedPreferences("RoomInfo", MODE_PRIVATE) == null) {

           view = LayoutInflater.from(this).inflate(R.layout.activity_main_first_use, null);
        } else {

            view = LayoutInflater.from(this).inflate(R.layout.activity_main_main_page, null);
        }

        setContentView(view);
	}

    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo().isAvailable();

    }

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
    	case 1:
    		if (resultCode == RESULT_OK) {
                refresh();
//    			reloadContentView
    		}
    		break;
    	}
    }

}
