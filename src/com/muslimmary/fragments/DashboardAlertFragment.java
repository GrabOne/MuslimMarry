package com.muslimmary.fragments;

import java.io.InputStream;
import java.net.URISyntaxException;
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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muslimmarry.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.adapters.DashboardAlertAdapter;
import com.muslimmarry.helpers.helpers;
import com.muslimmarry.model.DashboardAlertItem;
import com.muslimmarry.sharedpref.prefUser;


public class DashboardAlertFragment extends Fragment {
	
	ListView mList;
	ArrayList<DashboardAlertItem> mlst = new ArrayList<DashboardAlertItem>();
	DashboardAlertAdapter adapter;
	ProgressBar progressbar;
	TextView msg;
	
	prefUser user;
	String userid = "";
	String token = "";
	
	String resultString = "";
	
	SendDataToGiftReceivePage mCallback;
	
	SharedPreferences prefs;
	
	Socket socket;
	String gift_id = "";
	
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
		progressbar = (ProgressBar)rootView.findViewById(R.id.progressbar);
		msg = (TextView)rootView.findViewById(R.id.msg);
		
		// create user object
		user = new prefUser(getActivity());
		HashMap<String, String> user_info = user.getUserDetail();
		userid = user_info.get(prefUser.KEY_USERID);
		token = user_info.get(prefUser.KEY_TOKEN);
		
		// create sharedprefs object
		prefs = getActivity().getSharedPreferences("user_info_pref", 0);
		
		// set background for bottom nav element
		((MainActivity)getActivity()).setBgGroupBell();
		
		// get notifi gift
		new GetNotifiGift().execute();
		
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				gift_id = mlst.get(position).getId();
				ReadNotifiReceiveGift();
				mCallback.SendDataToGiftReceivePageFragment(mlst.get(position).getUserId(), mlst.get(position).getUsernameSend(), mlst.get(position).getGift());
			}
		});
		
		return rootView;
	}
	/*
	 * read notification receiving gifts
	 */
	private void ReadNotifiReceiveGift(){
		try{
			IO.Options opts = new IO.Options();
			opts.forceNew = true;
			opts.reconnection = true;
	    	socket = IO.socket("http://muslimmarry.campcoders.com:7777/chat?user_id="+ userid +"&token=" + token, opts);
	    	socket.on(socket.EVENT_CONNECT, new Emitter.Listener() {
				
				@Override
				public void call(Object... arg0) {
					// TODO Auto-generated method stub
					try{
						JSONObject obj = new JSONObject();
						obj.put("user_view", userid);
						obj.put("gift_id", gift_id);
						socket.emit("view_gift", obj);
					}catch(Exception e){
						Log.e("error", e.getMessage(), e);
					}
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							int unread = 0;
							if(((MainActivity)getActivity()).GetNumNotifiGif() > 0){
								unread = ((MainActivity)getActivity()).GetNumNotifiGif() - 1;
							}
							if(unread > 0){
								((MainActivity)getActivity()).SetNumNotifiGift(true, String.valueOf(unread));
							}else{
								((MainActivity)getActivity()).SetNumNotifiGift(false, "0");
							}
							// update gift_unread value
							SharedPreferences.Editor editor = prefs.edit();
							editor.putString("gift_unread", String.valueOf(unread)).commit();
						}
					});
				}
			}).on("send_message", new Emitter.Listener() {
				
				@Override
				public void call(Object... arg0) {
					// TODO Auto-generated method stub
					final JSONObject obj = (JSONObject)arg0[0];
					Log.d("myTag", obj.toString());
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try{
								// update num notifi message
								int unread = ((MainActivity)getActivity()).GetNumNotifiMes() + obj.getInt("unread");
								((MainActivity)getActivity()).SetNumNotifiMes(true, String.valueOf(unread));
								// update unread value
								SharedPreferences.Editor editor = prefs.edit();
								editor.putString("unread", String.valueOf(unread)).commit();
							}catch(Exception e){
								Log.e("error", e.getMessage(), e);
							}
						}
					});
				}
			}).on("send_gift", new Emitter.Listener() {
				
				@Override
				public void call(Object... arg0) {
					// TODO Auto-generated method stub
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							int unread = ((MainActivity)getActivity()).GetNumNotifiGif() + 1;
							((MainActivity)getActivity()).SetNumNotifiGift(true, String.valueOf(unread));
							// update gift_unread value
							SharedPreferences.Editor editor = prefs.edit();
							editor.putString("gift_unread", String.valueOf(unread)).commit();
						}
					});
				}
			});
	    	socket.connect();
		}catch(URISyntaxException e){
			Log.e("error", e.getMessage(), e);
		}
	}
	
	/*
	 * Get notifi gift
	 */
	private class GetNotifiGift extends AsyncTask<String, String, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mList.setVisibility(View.GONE);
		}
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
			progressbar.setVisibility(View.GONE);
			mList.setVisibility(View.VISIBLE);
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
					if(mlst.size() <= 0){
						msg.setVisibility(View.VISIBLE);
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
	/*
	 * Read all message
	 */
	private void RealAllMessage(){
		try{
			IO.Options opts = new IO.Options();
			opts.forceNew = true;
			opts.reconnection = true;
        	socket = IO.socket("http://muslimmarry.campcoders.com:7777/chat?user_id="+ userid +"&token=" + token, opts);
        	
        	socket.connect();
        }catch(URISyntaxException e){
        	Log.e("error", e.getMessage(), e);
        }
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
