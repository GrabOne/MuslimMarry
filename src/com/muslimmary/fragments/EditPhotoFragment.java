package com.muslimmary.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.muslimmarry.R;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.helpers.helpers;

public class EditPhotoFragment extends Fragment implements OnClickListener {
	
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
		helpers.setTouch(rootView);
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
		
		((MainActivity)getActivity()).setBgGroupOriginal();
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
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((MainActivity)getActivity()).setElementTopNav(true, true, false, false);
		((MainActivity)getActivity()).setTitle("edit photos");
		((MainActivity)getActivity()).showTopNav(true);
	}
}
