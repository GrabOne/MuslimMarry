package com.muslimmary.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muslimmarry.R;
import com.muslimmarry.activities.MainActivity;

public class EditPhotoFragment extends Fragment implements OnClickListener {
	
	ImageView option;
	ImageView back;
	ViewGroup main_photo;
	ViewGroup photo_1;
	ViewGroup photo_2;
	ViewGroup photo_3;
	ViewGroup photo_4;
	ViewGroup add_photo;
	ViewGroup fc_btn_main_photo;
	ViewGroup fc_btn_photo_1;
	ViewGroup fc_btn_photo_2;
	ViewGroup fc_btn_photo_3;
	ViewGroup fc_btn_photo_4;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_edit_photo, container, false);
		TextView title = (TextView)rootView.findViewById(R.id.title);
		option = (ImageView)rootView.findViewById(R.id.option);
		back = (ImageView)rootView.findViewById(R.id.back);
		main_photo = (ViewGroup)rootView.findViewById(R.id.main_photo);
		photo_1 = (ViewGroup)rootView.findViewById(R.id.photo_1);
		photo_2 = (ViewGroup)rootView.findViewById(R.id.photo_2);
		photo_3 = (ViewGroup)rootView.findViewById(R.id.photo_3);
		photo_4 = (ViewGroup)rootView.findViewById(R.id.photo_4);
		add_photo = (ViewGroup)rootView.findViewById(R.id.add_photo);
		fc_btn_main_photo = (ViewGroup)rootView.findViewById(R.id.fc_btn_main_photo);
		fc_btn_photo_1 = (ViewGroup)rootView.findViewById(R.id.fc_btn_photo_1);
		fc_btn_photo_2 = (ViewGroup)rootView.findViewById(R.id.fc_btn_photo_2);
		fc_btn_photo_3 = (ViewGroup)rootView.findViewById(R.id.fc_btn_photo_3);
		fc_btn_photo_4 = (ViewGroup)rootView.findViewById(R.id.fc_btn_photo_4);
		rootView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		((MainActivity)getActivity()).setBgGroupOriginal();
		((MainActivity)getActivity()).setFontTypeText(title);
		back.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				switch (arg1.getAction()) {
				case MotionEvent.ACTION_DOWN:
					back.setBackgroundColor(Color.parseColor("#2e9dbc"));
					break;
				case MotionEvent.ACTION_UP:
					back.setBackgroundColor(Color.TRANSPARENT);
					((MainActivity)getActivity()).hideKeyboard();
					getFragmentManager().popBackStack();
					break;
				default:
					break;
				}
				return true;
			}
		});
		option.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					option.setBackgroundColor(Color.parseColor("#2e9dbc"));
					break;
				case MotionEvent.ACTION_UP:
					option.setBackgroundColor(Color.TRANSPARENT);
					((MainActivity)getActivity()).showRightMenu();
				default:
					break;
				}
				return true;
			}
		});
		main_photo.setOnClickListener(this);
		photo_1.setOnClickListener(this);
		photo_2.setOnClickListener(this);
		photo_3.setOnClickListener(this);
		photo_4.setOnClickListener(this);
		return rootView;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.main_photo:
			if(fc_btn_main_photo.getVisibility() == View.GONE){
				fc_btn_main_photo.setVisibility(View.VISIBLE);
			}else{
				fc_btn_main_photo.setVisibility(View.GONE);
			}
			break;
		case R.id.photo_1:
			if(fc_btn_photo_1.getVisibility() == View.GONE){
				fc_btn_photo_1.setVisibility(View.VISIBLE);
			}else{
				fc_btn_photo_1.setVisibility(View.GONE);
			}
			break;
		case R.id.photo_2:
			if(fc_btn_photo_2.getVisibility() == View.GONE){
				fc_btn_photo_2.setVisibility(View.VISIBLE);
			}else{
				fc_btn_photo_2.setVisibility(View.GONE);
			}
			break;
		case R.id.photo_3:
			if(fc_btn_photo_3.getVisibility() == View.GONE){
				fc_btn_photo_3.setVisibility(View.VISIBLE);
			}else{
				fc_btn_photo_3.setVisibility(View.GONE);
			}
			break;
		case R.id.photo_4:
			if(fc_btn_photo_4.getVisibility() == View.GONE){
				fc_btn_photo_4.setVisibility(View.VISIBLE);
			}else{
				fc_btn_photo_4.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	}
}
