package com.example.muslimmarry;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Favorites extends Fragment {
	
	GridView mGrid;
	ArrayList<SearchResultItem> mlst = new ArrayList<SearchResultItem>();
	SearchResultAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.favorites, container, false);
		TextView title = (TextView)rootView.findViewById(R.id.title);
		mGrid = (GridView)rootView.findViewById(R.id.mGrid);
		rootView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		((MainActivity)getActivity()).setFontTypeText(title);
		
		mGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				mlst.get(position).setClick(true);
				for(int i=0; i<mlst.size(); i++){
					if(i != position){
						mlst.get(i).setClick(false);
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
		
		adapter = new SearchResultAdapter(getActivity(), R.layout.search_result_row, mlst);
		mGrid.setAdapter(adapter);
		
		return rootView;
	}
}
