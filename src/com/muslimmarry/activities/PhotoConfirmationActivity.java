package com.muslimmarry.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class PhotoConfirmationActivity extends Activity implements OnClickListener, OnTouchListener {
	
	ImageView back;
	
	String _uname = "";
	String _email = "";
	String _age = "";
	String _gender = "";
	String _pword = "";
	String _photo = "";
	String country = "";
	String city = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_photo_confirmation);
		back = (ImageView)findViewById(R.id.back);
		
		Button btn = (Button)findViewById(R.id.btn);
		TextView title = (TextView)findViewById(R.id.title);
		ImageView photo = (ImageView)findViewById(R.id.photo);
		
		// set font for element
		new helpers(getApplicationContext()).setFontTypeText(title);
		new helpers(getApplicationContext()).setFontTypeButton(btn);
		
		// set event for element
		back.setOnTouchListener(this);
		btn.setOnClickListener(this);
		
		// get data bundle
		try{
			Bundle getResults = getIntent().getExtras();
			_uname = getResults.getString("uname");
			_email = getResults.getString("email");
			_age = getResults.getString("age");
			_gender = getResults.getString("gender");
			_pword = getResults.getString("pword");
			_photo = getResults.getString("photo");
			country = getResults.getString("country");
			city = getResults.getString("city");
		}catch(NullPointerException e){
			e.printStackTrace();
		}
		
		// display photo bitmap
		try{
			Bitmap bm = BitmapFactory.decodeFile(getIntent().getExtras().getString("picturePath"));
			photo.setImageBitmap(bm);
		}catch(NullPointerException e){}
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
				new helpers(PhotoConfirmationActivity.this).PushActivityRight();
			default:
				break;
			}
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.btn){
			Intent i = new Intent(PhotoConfirmationActivity.this, LocationActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("uname", _uname);
			bundle.putString("email", _email);
			bundle.putString("age", _age);
			bundle.putString("gender", _gender);
			bundle.putString("pword", _pword);
			bundle.putString("photo", _photo);
			bundle.putString("country", country);
			bundle.putString("city", city);
			i.putExtras(bundle);
			startActivity(i);
			new helpers(PhotoConfirmationActivity.this).PushActivityLeft();
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		new helpers(PhotoConfirmationActivity.this).PushActivityRight();
	}
}
