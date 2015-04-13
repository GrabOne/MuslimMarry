package com.muslimmarry.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.example.muslimmarry.R;
import com.muslimmarry.sharedpref.prefUser;

public class SplashPage extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash_page);
		
		// create user object
		prefUser user = new prefUser(SplashPage.this);
				
		// check if user logged redirect to MainActivity
		if(user.isLoggedIn()){
			openActivity(MainActivity.class);
		}else{
			openActivity(LoginOrRegisterActivity.class);
		}
	}
	public void openActivity(final Class<? extends Activity> activity){
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent i = new Intent(SplashPage.this, activity);
				startActivity(i);
			}
		}, 2000);
	}
}
