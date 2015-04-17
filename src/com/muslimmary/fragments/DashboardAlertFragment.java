package com.muslimmary.fragments;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.muslimmarry.R;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.adapters.DashboardAlertAdapter;
import com.muslimmarry.helpers.helpers;
import com.muslimmarry.model.DashboardAlertItem;
import com.muslimmarry.sharedpref.prefUser;
import com.muslimmary.fragments.EditPhotoFragment.SendPhotoToShareFromEdit;


public class DashboardAlertFragment extends Fragment {
	
	ListView mList;
	ArrayList<DashboardAlertItem> mlst = new ArrayList<DashboardAlertItem>();
	DashboardAlertAdapter adapter;
	
	prefUser user;
	String userid = "";
	String token = "";
	
	String resultString;
	
	SendDataToGiftReceivePage mCallback;
	
	public interface SendDataToGiftReceivePage{
		public void SendDataToGiftReceivePageFragment(String userid_send, String username_send, String gift);
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try{
			mCallback = (SendDataToGiftReceivePage)activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_dashboard_alert, container, false);
		helpers.setTouch(rootView);
		
		mList = (ListView)rootView.findViewById(R.id.mList);
		
		// create user object
		user = new prefUser(getActivity());
		HashMap<String, String> user_info = user.getUserDetail();
		userid = user_info.get(prefUser.KEY_USERID);
		token = user_info.get(prefUser.KEY_TOKEN);
		
		// set background for bottom nav element
		((MainActivity)getActivity()).setBgGroupBell();
		
		// get notifi gift
		new GetNotifiGift().execute();
		
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				mCallback.SendDataToGiftReceivePageFragment(mlst.get(position).getUserId(), mlst.get(position).getUsernameSend(), mlst.get(position).getGift());
			}
		});
		
		return rootView;
	}
	/*
	 * Get notifi gift
	 */
	private class GetNotifiGift extends AsyncTask<String, String, Void>{
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			InputStream inputStream = null;
			try{
				JSONObject obj = new JSONObject();
				obj.put("token", token);
				obj.put("user_id", userid);
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://muslimmarry.campcoders.com:7777/api/get-gift");
				httppost.setEntity(new ByteArrayEntity(obj.toString().getBytes("UTF8")));
				httppost.setHeader("Accept", "application/json");
				httppost.setHeader("Content-type", "application/json;charset=UTF-8");
				httppost.setHeader("Accept-Charset", "utf-8");
				HttpResponse response = httpClient.execute(httppost);
				inputStream = response.getEntity().getContent();
				if(inputStream != null){
					resultString = helpers.convertInputStreamToString(inputStream);
					Log.d("result", resultString);
				}
			}catch(Exception e){
				Log.e("error", e.getMessage(), e);
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try{
				mlst.clear();
				JSONObject obj = new JSONObject(resultString);
				if(obj.getInt("error_code") == 0){
					JSONArray arr = obj.getJSONArray("gifts");
					for(int i=0; i<arr.length(); i++){
						JSONObject gift = arr.getJSONObject(i);
						
						String formattedTime = ConvertDateFormat(gift.getString("time"));
						String currentTime = GetCurrentTime("yyyy-MM-dd HH:mm:ss");
						
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date d1 = df.parse(formattedTime);
						Date d2 = df.parse(currentTime);
			 
						//in milliseconds
						long diff = d2.getTime() - d1.getTime();
						
						mlst.add(new DashboardAlertItem(gift.getString("_id"), new Date().getTime()-diff, gift.getInt("status"),
								gift.getString("image_url"), gift.getString("user_send"), gift.getString("username"), gift.getString("avatar")));
						adapter = new DashboardAlertAdapter(getActivity(), R.layout.row_dashboard_alert, mlst);
						mList.setAdapter(adapter);
					}
				}else{
					Toast.makeText(getActivity(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e){
				Log.e("error", e.getMessage(), e);
			}
		}
	}
	/*
	 * Get current time
	 */
	private String GetCurrentTime(String value){
		Calendar now = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(value);
		return sdf.format(now.getTime());
	}
	/*
	 * Convert date format
	 */
	private String ConvertDateFormat(String value){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try{
			d = df.parse(value);
		}catch(Exception e){}
		
		return output.format(d);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((MainActivity)getActivity()).setElementTopNav(true, true, false, false);
		((MainActivity)getActivity()).setTitle("activity");
		((MainActivity)getActivity()).showTopNav(true);
	}
}
