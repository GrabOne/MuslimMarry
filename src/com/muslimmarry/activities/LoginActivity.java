package com.muslimmarry.activities;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.muslimmarry.R;
import com.muslimmarry.helpers.TransparentProgressDialog;
import com.muslimmarry.helpers.helpers;
import com.muslimmarry.sharedpref.prefUser;

public class LoginActivity extends Activity implements OnTouchListener, OnClickListener {
	
	ImageView back;
	EditText uname;
	EditText pword;
	
	String resultString = "";
	
	TransparentProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		Button btnLogin = (Button)findViewById(R.id.btnLogin);
		back = (ImageView)findViewById(R.id.back);
		uname = (EditText)findViewById(R.id.uname);
		pword = (EditText)findViewById(R.id.pword);
		
		// set event for element
		back.setOnTouchListener(this);
		btnLogin.setOnClickListener(this);
		
		// set font for element
		new helpers(getApplicationContext()).setFontTypeButton(btnLogin);
	}
	
	/*
	 * Signin
	 */
	private class Signin extends AsyncTask<String, String, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new TransparentProgressDialog(LoginActivity.this, R.drawable.loading_spinner);
			pd.show();
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			InputStream inputStream = null;
			try{
				JSONObject jObj = new JSONObject();
				jObj.put("username", uname.getText().toString());
				jObj.put("password", pword.getText().toString());
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(helpers.url+"login");
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
					JSONObject data = new JSONObject(jObj.getString("data"));
					JSONArray languageArr = data.getJSONArray("language");
					JSONObject locate = new JSONObject(data.getString("location"));
					JSONObject coordinates = new JSONObject(locate.getString("coordinates"));
					String birthday = "";
					if(!data.isNull("birthday")){
						birthday = data.getString("birthday");
					}
					String album = "";
					if(!data.isNull("images")){
						JSONArray albumArr = data.getJSONArray("images");
						album = albumArr.toString();
					}
					// create user object
					prefUser user = new prefUser(LoginActivity.this);
					user.createUserSession(data.getString("_id"), new String(data.getString("nickname").getBytes("UTF-8"), "UTF-8"), data.getString("username"), data.getString("email"), data.getString("age"),
							birthday, data.getString("gender"), data.getString("avatar"), album, data.getString("remember_token"), data.getString("occupation"), data.getString("height"), languageArr.toString(),
							locate.getString("country"), locate.getString("city"), coordinates.getString("lat"), coordinates.getString("lng"), data.getString("promocode"), "", "", "false");
					Intent i = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(i);
					new helpers(LoginActivity.this).PushActivityLeft();
				}else{
					Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.btnLogin){
			if(uname.getText().toString().length() <= 0 || pword.getText().toString().length() <= 0){
				Toast.makeText(getApplicationContext(), "Please enter username or passowrd!", Toast.LENGTH_SHORT).show();
			}else{
				new Signin().execute();
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.back){
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				back.setBackgroundColor(Color.parseColor("#2e9dbc"));
				break;
			case MotionEvent.ACTION_UP:
				back.setBackgroundColor(Color.TRANSPARENT);
				finish();
				new helpers(LoginActivity.this).PushActivityRight();
			default:
				break;
			}
		}
		return true;
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		new helpers(LoginActivity.this).PushActivityRight();;
	}
}
