package com.muslimmary.fragments;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
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
import com.muslimmarry.helpers.TransparentProgressDialog;
import com.muslimmarry.helpers.helpers;
import com.muslimmarry.model.SendGiftItem;
import com.muslimmarry.sharedpref.prefUser;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment implements OnClickListener {
	
	ImageView more;
	ViewGroup box_profile;
	ViewGroup profile_detail;
	ImageView heart_selected;
	ImageView iv;
	ImageView gift;
	ImageView invisible;
	ImageView report_spam;
	ImageView photo;
	TextView name;
	TextView age;
	TextView city;
	TextView height;
	TextView occupation;
	LinearLayout llLanguage;
	EditText etmessage;
	
	String _user_id_viewing = "";
	String _photo = "";
	String _name = "";
	String _username = "";
	String _age = "";
	String _language = "";
	String _height = "";
	String _occupation = "";
	String _city = "";
	
	prefUser user;
	String userid = "";
	String token = "";

	String resultString = "";
	
	TransparentProgressDialog pd;
	
	SharedPreferences prefs;
	
	Socket socket;
	String message = "";
	
	ArrayList<SendGiftItem> mlst = new ArrayList<SendGiftItem>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		helpers.setTouch(rootView);
		box_profile = (ViewGroup)rootView.findViewById(R.id.box_profile);
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
		Button option = (Button)rootView.findViewById(R.id.option);
		ImageView back = (ImageView)rootView.findViewById(R.id.back);
		photo = (ImageView)rootView.findViewById(R.id.photo);
		name = (TextView)rootView.findViewById(R.id.name);
		age = (TextView)rootView.findViewById(R.id.age);
		city = (TextView)rootView.findViewById(R.id.city);
		height = (TextView)rootView.findViewById(R.id.height);
		occupation = (TextView)rootView.findViewById(R.id.occupation);
		llLanguage = (LinearLayout)rootView.findViewById(R.id.llLanguage);
		Button btnSend = (Button)rootView.findViewById(R.id.btnSend);
		etmessage = (EditText)rootView.findViewById(R.id.etmessage);
		
		// create user object
		user = new prefUser(getActivity());
		HashMap<String, String> user_info = user.getUserDetail();
		userid = user_info.get(prefUser.KEY_USERID);
		token = user_info.get(prefUser.KEY_TOKEN);
		
		try{
			_user_id_viewing = getArguments().getString("id");
			_photo = getArguments().getString("photo");
			_name = getArguments().getString("name");
			_username = getArguments().getString("username");
			_age = getArguments().getString("age");
			_height = getArguments().getString("height");
			_occupation = getArguments().getString("occupation");
			_language = getArguments().getString("language");
			_city = getArguments().getString("city");
			if(_photo.length() > 0){
				Picasso.with(getActivity()).load(_photo).fit().centerCrop().into(photo);
			}
			if(_name.length() <= 0){
				name.setText(_username);
			}else{
				name.setText(_name);
			}
			age.setText(_age);
			height.setText(_height);
			occupation.setText(_occupation.toUpperCase());
			city.setText(_city.toUpperCase());
			JSONArray jArr = new JSONArray(_language);
			for(int i=0; i<jArr.length(); i++){
				TextView value = new TextView(getActivity());
				value.setText(jArr.get(i).toString().toUpperCase());
				value.setTextColor(Color.parseColor("#101010"));
				value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
				value.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				((LinearLayout)llLanguage).addView(value);
			}
		}catch(NullPointerException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		// set background for bottm nav element
		((MainActivity)getActivity()).setBgGroupOriginal();
		
		// set event for element
		back.setOnClickListener(this);
		more.setOnClickListener(this);
		option.setOnClickListener(this);
		heart_selected.setOnClickListener(this);
		iv.setOnClickListener(this);
		invisible.setOnClickListener(this);
		gift.setOnClickListener(this);
		report_spam.setOnClickListener(this);
		btnSend.setOnClickListener(this);
		
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
		case R.id.back:
			((MainActivity)getActivity()).hideKeyboard();
			getFragmentManager().popBackStack();
			break;
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
		case R.id.option:
			((MainActivity)getActivity()).showRightMenu();
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
					new BlockUser().execute("2");
				}
			});
			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "30 Days", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					new BlockUser().execute("1");
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
			if(_name.length() <= 0){
				((MainActivity)getActivity()).displayPopupSendGift(v, mlst, _username, _user_id_viewing);
			}else{
				((MainActivity)getActivity()).displayPopupSendGift(v, mlst, _name, _user_id_viewing);
			}
			break;
		case R.id.report_spam:
			report_spam.setImageResource(R.drawable.spam_actived);
			Toast.makeText(getActivity(), "Sent report to admin successful!", Toast.LENGTH_LONG).show();
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
			            		obj.put("user_recei", _user_id_viewing);
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
		default:
			break;
		}
	}
	/*
	 * block user
	 */
	private class BlockUser extends AsyncTask<String, String, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new TransparentProgressDialog(getActivity(), R.drawable.loading_spinner);
			pd.show();
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			InputStream inputStream = null;
			try{
				JSONObject jObj = new JSONObject();
				jObj.put("user_id", userid);
				jObj.put("remember_token", token);
				jObj.put("user_block_id", _user_id_viewing);
				jObj.put("type", Integer.parseInt(params[0]));
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(helpers.url+"block-user");
				httppost.setEntity(new ByteArrayEntity(jObj.toString().getBytes("UTF8")));
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
					invisible.setImageResource(R.drawable.invisible_actived);
					Toast.makeText(getActivity(), "Block this user successful!", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(getActivity(), "Block this user unsuccessful!", Toast.LENGTH_LONG).show();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
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
		((MainActivity)getActivity()).showTopNav(false);
	}
}
