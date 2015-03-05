package com.muslimmarry.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

import com.example.muslimmarry.R;

public class SplashPage extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash_page);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent i = new Intent(SplashPage.this, LoginOrRegisterActivity.class);
				startActivity(i);
			}
		}, 2000);
		try{
			String a = getIntent().getData().getQueryParameter("a");
			Toast.makeText(getApplicationContext(), "a = " + a, Toast.LENGTH_SHORT).show();
		}catch(NullPointerException e){}
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Intent i = new Intent(SplashPage.this, LoginOrRegisterActivity.class);
		startActivity(i);
	}
}
