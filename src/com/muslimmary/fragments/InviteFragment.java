package com.muslimmary.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.muslimmarry.R;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.helpers.helpers;

public class InviteFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_invite, container, false);
		helpers.setTouch(rootView);
		
		((MainActivity)getActivity()).setBgGroupOriginal();
		return rootView;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((MainActivity)getActivity()).setElementTopNav(true, true, false, false);
		((MainActivity)getActivity()).setTitle("invite a friend");
		((MainActivity)getActivity()).showTopNav(true);
	}
}
