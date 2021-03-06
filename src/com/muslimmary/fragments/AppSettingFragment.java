package com.muslimmary.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.daimajia.swipe.SwipeLayout;
import com.example.muslimmarry.R;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.helpers.helpers;
import com.muslimmarry.sharedpref.DistanceIn;

public class AppSettingFragment extends Fragment implements OnClickListener {
	
	DistanceIn mi;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_app_setting, container, false);
		helpers.setTouch(rootView);
		
		SwipeLayout swipe = (SwipeLayout)rootView.findViewById(R.id.swipe);
		ViewGroup del_acc = (ViewGroup)rootView.findViewById(R.id.del_acc);
		RadioGroup radioDistanceIn = (RadioGroup)rootView.findViewById(R.id.radioDistanceIn);
		
		swipe.setDragEdge(SwipeLayout.DragEdge.Bottom);
		
		// set background for bottom nav element
		((MainActivity)getActivity()).setBgGroupOriginal();
		
		mi = new DistanceIn(getActivity());
		if(mi.isCheckMi()){
			radioDistanceIn.check(R.id.radioMi);
		}else{
			radioDistanceIn.check(R.id.radioKm);
		}
		radioDistanceIn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId == R.id.radioMi){
					mi.createCheckMi("Mi");
				}else{
					mi.delCheckMi();
				}
			}
		});
		
		// set event for element
		del_acc.setOnClickListener(this);
		return rootView;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((MainActivity)getActivity()).setElementTopNav(true, true, false, false);
		((MainActivity)getActivity()).setTitle("app settings");
		((MainActivity)getActivity()).showTopNav(true);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.del_acc){
			AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
			alertDialog.setMessage("Do you really want to delete your account or do you want to hide your account ( you will still be able to use message feature )?");
			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
			});
			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
			});
			alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Hide", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					SearchFilterFragment fr = new SearchFilterFragment();
					FragmentManager fm = getFragmentManager();
				    FragmentTransaction fragmentTransaction = fm.beginTransaction();
				    fragmentTransaction.replace(R.id.frag, fr);
				    fragmentTransaction.addToBackStack(null).commit();
				}
			});
			alertDialog.show();
		}
	}
}
