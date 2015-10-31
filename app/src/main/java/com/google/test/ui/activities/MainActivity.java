package com.google.test.ui.activities;


import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.test.R;
import com.google.test.ui.fragments.MainPageFragment;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        initView();
    }

    public void initView() {

        setContentView(R.layout.activity_main);

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

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
    	case 1:
    		if (resultCode == RESULT_OK) {
                FragmentManager fragmentManager = getFragmentManager();
                MainPageFragment fragment = (MainPageFragment) fragmentManager.findFragmentById(R.id.fragment_container);
                fragment.sendRequest();
    		}
    		break;
    	}
    }

}
