package com.muslimmary.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.muslimmarry.R;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.helpers.helpers;

public class PhotoAudioFragment extends Fragment implements OnTouchListener {
	
	ImageView back;
	ImageView option;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_photo_audio, container, false);
		helpers.setTouch(rootView);
		
		back = (ImageView)rootView.findViewById(R.id.back);
		option = (ImageView)rootView.findViewById(R.id.option);
		
		// set event for element
		back.setOnTouchListener(this);
		option.setOnTouchListener(this);
		
		// set background for bottom nav element
		((MainActivity)getActivity()).setBgGroupOriginal();
		return rootView;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((MainActivity)getActivity()).showTopNav(false);
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.back){
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				back.setBackgroundColor(Color.parseColor("#2e9dbc"));
				break;
			case MotionEvent.ACTION_UP:
				back.setBackgroundColor(Color.TRANSPARENT);
				getFragmentManager().popBackStack();
				break;
			default:
				break;
			}
		}else if(v.getId() == R.id.option){
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				option.setBackgroundColor(Color.parseColor("#2e9dbc"));
				break;
			case MotionEvent.ACTION_UP:
				option.setBackgroundColor(Color.TRANSPARENT);
				((MainActivity)getActivity()).showRightMenu();
				break;
			default:
				break;
			}
		}
		return true;
	}
}
