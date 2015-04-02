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

public class PhotoOptionActivity extends Activity implements OnClickListener {
	
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

		new helpers(getApplicationContext()).setFontTypeText(title);
		new helpers(getApplicationContext()).setFontTypeText(title2);
		new helpers(getApplicationContext()).setFontTypeButton(take);
		new helpers(getApplicationContext()).setFontTypeButton(upload);
		
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
					finish();
				default:
					break;
				}
				return true;
			}
		});
		// get bundle data
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
		
		upload.setOnClickListener(this);
		take.setOnClickListener(this);
		notnow.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.upload:
			Intent uploadIntent = new Intent(PhotoOptionActivity.this, PhotoCustomizationActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("uname", _uname);
			bundle.putString("email", _email);
			bundle.putString("age", _age);
			bundle.putString("gender", _gender);
			bundle.putString("pword", _pword);
			bundle.putString("country", country);
			bundle.putString("city", city);
			bundle.putString("flag", "upload photo");
			uploadIntent.putExtras(bundle);
			startActivity(uploadIntent);
			break;
		case R.id.take:
			Intent takeIntent = new Intent(PhotoOptionActivity.this, PhotoCustomizationActivity.class);
			Bundle bundle2 = new Bundle();
			bundle2.putString("uname", _uname);
			bundle2.putString("email", _email);
			bundle2.putString("age", _age);
			bundle2.putString("gender", _gender);
			bundle2.putString("pword", _pword);
			bundle2.putString("country", country);
			bundle2.putString("city", city);
			bundle2.putString("flag", "take photo");
			takeIntent.putExtras(bundle2);
			startActivity(takeIntent);
			break;
		case R.id.notnow:
			Intent notnowIntent = new Intent(PhotoOptionActivity.this, ManuallyLocateActivity.class);
			Bundle bundle3 = new Bundle();
			bundle3.putString("uname", _uname);
			bundle3.putString("email", _email);
			bundle3.putString("age", _age);
			bundle3.putString("gender", _gender);
			bundle3.putString("pword", _pword);
			bundle3.putString("avatar", "");
			bundle3.putString("country", country);
			bundle3.putString("city", city);
			notnowIntent.putExtras(bundle3);
			startActivity(notnowIntent);
			break;
		default:
			break;
		}
	}
}
