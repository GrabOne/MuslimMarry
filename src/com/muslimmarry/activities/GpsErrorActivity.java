package com.muslimmarry.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muslimmarry.R;
import com.muslimmarry.helpers.helpers;

public class GpsErrorActivity extends Activity implements OnTouchListener {
	
	ImageView back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_gps_error);
		
		back = (ImageView)findViewById(R.id.back);
		TextView title = (TextView)findViewById(R.id.title);
		back.setOnTouchListener(this);
		new helpers(getApplicationContext()).setFontTypeText(title);
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
			default:
				break;
			}
		}
		return true;
	}
}
