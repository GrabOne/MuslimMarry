package com.muslimmary.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.example.muslimmarry.R;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.sharedpref.DistanceIn;

public class AppSettingFragment extends Fragment {
	
	ImageView back;
	ImageView option;
	
	DistanceIn mi;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_app_setting, container, false);
		TextView title = (TextView)rootView.findViewById(R.id.title);
		back = (ImageView)rootView.findViewById(R.id.back);
		option = (ImageView)rootView.findViewById(R.id.option);
		SwipeLayout swipe = (SwipeLayout)rootView.findViewById(R.id.swipe);
		ViewGroup del_acc = (ViewGroup)rootView.findViewById(R.id.del_acc);
		RadioGroup radioDistanceIn = (RadioGroup)rootView.findViewById(R.id.radioDistanceIn);
		rootView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		swipe.setDragEdge(SwipeLayout.DragEdge.Bottom);
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
		
		del_acc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
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
		});
		return rootView;
	}
}
