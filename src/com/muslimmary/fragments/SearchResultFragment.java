package com.muslimmary.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.example.muslimmarry.R;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.adapters.SearchResultAdapter;
import com.muslimmarry.helpers.helpers;
import com.muslimmarry.model.SearchResultItem;

public class SearchResultFragment extends Fragment {
	
	TextView notify;
	GridView mGrid;
	ArrayList<SearchResultItem> mlst = new ArrayList<SearchResultItem>();
	SearchResultAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_search_result, container, false);
		helpers.setTouch(rootView);
		mGrid = (GridView)rootView.findViewById(R.id.mGrid);
		notify = (TextView)rootView.findViewById(R.id.notify);
		
		// get json object from search filter
		try{
			JSONObject jObj = new JSONObject(getArguments().getString("arrList"));
			JSONArray jArr = new JSONArray(jObj.getString("data"));
			if(jArr.length() > 0){
				mlst.clear();
				for(int i=0; i<jArr.length(); i++){
					JSONObject data = jArr.getJSONObject(i);
					JSONArray languageArr = data.getJSONArray("language");
					JSONObject locate = new JSONObject(data.getString("location"));
					JSONObject coordinates = new JSONObject(locate.getString("coordinates"));
					String username = "";
					if(!data.isNull("username")){
						username = data.getString("username");
					}
					mlst.add(new SearchResultItem(data.getString("_id"),data.getString("nickname"), username, data.getString("occupation"), data.getString("age"), data.getString("avatar"),
							data.getString("birthday"), data.getString("email"), data.getString("height"), languageArr.toString(), data.getString("promocode"), locate.getString("country"),
							locate.getString("city"), coordinates.getString("lat"), coordinates.getString("lng"), data.getDouble("distance"), false));
					adapter = new SearchResultAdapter(getActivity(), R.layout.row_search_result, mlst);
					mGrid.setAdapter(adapter);
					notify.setVisibility(View.GONE);
				}
			}else{
				notify.setVisibility(View.VISIBLE);
			}
		}catch(NullPointerException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		mGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
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
		
		return rootView;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((MainActivity)getActivity()).setElementTopNav(true, false, false, true);
		((MainActivity)getActivity()).showTopNav(true);
	}
}
