package com.example.muslimmarry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muslimmarry.libraries.RangeSeekBar;
import com.example.muslimmarry.libraries.ServiceHandler;
import com.example.muslimmarry.libraries.RangeSeekBar.OnRangeSeekBarChangeListener;
import com.example.muslimmarry.libraries.TransparentProgressDialog;
import com.example.muslimmarry.sharedpref.DistanceIn;
import com.example.muslimmarry.sharedpref.prefUser;

public class SearchFilter extends Fragment {
	
	View rootView;
	ImageView back;
	ImageView option;
	EditText speak;
	ImageView addSpeak;
	EditText job;
	ImageView addJob;
	TextView titDistance;
	TextView titHeight;
	RadioGroup radioSex;
	RadioButton radioSexBtn;
	TextView minAge;
	TextView maxAge;
	TextView minDistance;
	TextView maxDistance;
	TextView minHeight;
	TextView maxHeight;
	Button show;
	
	ListView listSpeak;
	ArrayList<SpeakItem> dataSpeaks = new ArrayList<SpeakItem>();
	ArrayList<GetSpeaks> speaksLst = new ArrayList<GetSpeaks>();
	
	ListView listJob;
	ArrayList<JobItem> dataJobs = new ArrayList<JobItem>();
	
	DistanceIn mi;
	
	String userid = "";
	String token = "";
	String lat = "";
	String lng = "";
	String sexValue = "";
	prefUser user;
	
	String resultString = "";
	
	TransparentProgressDialog pd;
	
	SendDataToSearchResult mCallback;
	
	AlertDialog dialog;
	String[] speaks;
	String[] jobs;
	
	public interface SendDataToSearchResult{
		public void SendArrList(String arrList);
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try{
			mCallback = (SendDataToSearchResult)activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.search_filter, container, false);
		TextView title = (TextView)rootView.findViewById(R.id.title);
		back = (ImageView)rootView.findViewById(R.id.back);
		option = (ImageView)rootView.findViewById(R.id.option);
		speak = (EditText)rootView.findViewById(R.id.speak);
		addSpeak = (ImageView)rootView.findViewById(R.id.addSpeak);
		job = (EditText)rootView.findViewById(R.id.job);
		addJob = (ImageView)rootView.findViewById(R.id.addJob);
		titDistance = (TextView)rootView.findViewById(R.id.titDistance);
		titHeight = (TextView)rootView.findViewById(R.id.titHeight);
		radioSex = (RadioGroup)rootView.findViewById(R.id.radioSex);
		minAge = (TextView)rootView.findViewById(R.id.minAge);
		maxAge = (TextView)rootView.findViewById(R.id.maxAge);
		minDistance = (TextView)rootView.findViewById(R.id.minDistance);
		maxDistance = (TextView)rootView.findViewById(R.id.maxDistance);
		minHeight = (TextView)rootView.findViewById(R.id.minHeight);
		maxHeight = (TextView)rootView.findViewById(R.id.maxHeight);
		show = (Button)rootView.findViewById(R.id.show);
		listSpeak = (ListView)rootView.findViewById(R.id.listSpeak);
		listJob = (ListView)rootView.findViewById(R.id.listJob);
		rootView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		new FetchSpeaks().execute();
		
		// create user object
		user = new prefUser(getActivity());
		HashMap<String, String> user_info = user.getUserDetail();
		userid = user_info.get(prefUser.KEY_USERID);
		token = user_info.get(prefUser.KEY_TOKEN);
		lat = user_info.get(prefUser.KEY_LAT);
		lng = user_info.get(prefUser.KEY_LNG);
		
		// create mi object
		mi = new DistanceIn(getActivity());
		if(mi.isCheckMi()){
			titDistance.setText("DISTANCE (Mi)");
			titHeight.setText("HEIGHT (Feet)");
		}else{
			titDistance.setText("DISTANCE (KM)");
			titHeight.setText("HEIGHT (CM)");
		}
		
		//((MainActivity)getActivity()).setBgGroupFindUser();

