package com.muslimmary.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.muslimmarry.R;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.helpers.helpers;

public class InviteFragment extends Fragment implements OnClickListener {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_invite, container, false);
		helpers.setTouch(rootView);
		TextView more = (TextView)rootView.findViewById(R.id.more);
		more.setOnClickListener(this);
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
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.more:
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, "dra4me");
			startActivity(Intent.createChooser(intent, "Share with..."));
			break;

		default:
			break;
		}
	}
}
