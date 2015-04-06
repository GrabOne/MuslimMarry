package com.muslimmarry.activities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.muslimmarry.R;
import com.facebook.android.Facebook;
import com.muslimmarry.authenticator.FacebookLogin;
import com.muslimmarry.authenticator.TwitterLogin;
import com.muslimmarry.helpers.TransparentProgressDialog;
import com.muslimmarry.helpers.helpers;
import com.muslimmarry.sharedpref.prefUser;

public class LoginOrRegisterActivity extends Activity implements OnClickListener {

	// Your Facebook APP ID
    private static String FB_APP_ID = "1408403482790942";
    // Instance of Facebook Class
    private Facebook facebook = new Facebook(FB_APP_ID);
    
    // shared pref to save location
    public static final  String LOCATE_PRE_NAME="locate_pref";
    private static SharedPreferences mLocatePref;
    
    
    String country = "";
    String city = "";
    String lat = "";
    String lng = "";
    
    prefUser user;
    
    String resultString = "";
    
    TransparentProgressDialog pd;
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login_or_register);
		
		Button btnLogin = (Button)findViewById(R.id.btnLogin);
		Button btnRegister = (Button)findViewById(R.id.btnRegister);
		ImageView fb_ic = (ImageView)findViewById(R.id.fb_ic);
		ImageView tt_ic = (ImageView)findViewById(R.id.tt_ic);
		ImageView gg_ic = (ImageView)findViewById(R.id.gg_ic);
		
		// set event for element
		btnLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		fb_ic.setOnClickListener(this);
		tt_ic.setOnClickListener(this);
		gg_ic.setOnClickListener(this);
		
		// set font for element
		new helpers(getApplicationContext()).setFontTypeButton(btnLogin);
		new helpers(getApplicationContext()).setFontTypeButton(btnRegister);
		
		// get data bundle
		try{
			Bundle bundle = getIntent().getExtras();
			country = bundle.getString("bundle");
			city = bundle.getString("city");
			lat = bundle.getString("lat");
			lng = bundle.getString("lng");
			
			// save location to shared pref
			mLocatePref = getApplicationContext().getSharedPreferences(LOCATE_PRE_NAME, 0);
			SharedPreferences.Editor value = mLocatePref.edit();
			value.putString("city", city);
			value.putString("country", country);
			value.putString("lat", lat);
			value.putString("lng", lng);
			value.commit();
		}catch(NullPointerException e){}
		
		// create user object
		user = new prefUser(LoginOrRegisterActivity.this);
		if(user.isLoggedIn()){
			Intent i = new Intent(LoginOrRegisterActivity.this, MainActivity.class);
			startActivity(i);
		}
		
		if (android.os.Build.VERSION.SDK_INT > 14) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
		
		// get key hashes
		try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	                "com.example.muslimmarry", 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	        }
	    } catch (NameNotFoundException e) {
	    	e.printStackTrace();
	    } catch (NoSuchAlgorithmException e) {
	    	e.printStackTrace();
	    }
		
		// Twitter login
		mLocatePref = getApplicationContext().getSharedPreferences(LOCATE_PRE_NAME, 0);
		new TwitterLogin(LoginOrRegisterActivity.this, mLocatePref.getString("country", ""), mLocatePref.getString("city", ""), mLocatePref.getString("lat", ""), mLocatePref.getString("lng", "")).initTwitter();
		Log.d("myTag", "Twitter: " + mLocatePref.getString("lat", "") + "  " + mLocatePref.getString("lng", ""));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i = null;
		switch (v.getId()) {
		case R.id.btnLogin:
			i = new Intent(LoginOrRegisterActivity.this, LoginActivity.class);
			startActivity(i);
			break;
		case R.id.btnRegister:
			i = new Intent(LoginOrRegisterActivity.this, RegisterActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("country", country);
			bundle.putString("city", city);
			bundle.putString("lat", String.valueOf(lat));
			bundle.putString("lng", String.valueOf("lng"));
			i.putExtras(bundle);
			startActivity(i);
			break;
		case R.id.fb_ic:
			new FacebookLogin(LoginOrRegisterActivity.this, country, city, String.valueOf(lat), String.valueOf(lng), facebook).Login();
			break;
		case R.id.tt_ic:
			new TwitterLogin(LoginOrRegisterActivity.this, mLocatePref.getString("country", ""), mLocatePref.getString("city", ""), mLocatePref.getString("lat", ""), mLocatePref.getString("lng", "")).Login();
			break;
		case R.id.gg_ic:
			
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		if(user.isLoggedIn()){
			Intent i = new Intent(LoginOrRegisterActivity.this, MainActivity.class);
			startActivity(i);
		}
	}
}
