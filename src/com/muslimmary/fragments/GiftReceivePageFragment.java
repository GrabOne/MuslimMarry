package com.muslimmary.fragments;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muslimmarry.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.helpers.ServiceHandler;
import com.muslimmarry.helpers.helpers;
import com.muslimmarry.model.SendGiftItem;
import com.muslimmarry.sharedpref.prefUser;
import com.squareup.picasso.Picasso;

public class GiftReceivePageFragment extends Fragment implements OnClickListener {
	
	ImageView more;
	ViewGroup profile_detail;
	ImageView heart_selected;
	ImageView iv;
	ImageView gift;
	ImageView invisible;
	ImageView report_spam;
	EditText etmessage;
	TextView age;
	TextView city;
	TextView height;
	TextView occupation;
	LinearLayout llLanguage;
	
	Socket socket;
	String message = "";
	
	SharedPreferences prefs;
	prefUser user;
	String userid = "";
	String token = "";
	String user_id_viewing;
	String _name_viewing = "";
	
	ArrayList<SendGiftItem> mlst = new ArrayList<SendGiftItem>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_gift_receive_page, container, false);
		helpers.setTouch(rootView);
		
		profile_detail = (ViewGroup)rootView.findViewById(R.id.profile_detail);
		iv = (ImageView)rootView.findViewById(R.id.iv);
		iv.setTag(R.drawable.ic_heart_unselected);
		heart_selected = (ImageView)rootView.findViewById(R.id.heart_selected);
		heart_selected.setTag(R.drawable.ic_heart_unselected);
		gift = (ImageView)rootView.findViewById(R.id.gift);
		invisible = (ImageView)rootView.findViewById(R.id.invisible);
		report_spam = (ImageView)rootView.findViewById(R.id.report_spam);
		more = (ImageView)rootView.findViewById(R.id.more);
		more.setTag(R.drawable.arrow_up);
		TextView name = (TextView)rootView.findViewById(R.id.name);
		TextView title1 = (TextView)rootView.findViewById(R.id.title1);
		ImageView gift_received = (ImageView)rootView.findViewById(R.id.gift_received);
		etmessage = (EditText)rootView.findViewById(R.id.etmessage);
		Button btnSend = (Button)rootView.findViewById(R.id.btnSend);
		age = (TextView)rootView.findViewById(R.id.age);
		city = (TextView)rootView.findViewById(R.id.city);
		height = (TextView)rootView.findViewById(R.id.height);
		occupation = (TextView)rootView.findViewById(R.id.occupation);
		llLanguage = (LinearLayout)rootView.findViewById(R.id.llLanguage);
		
		// get bundle data
		try{
			String username_send = getArguments().getString("username_send");
			String gift_url = getArguments().getString("gift");
			user_id_viewing = getArguments().getString("userid_send");
			title1.setText("A gift from " + username_send);
			name.setText(username_send);
			etmessage.setHint("Send " + username_send + " a thank you message...");
			Picasso.with(getActivity()).load(gift_url).into(gift_received);
			new GetProfileUserSentGift().execute(user_id_viewing);
		}catch(NullPointerException e){
			Log.e("error", e.getMessage(), e);
		}
		
		// set background fo bottom nav element
		((MainActivity)getActivity()).setBgGroupOriginal();
		
		// set event for element
		more.setOnClickListener(this);
		heart_selected.setOnClickListener(this);
		iv.setOnClickListener(this);
		invisible.setOnClickListener(this);
		gift.setOnClickListener(this);
		report_spam.setOnClickListener(this);
		btnSend.setOnClickListener(this);
		
		// create user object
		user = new prefUser(getActivity());
		HashMap<String, String> user_info = user.getUserDetail();
		userid = user_info.get(prefUser.KEY_USERID);
		token = user_info.get(prefUser.KEY_TOKEN);
		
		// create prefs object
		prefs = getActivity().getSharedPreferences("user_info_pref", 0);
		
		// get gift
		new GetGift().execute();
		
		return rootView;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.more:
			if((Integer)more.getTag() == R.drawable.arrow_up){
				more.setTag(R.drawable.arrow_down);
				more.setImageResource(R.drawable.arrow_down);
				profile_detail.setVisibility(View.VISIBLE);
				iv.setVisibility(View.GONE);
			}else{
				more.setTag(R.drawable.arrow_up);
				more.setImageResource(R.drawable.arrow_up);
				profile_detail.setVisibility(View.GONE);
				iv.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.heart_selected:
			if((Integer)heart_selected.getTag() == R.drawable.ic_heart_selected){
				heart_selected.setImageResource(R.drawable.ic_heart_unselected);
				heart_selected.setTag(R.drawable.ic_heart_unselected);
				iv.setImageResource(R.drawable.ic_heart_unselected);
				iv.setTag(R.drawable.ic_heart_unselected);
			}else{
				heart_selected.setImageResource(R.drawable.ic_heart_selected);
				heart_selected.setTag(R.drawable.ic_heart_selected);
				iv.setImageResource(R.drawable.ic_heart_selected);
				iv.setTag(R.drawable.ic_heart_selected);
			}
			break;
		case R.id.iv:
			if((Integer)iv.getTag() == R.drawable.ic_heart_selected){
				iv.setImageResource(R.drawable.ic_heart_unselected);
				iv.setTag(R.drawable.ic_heart_unselected);
				heart_selected.setImageResource(R.drawable.ic_heart_unselected);
				heart_selected.setTag(R.drawable.ic_heart_unselected);
			}else{
				iv.setImageResource(R.drawable.ic_heart_selected);
				iv.setTag(R.drawable.ic_heart_selected);
				heart_selected.setImageResource(R.drawable.ic_heart_selected);
				heart_selected.setTag(R.drawable.ic_heart_selected);
			}
			break;
		case R.id.invisible:
			AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
			alertDialog.setMessage("Do you want to block this user?");
			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Permanently", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					invisible.setImageResource(R.drawable.invisible_actived);
				}
			});
			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "30 Days", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					invisible.setImageResource(R.drawable.invisible_actived);
				}
			});
			alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
			});
			alertDialog.show();
			break;
		case R.id.gift:
			((MainActivity)getActivity()).displayPopupSendGift(v, mlst, _name_viewing, user_id_viewing);
			break;
		case R.id.report_spam:
			report_spam.setImageResource(R.drawable.spam_actived);
			Toast.makeText(getActivity(), "Sent report to admin successful!", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnSend:
			if(etmessage.getText().toString().length() > 0){
				message = etmessage.getText().toString();
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
			            		obj.put("user_send", userid);
			            		obj.put("user_recei", user_id_viewing);
			            		obj.put("content", message);
			            		socket.emit("send_message", obj);
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
									}catch(Exception e){
										Log.e("error", e.getMessage(), e);
									}
								}
							});
						}
					}).on("send_message_success", new Emitter.Listener() {
						
						@Override
						public void call(Object... arg0) {
							// TODO Auto-generated method stub
							JSONObject obj = (JSONObject)arg0[0];
							Log.d("myTag", obj.toString());
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
			break;
		default:
			break;
		}
	}
	/*
	 * Get profile user sent gift
	 */
	private class GetProfileUserSentGift extends AsyncTask<String, String, Void>{
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall(helpers.url+"profile/"+params[0], ServiceHandler.GET);
			if(jsonStr != null){
				try{
					Log.d("myTag", jsonStr);
					JSONObject obj = new JSONObject(jsonStr);
					if(obj.getString("status").equalsIgnoreCase("success")){
						JSONObject data = new JSONObject(obj.getString("data"));
						JSONObject locate = new JSONObject(data.getString("location"));
						JSONArray language = data.getJSONArray("language");
						String _username = "";
						String _nickname = "";
						if(!data.isNull("username")){
							_username = data.getString("username");
						}
						if(!data.isNull("nickname")){
							_nickname = data.getString("nickname");
						}
						if(_username.length() > 0){
							_name_viewing = _username;
						}else{
							_name_viewing = _nickname;
						}
						
						for(int i=0; i<language.length(); i++){
							TextView value = new TextView(getActivity());
							value.setText(language.get(i).toString().toUpperCase());
							value.setTextColor(Color.parseColor("#101010"));
							value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
							value.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
							((LinearLayout)llLanguage).addView(value);
						}
						String _age = data.getString("age");
						String _city = locate.getString("city");
						String _height = data.getString("height");
						String _occupation = data.getString("occupation");
						age.setText(_age);
						city.setText(_city.toUpperCase());
						height.setText(_height);
						occupation.setText(_occupation.toUpperCase());
					}else{
						Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
					}
				}catch(Exception e){
					Log.e("error", e.getMessage(), e);
				}
			}
			return null;
		}
	}
	/*
	 * Get gift from server
	 */
	private class GetGift extends AsyncTask<String, String, Void>{
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall("http://muslimmarry.campcoders.com:7777/api/get-gift-url", ServiceHandler.GET);
			if(jsonStr != null){
				mlst.clear();
				try{
					JSONObject obj = new JSONObject(jsonStr);
					if(obj.getInt("error_code") == 0){
						JSONArray arr = obj.getJSONArray("gift_url");
						Log.d("myTag", arr.toString());
						for(int i=0; i<arr.length(); i++){
							mlst.add(new SendGiftItem(arr.get(i).toString(), false));
						}
					}else{
						Toast.makeText(getActivity(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			return null;
		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((MainActivity)getActivity()).setElementTopNav(true, false, true, false);
		((MainActivity)getActivity()).showTopNav(true);
	}
}
