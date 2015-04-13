package com.muslimmary.fragments;

import java.io.InputStream;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.muslimmarry.R;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.helpers.TransparentProgressDialog;
import com.muslimmarry.helpers.helpers;
import com.muslimmarry.sharedpref.prefUser;

public class AccountSettingFragment extends Fragment implements OnClickListener {
	
	RelativeLayout row2;
	RelativeLayout row3;
	RelativeLayout row4;
	View line2;
	View line3;
	View line4;
	EditText name;
	EditText mail;
	EditText pword;
	EditText repword;
	EditText pc;
	ImageView ic_name;
	ImageView ic_pword;
	ImageView ic_pc;
	Button done;
	
	prefUser user;
	String userid = "";
	String token = "";
	String is_social = "";
	
	String resultString = "";
	
	// Shared pref mode
    int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREF_NAME = "user_info_pref";
    
    SharedPreferences pref;
    Editor editor;
    
    TransparentProgressDialog pd;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_account_setting, container, false);
		helpers.setTouch(rootView);
		
		row2 = (RelativeLayout)rootView.findViewById(R.id.row2);
		row3 = (RelativeLayout)rootView.findViewById(R.id.row3);
		row4 = (RelativeLayout)rootView.findViewById(R.id.row4);
		line2 = (View)rootView.findViewById(R.id.line2);
		line3 = (View)rootView.findViewById(R.id.line3);
		line4 = (View)rootView.findViewById(R.id.line4);
		name  =(EditText)rootView.findViewById(R.id.name);
		mail  =(EditText)rootView.findViewById(R.id.mail);
		pword  =(EditText)rootView.findViewById(R.id.pword);
		repword = (EditText)rootView.findViewById(R.id.repword);
		pc  = (EditText)rootView.findViewById(R.id.pc);
		ic_name = (ImageView)rootView.findViewById(R.id.ic_name);
		ic_pword = (ImageView)rootView.findViewById(R.id.ic_pword);
		ic_pc = (ImageView)rootView.findViewById(R.id.ic_pc);
		done = (Button)rootView.findViewById(R.id.done);
		
		// set background for bottom nav element
		((MainActivity)getActivity()).setBgGroupOriginal();
		
		// set font for element
		new helpers(getActivity()).setFontTypeButton(done);
		
		// create pref object
		pref = getActivity().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
		
		// create user object
		user = new prefUser(getActivity());
		HashMap<String, String> user_info = user.getUserDetail();
		if(user_info.get(prefUser.KEY_IS_SOCIAL).equalsIgnoreCase("true")){
			row3.setVisibility(View.GONE);
			line3.setVisibility(View.GONE);
		}
		if(user_info.get(prefUser.KEY_EMAIL).length() <= 0){
			row2.setVisibility(View.GONE);
			line2.setVisibility(View.GONE);
		}
		userid = user_info.get(prefUser.KEY_USERID);
		token = user_info.get(prefUser.KEY_TOKEN);
		is_social = user_info.get(prefUser.KEY_IS_SOCIAL);
		name.setText(user_info.get(prefUser.KEY_NAME));
		mail.setText(user_info.get(prefUser.KEY_EMAIL));
		pc.setText(user_info.get(prefUser.KEY_CODE));
		
		// set event for element
		ic_name.setOnClickListener(this);
		ic_pword.setOnClickListener(this);
		ic_pc.setOnClickListener(this);
		done.setOnClickListener(this);
		
		return rootView;
	}
	
	/*
	 * update normal account
	 */
	private class UpdateNormalAccount extends AsyncTask<String, String, Void>{
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
				if(name.getText().toString().length() > 0){
					jObj.put("nickname", name.getText().toString());
				}
				if(pword.getText().toString().length() > 0){
					jObj.put("password", pword.getText().toString());
				}
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpPut httpput = new HttpPut(helpers.url+"edit-normal-account");
				httpput.setEntity(new ByteArrayEntity(jObj.toString().getBytes("UTF8")));
				httpput.setHeader("Accept", "application/json");
				httpput.setHeader("Content-type", "application/json;charset=UTF-8");
				httpput.setHeader("Accept-Charset", "utf-8");
				
				HttpResponse response = httpClient.execute(httpput);
				
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
					if(name.getText().toString().length() > 0){
						editor.putString("name", name.getText().toString());
					}
					editor.commit();
					
					name.setFocusable(false);
					pword.setFocusable(false);
					row4.setVisibility(View.GONE);
					line4.setVisibility(View.GONE);
					Toast.makeText(getActivity(), "Save successful!", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	/*
	 * Update social account
	 */
	private class UpdateSocialAccount extends AsyncTask<String, String, Void>{
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
				if(name.getText().toString().length() > 0){
					jObj.put("nickname", name.getText().toString());
				}
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpPut httpput = new HttpPut(helpers.url+"edit-social-account");
				httpput.setEntity(new ByteArrayEntity(jObj.toString().getBytes("UTF8")));
				httpput.setHeader("Accept", "application/json");
				httpput.setHeader("Content-type", "application/json;charset=UTF-8");
				httpput.setHeader("Accept-Charset", "utf-8");
				
				HttpResponse response = httpClient.execute(httpput);
				
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
					if(name.getText().toString().length() > 0){
						editor.putString("name", name.getText().toString());
					}
					editor.commit();
					
					name.setFocusable(false);
					pword.setFocusable(false);
					row4.setVisibility(View.GONE);
					line4.setVisibility(View.GONE);
					Toast.makeText(getActivity(), "Save successful!", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ic_name:
			name.setFocusableInTouchMode(true);
			name.setCursorVisible(true);
			name.requestFocus();
			break;
		case R.id.ic_pword:
			pword.setFocusableInTouchMode(true);
			pword.setCursorVisible(true);
			pword.requestFocus();
			row4.setVisibility(View.VISIBLE);
			line4.setVisibility(View.VISIBLE);
			break;
		case R.id.ic_pc:
			((MainActivity)getActivity()).displayPopupSharePromocode(v);
			break;
		case R.id.done:
			if(is_social.equalsIgnoreCase("true")){
				new UpdateSocialAccount().execute();
			}else{
				if(row4.getVisibility() == View.VISIBLE){
					if(pword.getText().toString().length() <= 0){
						Toast.makeText(getActivity(), "Please enter password!", Toast.LENGTH_SHORT).show();
					}else if(!pword.getText().toString().equalsIgnoreCase(repword.getText().toString())){
						Toast.makeText(getActivity(), "Password not match!", Toast.LENGTH_SHORT).show();
					}else{
						new UpdateNormalAccount().execute();
					}
				}else{
					new UpdateNormalAccount().execute();
				}
			}
			break;
		default:
			break;
		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((MainActivity)getActivity()).setElementTopNav(true, true, false, false);
		((MainActivity)getActivity()).setTitle("account");
		((MainActivity)getActivity()).showTopNav(true);
	}
}
