package com.muslimmary.fragments;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.muslimmarry.R;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.adapters.DashboardAlertAdapter;
import com.muslimmarry.item.DashboardAlertItem;


public class DashboardAlertFragment extends Fragment {
	
	ImageView option;
	
	ListView mList;
	ArrayList<DashboardAlertItem> mlst = new ArrayList<DashboardAlertItem>();
	DashboardAlertAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_dashboard_alert, container, false);
		mList = (ListView)rootView.findViewById(R.id.mList);
		TextView title = (TextView)rootView.findViewById(R.id.title);
		option = (ImageView)rootView.findViewById(R.id.option);
		rootView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		((MainActivity)getActivity()).setBgGroupBell();
		((MainActivity)getActivity()).setFontTypeText(title);
		
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
					break;
				default:
					break;
				}
				return true;
			}
		});
		
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				GiftReceivePageFragment fr = new GiftReceivePageFragment();
				FragmentManager fm = getFragmentManager();
			    FragmentTransaction fragmentTransaction = fm.beginTransaction();
			    fragmentTransaction.replace(R.id.frag, fr);
			    fragmentTransaction.addToBackStack(null).commit();
			}
		});
		mlst.clear();
		mlst.add(new DashboardAlertItem(R.drawable.avatar, "Barakla23", "Accepted your gift", "14 MIN AGO", "unread"));
		mlst.add(new DashboardAlertItem(R.drawable.avatar, "Barakla23", "Accepted your gift", "14 MIN AGO", "unread"));
		mlst.add(new DashboardAlertItem(R.drawable.avatar, "Barakla23", "Accepted your gift", "14 MIN AGO", "read"));
		adapter = new DashboardAlertAdapter(getActivity(), R.layout.list_item_dashboard_alert, mlst);
		mList.setAdapter(adapter);
		
		return rootView;
	}
}