		show.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    ((MainActivity)getActivity()).setBgGroupOriginal();
				int selectedId = radioSex.getCheckedRadioButtonId();
				radioSexBtn = (RadioButton)rootView.findViewById(selectedId);
				sexValue = radioSexBtn.getText().toString();
				if(dataSpeaks.size() <= 0){
					Toast.makeText(getActivity(), "Please select speak!", Toast.LENGTH_SHORT).show();
				}else if(dataJobs.size() <= 0){
					Toast.makeText(getActivity(), "Please select occupation!", Toast.LENGTH_SHORT).show();
				}else{
					new Search().execute();
				}
			}
		});
		((MainActivity)getActivity()).setFontTypeText(title);
		((MainActivity)getActivity()).setFontTypeButton(show);
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
					((MainActivity)getActivity()).setBgGroupOriginal();
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
		
		// speak
		speak.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectSpeak();
			}
		});
		addSpeak.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int count = 0;
				if(speak.getText().toString().length() > 0){
					for(int i=0; i<dataSpeaks.size(); i++){
						if(dataSpeaks.get(i).getLanguage().equals(speak.getText().toString())){
							count += 1;
						}
					}
					if(count == 0){
						dataSpeaks.add(new SpeakItem(speak.getText().toString()));
					}
					listSpeak.setAdapter(speakListAdapter);
					speakListAdapter.notifyDataSetChanged();
					setListViewHeightBasedOnChildren(listSpeak);
					speak.setText("");
				}
			}
		});
		
		// job
		job.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				selectJob();
			}
		});
		addJob.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int count = 0;
				if(job.getText().toString().length() > 0){
					for(int i=0; i<dataJobs.size(); i++){
						if(dataJobs.get(i).getJob().equals(job.getText().toString())){
							count += 1;
						}
					}
					if(count == 0){
						dataJobs.add(new JobItem(job.getText().toString()));
					}
					listJob.setAdapter(jobListAdapter);
					jobListAdapter.notifyDataSetChanged();
					setListViewHeightBasedOnChildren(listJob);
					job.setText("");
				}
			}
		});
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		// seekbar age
		RangeSeekBar<Integer> seekBarAge = new RangeSeekBar<Integer>(18, 65, getActivity());
		seekBarAge.setNotifyWhileDragging(true);
		seekBarAge.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {

			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
					Integer minValue, Integer maxValue) {
				// TODO Auto-generated method stub
				minAge.setText(""+minValue);
				maxAge.setText(""+maxValue);
			}
		});
		ViewGroup age_group = (ViewGroup)rootView.findViewById(R.id.age_group);
		seekBarAge.setLayoutParams(params);
		age_group.addView(seekBarAge);
		
		// seekbar distance
		RangeSeekBar<Integer> seekBarDistance = new RangeSeekBar<Integer>(1, 161, getActivity());
		seekBarDistance.setNotifyWhileDragging(true);
		seekBarDistance.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {

			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
					Integer minValue, Integer maxValue) {
				// TODO Auto-generated method stub
				minDistance.setText(""+minValue);
				maxDistance.setText(""+maxValue);
			}
		});
		ViewGroup distance_group = (ViewGroup)rootView.findViewById(R.id.distance_group);
		seekBarDistance.setLayoutParams(params);
		distance_group.addView(seekBarDistance);
		
		// seekbar height
		RangeSeekBar<Double> seekBarHeight = new RangeSeekBar<Double>(1.30, 2.30, getActivity());
		seekBarHeight.setNotifyWhileDragging(true);
		seekBarHeight.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Double>() {

			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
					Double minValue, Double maxValue) {
				// TODO Auto-generated method stub
				minHeight.setText(""+Math.floor(minValue * 100) / 100);
				maxHeight.setText(""+Math.floor(maxValue * 100) / 100);
			}
		});
		ViewGroup height_group = (ViewGroup)rootView.findViewById(R.id.height_group);
		seekBarHeight.setLayoutParams(params);
		height_group.addView(seekBarHeight);
		
		return rootView;
	}
	
	private BaseAdapter speakListAdapter = new BaseAdapter() {
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View rowView = LayoutInflater.from(getActivity()).inflate(R.layout.list_speak_row, null);
			TextView txt = (TextView)rowView.findViewById(R.id.txt);
			ImageView remove = (ImageView)rowView.findViewById(R.id.remove);
			SpeakItem item = dataSpeaks.get(position);
			txt.setText(item.getLanguage().substring(0, 1).toUpperCase() + item.getLanguage().substring(1));
			remove.setBackgroundResource(R.drawable.remove_icon);
			remove.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					View parentRow = (View) arg0.getParent();
					ListView lv = (ListView) parentRow.getParent();
					final int position = lv.getPositionForView(parentRow);
					dataSpeaks.remove(position);
					notifyDataSetChanged();
					setListViewHeightBasedOnChildren(listSpeak);
				}
			});
			return rowView;
		}
		
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataSpeaks.size();
		}
	};
	
	private BaseAdapter jobListAdapter = new BaseAdapter() {
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View rowView = LayoutInflater.from(getActivity()).inflate(R.layout.list_speak_row, null);
			TextView txt = (TextView)rowView.findViewById(R.id.txt);
			ImageView remove = (ImageView)rowView.findViewById(R.id.remove);
			JobItem item = dataJobs.get(position);
			txt.setText(item.getJob());
			remove.setBackgroundResource(R.drawable.remove_icon);
			remove.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					View parentRow = (View) arg0.getParent();
					ListView lv = (ListView) parentRow.getParent();
					final int position = lv.getPositionForView(parentRow);
					dataJobs.remove(position);
					notifyDataSetChanged();
					setListViewHeightBasedOnChildren(listJob);
				}
			});
			return rowView;
		}
		
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataJobs.size();
		}
	};
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {
	    ListAdapter listAdapter = listView.getAdapter();
	    if (listAdapter == null)
	        return;

	    int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
	    int totalHeight = 0;
	    View view = null;
	    for (int i = 0; i < listAdapter.getCount(); i++) {
	        view = listAdapter.getView(i, view, listView);
	        if (i == 0)
	            view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

	        view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
	        totalHeight += view.getMeasuredHeight();
	    }
	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	    listView.setLayoutParams(params);
	    listView.requestLayout();
	}
	/*
	 * Search 
	 */
	private class Search extends AsyncTask<String, String, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new TransparentProgressDialog(getActivity(), R.drawable.loading_spinner);
			pd.show();
		}
		@Override
		protected Void doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			InputStream inputStream = null;
			try{
				JSONObject jObj = new JSONObject();
				jObj.put("user_id", userid);
				jObj.put("remember_token", token);
				jObj.put("gender", sexValue.toLowerCase());
				JSONObject age = new JSONObject();
				age.put("from", minAge.getText().toString());
				age.put("to", maxAge.getText().toString());
				jObj.put("age", age);
				JSONObject distance = new JSONObject();
				if(minDistance.getText().toString().equalsIgnoreCase("1")){
					distance.put("from", "0");
				}else{
					distance.put("from", minDistance.getText().toString());
				}
				distance.put("to", maxDistance.getText().toString());
				jObj.put("distance", distance);
				JSONArray speaks = new JSONArray();
				for(int i=0; i <dataSpeaks.size(); i++){
					speaks.put(dataSpeaks.get(i).getLanguage().toLowerCase());
				}
				jObj.put("language", speaks);
				JSONArray occupations = new JSONArray();
				for(int i=0; i<dataJobs.size(); i++){
					occupations.put(dataJobs.get(i).getJob().toLowerCase());
				}
				jObj.put("occupations", occupations);
				JSONObject height = new JSONObject();
				height.put("from", minHeight.getText().toString());
				height.put("to", maxHeight.getText().toString());
				jObj.put("height", height);
				JSONObject coordinates = new JSONObject();
				coordinates.put("lat", lat);
				coordinates.put("lng", lng);
				jObj.put("coordinates", coordinates);
				Log.d("obj", jObj.toString());
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://muslimmarry.campcoders.com/api/v1/search");
				StringEntity se = new StringEntity(jObj.toString());
				httppost.setEntity(se);
				httppost.setHeader("Accept", "application/json");
				httppost.setHeader("Content-type", "application/json");
				
				HttpResponse response = httpClient.execute(httppost);
				
				inputStream = response.getEntity().getContent();
				if(inputStream != null){
					resultString = convertInputStreamToString(inputStream);
					Log.d("result", resultString);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pd.dismiss();
			try{
				JSONObject jObj = new JSONObject(resultString);
				if(jObj.getString("status").equalsIgnoreCase("success")){
					mCallback.SendArrList(jObj.toString());
				}else{
					Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
    }
	/*
	 * Select speak
	 */
	private void selectSpeak(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View convertView = inflater.inflate(R.layout.speaks_list_for_dialog, null);
		alertDialog.setView(convertView);
		alertDialog.setTitle("Select Speak!");
		ListView lv = (ListView)convertView.findViewById(R.id.lvSpeak);
		SpeakDialogAdapter adapter = new SpeakDialogAdapter(getActivity(), speaksLst);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				speak.setText(speaksLst.get(arg2).getValue().substring(0, 1).toUpperCase() + speaksLst.get(arg2).getValue().substring(1));
			}
		});
		dialog = alertDialog.create();
		dialog.show();
	}
	/*
	 * Select job
	 */
	private void selectJob(){
		jobs = new String[] {"Student", "Doctor" , "Teacher"};
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View convertView = inflater.inflate(R.layout.jobs_list_for_dialog, null);
		alertDialog.setView(convertView);
		alertDialog.setTitle("Select Occupation!");
		ListView lv = (ListView)convertView.findViewById(R.id.lvJob);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, jobs);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				job.setText(jobs[arg2].toString());
			}
		});
		dialog = alertDialog.create();
		dialog.show();
	}
	/*
	 * get speaks
	 */
	private class FetchSpeaks extends AsyncTask<String, String, Void>{
		@Override
		protected Void doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall("http://muslimmarry.campcoders.com/api/v1/language", ServiceHandler.GET);
			if(jsonStr != null){
				speaksLst.clear();
				try{
					JSONObject jObj = new JSONObject(jsonStr);
					if(jObj.getString("status").equalsIgnoreCase("success")){
						JSONArray jArr = new JSONArray(jObj.getString("data"));
						Log.d("myTag", jArr.toString());
						for(int i=0; i<jArr.length(); i++){
							JSONObject data = jArr.getJSONObject(i);
							speaksLst.add(new GetSpeaks(data.getString("_id"), data.getString("name")));
						}
					}else{
						Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
	}
}
