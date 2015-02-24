package com.example.muslimmarry;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WhereAreYou extends Activity {
	
	ImageView back;
	
	String _uname = "";
	String _email = "";
	String _age = "";
	String _gender = "";
	String _pword = "";
	String country = "";
	String city = "";
	String _avatar = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.where_are_you);
		TextView title = (TextView)findViewById(R.id.title);
		back = (ImageView)findViewById(R.id.back);
		Button auto = (Button)findViewById(R.id.auto);
		Button manually = (Button)findViewById(R.id.manually);
		setFontTypeText(title);
		setFontTypeButton(auto);
		setFontTypeButton(manually);
		
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
			_avatar = getResults.getString("avatar");
			country = getResults.getString("country");
			city = getResults.getString("city");
		}catch(NullPointerException e){
			e.printStackTrace();
		}
		
		auto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(WhereAreYou.this, LocateActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("uname", _uname);
				bundle.putString("email", _email);
				bundle.putString("age", _age);
				bundle.putString("gender", _gender);
				bundle.putString("pword", _pword);
				bundle.putString("country", country);
				bundle.putString("city", city);
				bundle.putString("avatar", _avatar);
				i.putExtras(bundle);
				startActivity(i);
			}
		});
		manually.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(WhereAreYou.this, LocateActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("uname", _uname);
				bundle.putString("email", _email);
				bundle.putString("age", _age);
				bundle.putString("gender", _gender);
				bundle.putString("pword", _pword);
				bundle.putString("country", country);
				bundle.putString("city", city);
				bundle.putString("avatar", _avatar);
				i.putExtras(bundle);
				startActivity(i);
			}
		});
	}
	public void setFontTypeText(TextView tv){
		Typeface face = Typeface.createFromAsset(getAssets(),
	            "fonts/moolbor_0.ttf");
		tv.setTypeface(face);
	}
	public void setFontTypeButton(Button btn){
		Typeface face = Typeface.createFromAsset(getAssets(),
	            "fonts/moolbor_0.ttf");
		btn.setTypeface(face);
	}
}