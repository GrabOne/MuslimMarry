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

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muslimmarry.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.adapters.MessagingPageAdapter;
import com.muslimmarry.helpers.helpers;
import com.muslimmarry.model.MessageItem;
import com.muslimmarry.sharedpref.prefUser;
import com.squareup.picasso.Picasso;


public class MessagingPageFragment extends Fragment implements OnClickListener {
	
	private MessagingPageAdapter adapter;
	
	ScrollView scroll;
	EditText etmessage;
	Button btnSend;
	SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView lvMsg;
    ImageView wallpaper;
    private ArrayList<MessageItem> comments = new ArrayList<MessageItem>();
    private ArrayList<MessageItem> mlst = new ArrayList<MessageItem>();
    
    prefUser user;
    String userid = "";
    String token = "";
    
    String friendId = "";
    String friendName = "";
    
    String resultString = "";
    
    Socket socket;
	String message = "";
	int skip = 0;
	int limit = 15;
	int more = 0;
	
	SharedPreferences prefs;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_messaging_page, container, false);
		helpers.setTouch(rootView);
		
		TextView name = (TextView)rootView.findViewById(R.id.name);
		wallpaper = (ImageView)rootView.findViewById(R.id.wallpaper);
		etmessage = (EditText)rootView.findViewById(R.id.etmessage);
		btnSend = (Button)rootView.findViewById(R.id.btnSend);
		mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh_layout);
        lvMsg = (ListView) rootView.findViewById(R.id.listMessage);
        adapter = new MessagingPageAdapter(getActivity(), R.layout.row_chat_message, comments);
        lvMsg.setAdapter(adapter);
		ImageView back = (ImageView)rootView.findViewById(R.id.back);
		
		// set event for element
		back.setOnClickListener(this);
		btnSend.setOnClickListener(this);
		// set background for bottom nav element
		((MainActivity)getActivity()).setBgGroupOriginal();
		
		// set font for element
		new helpers(getActivity()).setFontTypeText(name);
		
		// create user object
		user = new prefUser(getActivity());
		HashMap<String, String> user_info = user.getUserDetail();
		userid = user_info.get(prefUser.KEY_USERID);
		token = user_info.get(prefUser.KEY_TOKEN);
		
		// get data bundle
		try{
			friendId = getArguments().getString("friendId");
			friendName = getArguments().getString("name");
			name.setText(friendName);
		}catch(NullPointerException e){}
		
		// display photo url
		if(user_info.get(prefUser.KEY_PHOTO).length() > 0){
			Picasso.with(getActivity()).load(user_info.get(prefUser.KEY_PHOTO)).fit().centerCrop().into(wallpaper);
		}
		// click edittext to read message
		etmessage.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if(MotionEvent.ACTION_UP == arg1.getAction()){
					scrollToEndList();
                    RealAllMessage();
				}
				return false;
			}
		});
		// get message to display
		if(more == 0){
			new GetMessages().execute(String.valueOf(skip), String.valueOf(limit));
		}
		// scroll to the top load message
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if(more == 1){
					new GetMessages().execute(String.valueOf(skip), String.valueOf(limit));
				}
			}
		});
		
		// create prefs object
		prefs = getActivity().getSharedPreferences("user_info_pref", 0);
		
		// connect socket
		ConnSocket();
		
		return rootView;
	}
	/**
	 * Connect socket
	 */
	private void ConnSocket(){
		try{
			IO.Options opts = new IO.Options();
			opts.forceNew = true;
			opts.reconnection = true;
        	socket = IO.socket("http://muslimmarry.campcoders.com:7777/chat?user_id="+ userid +"&token=" + token, opts);
        	socket.on("send_message", new Emitter.Listener() {
				
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
								if(unread > 0){
									((MainActivity)getActivity()).SetNumNotifiMes(true, String.valueOf(unread));
								}
								// update unread value
								SharedPreferences.Editor editor = prefs.edit();
								editor.putString("unread", String.valueOf(unread)).commit();
								// add message
								String time = GetCurrentTime("HH:mm");
								addItems(true, obj.getString("content"), time);
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
							int unread = ((MainActivity) getActivity()).GetNumNotifiGif() + 1;
							((MainActivity) getActivity()).SetNumNotifiGift(true, String.valueOf(unread));
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
	 * Read all message
	 */
	private void RealAllMessage(){
		try{
			IO.Options opts = new IO.Options();
			opts.forceNew = true;
			opts.reconnection = true;
        	socket = IO.socket("http://muslimmarry.campcoders.com:7777/chat?user_id="+ userid +"&token=" + token, opts);
        	socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
				
				@Override
				public void call(Object... arg0) {
					// TODO Auto-generated method stub
					try{
						JSONObject obj = new JSONObject();
						obj.put("user_send", friendId);
						obj.put("user_recei", userid);
						socket.emit("readAllMessage", obj);
						getActivity().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								// update num notifi message
								int unread = 0;
								if(((MainActivity)getActivity()).GetNumNotifiMes() > 0){
									unread = ((MainActivity)getActivity()).GetNumNotifiMes() - 1;
								}
								if(unread > 0){
									((MainActivity)getActivity()).SetNumNotifiMes(true, String.valueOf(unread));
								}else{
									((MainActivity)getActivity()).SetNumNotifiMes(false, String.valueOf(unread));
								}
								// update unread value
								SharedPreferences.Editor editor = prefs.edit();
								editor.putString("unread", String.valueOf(unread)).commit();
							}
						});
					}catch(Exception e){
						Log.e("error", e.getMessage(), e);
					}
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
								if(unread > 0){
									((MainActivity)getActivity()).SetNumNotifiMes(true, String.valueOf(unread));
								}
								// update unread value
								SharedPreferences.Editor editor = prefs.edit();
								editor.putString("unread", String.valueOf(unread)).commit();
								// add message
								String time = GetCurrentTime("HH:mm");
								addItems(true, obj.getString("content"), time);
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
							int unread = ((MainActivity) getActivity()).GetNumNotifiGif() + 1;
							((MainActivity) getActivity()).SetNumNotifiGift(true, String.valueOf(unread));
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
	private void scrollToEndList(){
        if(adapter==null || adapter.getCount() < 1){
            Log.d("Scroll List", "Adapter null or count less than 1");
            return;
        }
        lvMsg.smoothScrollToPosition(adapter.getCount());
    }
    private void addItems(boolean left, String txt, String time) {
        adapter.add(new MessageItem(left, txt, time));
    }
    @Override
    public void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	((MainActivity)getActivity()).showTopNav(false);
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			((MainActivity)getActivity()).hideKeyboard();
			getFragmentManager().popBackStack();
			break;
		case R.id.btnSend:
			if (etmessage.getText().toString().length() > 0) {
                // Them Item vao khung chat. Xac dinh left de hien thi doan chat cua tung nguoi
				String time = GetCurrentTime("HH:mm");
                addItems(false, etmessage.getText().toString(), time);
                scrollToEndList();
                try{
                	message = etmessage.getText().toString();
                	IO.Options opts = new IO.Options();
					opts.forceNew = true;
					opts.reconnection = true;
	            	socket = IO.socket("http://muslimmarry.campcoders.com:7777/chat?user_id="+ userid +"&token=" + token, opts);
	            	socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
						
						@Override
						public void call(Object... arg0) {
							// TODO Auto-generated method stub
							try{
			            		JSONObject obj = new JSONObject();
			            		obj.put("user_send", userid);
			            		obj.put("user_recei", friendId);
			            		obj.put("content", message);
			            		socket.emit("send_message", obj);
			            	}catch(Exception e){
			            		Log.e("error", e.getMessage(), e);
			            	}
						}
					}).on("send_message_success", new Emitter.Listener() {
						
						@Override
						public void call(Object... arg0) {
							// TODO Auto-generated method stub
							JSONObject obj = (JSONObject)arg0[0];
							Log.d("myTag", obj.toString());
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
										if(unread > 0){
											((MainActivity)getActivity()).SetNumNotifiMes(true, String.valueOf(unread));
										}
										// update unread value
										SharedPreferences.Editor editor = prefs.edit();
										editor.putString("unread", String.valueOf(unread)).commit();
										// add message
										String time = GetCurrentTime("HH:mm");
										addItems(true, obj.getString("content"), time);
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
									int unread = ((MainActivity) getActivity()).GetNumNotifiGif() + 1;
									((MainActivity) getActivity()).SetNumNotifiGift(true, String.valueOf(unread));
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
                etmessage.setText("");
            }
		default:
			break;
		}
	}
	/**
	 * Get messages
	 */
	private class GetMessages extends AsyncTask<String, String, Void>{
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			InputStream inputStream = null;
			try{
				JSONObject obj = new JSONObject();
				obj.put("token", token);
				obj.put("user_id", userid);
				obj.put("friend_id", friendId);
				obj.put("skip", Integer.parseInt(params[0]));
				obj.put("limit", Integer.parseInt(params[1]));
				Log.d("obj", obj.toString());
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://muslimmarry.campcoders.com:7777/api/get-message");
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
			mSwipeRefreshLayout.setRefreshing(false);
			try{
				JSONObject obj = new JSONObject(resultString);
				if(obj.getInt("error_code") == 0){
					JSONArray arr = obj.getJSONArray("messages");
					mlst.clear();
					for(int i=arr.length()-1; i>=0; i--){
						JSONObject message = arr.getJSONObject(i);
						
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
						df.setTimeZone(TimeZone.getTimeZone("UTC"));
						SimpleDateFormat output = new SimpleDateFormat("HH:mm");
						Date d = df.parse(message.getString("time"));
						String formattedTime = output.format(d);
						
						if(message.getString("user_recei").equalsIgnoreCase(userid)){
							mlst.add(new MessageItem(true, message.getString("content"), formattedTime));
						}else{
							mlst.add(new MessageItem(false, message.getString("content"), formattedTime));
						}
					}
					if(mlst.size() > 0){
						for(int i = mlst.size()-1; i>=0; i--){
							comments.add(0, new MessageItem(mlst.get(i).left, mlst.get(i).comment, mlst.get(i).time));
						}
					}
					adapter.notifyDataSetChanged();
					more = 1;
					skip += 15;
				}else{
					Toast.makeText(getActivity(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e){
				Log.e("error", e.getMessage(), e);
			}
		}
	}
	/*
	 * Get current time hour:minute
	 */
	private String GetCurrentTime(String value){
		Calendar now = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(value);
		String strDate = sdf.format(now.getTime());
		return strDate;
	}
}
