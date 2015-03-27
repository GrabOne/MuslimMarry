package com.muslimmary.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.muslimmarry.R;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.helpers.helpers;

public class GiftReceivePageFragment extends Fragment {
	
	ImageView more;
	ViewGroup profile_detail;
	ImageView heart_selected;
	ImageView iv;
	ImageView gift;
	ImageView invisible;
	ImageView report_spam;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_gift_receive_page, container, false);
		helpers.setTouch(rootView);
		profile_detail = (ViewGroup)rootView.findViewById(R.id.profile_detail);
		iv = (ImageView)rootView.findViewById(R.id.iv);
		iv.setTag(R.drawable.ic_heart_unselected);
		heart_selected = (ImageView)rootView.findViewById(R.id.heart_selected);
		heart_selected.setTag(R.drawable.ic_heart_unselected);
		gift = (ImageView)rootView.findViewById(R.id.gift);
		invisible = (ImageView)rootView.findViewById(R.id.invisible);
		report_spam = (ImageView)rootView.findViewById(R.id.report_spam);
		more = (ImageView)rootView.findViewById(R.id.more);
		more.setTag(R.drawable.arrow_up);
		
		((MainActivity)getActivity()).setBgGroupOriginal();
		more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if((Integer)more.getTag() == R.drawable.arrow_up){
					more.setTag(R.drawable.arrow_down);
					more.setImageResource(R.drawable.arrow_down);
					profile_detail.setVisibility(View.VISIBLE);
					iv.setVisibility(View.GONE);
				}else{
					more.setTag(R.drawable.arrow_up);
					more.setImageResource(R.drawable.arrow_up);
					profile_detail.setVisibility(View.GONE);
					iv.setVisibility(View.VISIBLE);
				}
			}
		});
		heart_selected.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if((Integer)heart_selected.getTag() == R.drawable.ic_heart_selected){
					heart_selected.setImageResource(R.drawable.ic_heart_unselected);
					heart_selected.setTag(R.drawable.ic_heart_unselected);
					iv.setImageResource(R.drawable.ic_heart_unselected);
					iv.setTag(R.drawable.ic_heart_unselected);
				}else{
					heart_selected.setImageResource(R.drawable.ic_heart_selected);
					heart_selected.setTag(R.drawable.ic_heart_selected);
					iv.setImageResource(R.drawable.ic_heart_selected);
					iv.setTag(R.drawable.ic_heart_selected);
				}
			}
		});
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if((Integer)iv.getTag() == R.drawable.ic_heart_selected){
					iv.setImageResource(R.drawable.ic_heart_unselected);
					iv.setTag(R.drawable.ic_heart_unselected);
					heart_selected.setImageResource(R.drawable.ic_heart_unselected);
					heart_selected.setTag(R.drawable.ic_heart_unselected);
				}else{
					iv.setImageResource(R.drawable.ic_heart_selected);
					iv.setTag(R.drawable.ic_heart_selected);
					heart_selected.setImageResource(R.drawable.ic_heart_selected);
					heart_selected.setTag(R.drawable.ic_heart_selected);
				}
			}
		});
		invisible.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
				alertDialog.setMessage("Do you want to block this user?");
				alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Permanently", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						invisible.setImageResource(R.drawable.invisible_actived);
					}
				});
				alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "30 Days", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						invisible.setImageResource(R.drawable.invisible_actived);
					}
				});
				alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						
					}
				});
				alertDialog.show();
				
			}
		});
		gift.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((MainActivity)getActivity()).displayPopupSendGift(v);
			}
		});
		report_spam.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				report_spam.setImageResource(R.drawable.spam_actived);
				Toast.makeText(getActivity(), "Sent report to admin!", Toast.LENGTH_SHORT).show();
			}
		});
		return rootView;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((MainActivity)getActivity()).setElementTopNav(true, false, true, false);
		((MainActivity)getActivity()).showTopNav(true);
	}
}
