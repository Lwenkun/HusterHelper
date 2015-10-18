package com.google.test.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.test.R;

public class DefaultContentView extends Fragment {
	
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 View view = inflater.inflate(R.layout.main_def_view, null);
		 return view;
	 }

}
