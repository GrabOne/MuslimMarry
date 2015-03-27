package com.muslimmary.fragments;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.muslimmarry.R;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.adapters.DashBoardMessageAdapter;
import com.muslimmarry.helpers.helpers;
import com.muslimmarry.model.DashBoardMessageItem;

public class DashboardMessageFragment extends Fragment {
	
	ListView mList;
	ArrayList<DashBoardMessageItem> mlst = new ArrayList<DashBoardMessageItem>();
	DashBoardMessageAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_dashboard_message, container, false);
		helpers.setTouch(rootView);
		mList = (ListView)rootView.findViewById(R.id.mList);
		
		((MainActivity)getActivity()).setBgGroupMessage();
		
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				MessagingPageFragment fr = new MessagingPageFragment();
				FragmentManager fm = getFragmentManager();
			    FragmentTransaction fragmentTransaction = fm.beginTransaction();
			    fragmentTransaction.replace(R.id.frag, fr);
			    fragmentTransaction.addToBackStack(null).commit();
			}
		});
		mlst.clear();
		mlst.add(new DashBoardMessageItem(R.drawable.avatar, "Marakana", "64 messages", "Iam currently living Beriut...", "14 MIN AGO", "unread"));
		mlst.add(new DashBoardMessageItem(R.drawable.avatar, "Marakana", "64 messages", "Iam currently living Beriut...", "14 MIN AGO", "unread"));
		mlst.add(new DashBoardMessageItem(R.drawable.avatar, "Marakana", "64 messages", "Iam currently living Beriut...", "14 MIN AGO", "read"));
		adapter = new DashBoardMessageAdapter(getActivity(), R.layout.row_dashboard_message, mlst);
		mList.setAdapter(adapter);
		
		return rootView;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((MainActivity)getActivity()).setElementTopNav(true, true, false, false);
		((MainActivity)getActivity()).setTitle("messages");
		((MainActivity)getActivity()).showTopNav(true);
	}
}
