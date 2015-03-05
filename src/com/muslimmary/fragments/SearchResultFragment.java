package com.muslimmary.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.muslimmarry.R;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.adapters.SearchResultAdapter;
import com.muslimmarry.helpers.Notify;
import com.muslimmarry.item.SearchResultItem;

public class SearchResultFragment extends Fragment {
	
	ImageView option;
	
	GridView mGrid;
	ArrayList<SearchResultItem> mlst = new ArrayList<SearchResultItem>();
	SearchResultAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_search_result, container, false);
		mGrid = (GridView)rootView.findViewById(R.id.mGrid);
		option = (ImageView)rootView.findViewById(R.id.option);
		rootView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
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
					adapter = new SearchResultAdapter(getActivity(), R.layout.list_item_search_result, mlst);
					mGrid.setAdapter(adapter);
				}
			}else{
				Notify.show(getActivity(), "No result matches!");
			}
		}catch(NullPointerException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
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
}
