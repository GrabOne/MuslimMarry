package com.muslimmary.fragments;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.example.muslimmarry.R;
import com.muslimmarry.adapters.SearchResultAdapter;
import com.muslimmarry.helpers.helpers;
import com.muslimmarry.model.SearchResultItem;

public class FavoritesFragment extends Fragment {
	
	GridView mGrid;
	ArrayList<SearchResultItem> mlst = new ArrayList<SearchResultItem>();
	SearchResultAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
		helpers.setTouch(rootView);
		mGrid = (GridView)rootView.findViewById(R.id.mGrid);
		
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
		
		adapter = new SearchResultAdapter(getActivity(), R.layout.row_search_result, mlst);
		mGrid.setAdapter(adapter);
		
		return rootView;
	}
}
