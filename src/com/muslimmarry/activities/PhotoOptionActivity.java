package com.muslimmarry.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muslimmarry.R;
import com.muslimmarry.helpers.helpers;

public class PhotoOptionActivity extends Activity implements OnClickListener, OnTouchListener {
	
	TextView title;
	TextView title2;
	ImageView back;
	Button upload;
	Button take;
	TextView notnow;
	
	String _uname = "";
	String _email = "";
	String _age = "";
	String _gender = "";
	String _pword = "";
	String country = "";
	String city = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_photo_option);
		
		title = (TextView)findViewById(R.id.title);
		title2 = (TextView)findViewById(R.id.title2);
		back = (ImageView)findViewById(R.id.back);
		upload = (Button)findViewById(R.id.upload);
		take = (Button)findViewById(R.id.take);
		notnow = (TextView)findViewById(R.id.notnow);
		
		// set font for element
		new helpers(getApplicationContext()).setFontTypeText(title);
		new helpers(getApplicationContext()).setFontTypeText(title2);
		new helpers(getApplicationContext()).setFontTypeButton(take);
		new helpers(getApplicationContext()).setFontTypeButton(upload);
		
		// set event for element
		upload.setOnClickListener(this);
		take.setOnClickListener(this);
		notnow.setOnClickListener(this);
		back.setOnTouchListener(this);
		
		// get data bundle
		try{
			Bundle getResults = getIntent().getExtras();
			_uname = getResults.getString("uname");
			_email = getResults.getString("email");
			_age = getResults.getString("age");
			_gender = getResults.getString("gender");
			_pword = getResults.getString("pword");
			country = getResults.getString("country");
			city = getResults.getString("city");
		}catch(NullPointerException e){
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i = null;
		Bundle bundle = null;
		switch (v.getId()) {
		case R.id.upload:
			i = new Intent(PhotoOptionActivity.this, PhotoCustomizationActivity.class);
			bundle = new Bundle();
			bundle.putString("uname", _uname);
			bundle.putString("email", _email);
			bundle.putString("age", _age);
			bundle.putString("gender", _gender);
			bundle.putString("pword", _pword);
			bundle.putString("country", country);
			bundle.putString("city", city);
			bundle.putString("flag", "upload photo");
			i.putExtras(bundle);
			startActivity(i);
			new helpers(PhotoOptionActivity.this).PushActivityLeft();
			break;
		case R.id.take:
			i = new Intent(PhotoOptionActivity.this, PhotoCustomizationActivity.class);
			bundle = new Bundle();
			bundle.putString("uname", _uname);
			bundle.putString("email", _email);
			bundle.putString("age", _age);
			bundle.putString("gender", _gender);
			bundle.putString("pword", _pword);
			bundle.putString("country", country);
			bundle.putString("city", city);
			bundle.putString("flag", "take photo");
			i.putExtras(bundle);
			startActivity(i);
			new helpers(PhotoOptionActivity.this).PushActivityLeft();
			break;
		case R.id.notnow:
			i = new Intent(PhotoOptionActivity.this, ManuallyLocateActivity.class);
			bundle = new Bundle();
			bundle.putString("uname", _uname);
			bundle.putString("email", _email);
			bundle.putString("age", _age);
			bundle.putString("gender", _gender);
			bundle.putString("pword", _pword);
			bundle.putString("photo", "");
			bundle.putString("country", country);
			bundle.putString("city", city);
			i.putExtras(bundle);
			startActivity(i);
			new helpers(PhotoOptionActivity.this).PushActivityLeft();
			break;
		default:
			break;
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
				new helpers(PhotoOptionActivity.this).PushActivityRight();
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
		new helpers(PhotoOptionActivity.this).PushActivityRight();
	}
}
