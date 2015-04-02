package com.muslimmarry.activities;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.plus.Plus;
import com.muslimmarry.authenticator.FacebookLogin;
import com.muslimmarry.authenticator.GoogleLogin;
import com.muslimmarry.authenticator.TwitterLogin;
import com.muslimmarry.helpers.TransparentProgressDialog;
import com.muslimmarry.helpers.helpers;
import com.muslimmarry.sharedpref.prefUser;

public class LoginOrRegisterActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
	LocationListener, OnClickListener {
	
	Button btnLogin;
	Button btnRegister;
	ImageView fb_ic;
	ImageView tt_ic;
	ImageView gg_ic;
	
	// google api client object
	private GoogleApiClient mGoogleApiClient;
	// location request object
	private LocationRequest mLocationRequest;

	// Your Facebook APP ID
    private static String FB_APP_ID = "1408403482790942";
    // Instance of Facebook Class
    private Facebook facebook = new Facebook(FB_APP_ID);
    
    // shared pref to save location
    public static final  String LOCATE_PRE_NAME="locate_pref";
    private static SharedPreferences mLocatePref;
    
    
    prefUser user;
    String country = "";
    String city = "";
    double lat = 0;
    double lng = 0;
    String resultString = "";
    
    TransparentProgressDialog pd;
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login_or_register);
		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnRegister = (Button)findViewById(R.id.btnRegister);
		new helpers(getApplicationContext()).setFontTypeButton(btnLogin);
		new helpers(getApplicationContext()).setFontTypeButton(btnRegister);
		fb_ic = (ImageView)findViewById(R.id.fb_ic);
		tt_ic = (ImageView)findViewById(R.id.tt_ic);
		gg_ic = (ImageView)findViewById(R.id.gg_ic);
		
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
		
		btnLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		fb_ic.setOnClickListener(this);
		tt_ic.setOnClickListener(this);
		gg_ic.setOnClickListener(this);
		
		// create GoogleApiClient object
		mGoogleApiClient = new GoogleApiClient.Builder(this)
	        .addConnectionCallbacks(this)
	        .addOnConnectionFailedListener(this)
	        .addApi(Plus.API)
	        .addApi(LocationServices.API)
	        .addScope(Plus.SCOPE_PLUS_LOGIN)
	        .build();

		// Create the LocationRequest object
		mLocationRequest = LocationRequest.create()
	        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
	        .setInterval(10 * 1000)        // 10 seconds, in milliseconds
	        .setFastestInterval(1 * 1000); // 1 second, in milliseconds
		
		// Twitter
		mLocatePref = getApplicationContext().getSharedPreferences(LOCATE_PRE_NAME, 0);
		new TwitterLogin(LoginOrRegisterActivity.this, mLocatePref.getString("country", ""), mLocatePref.getString("city", ""), mLocatePref.getString("lat", ""), mLocatePref.getString("lng", "")).initTwitter();
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub	
		
		Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		if(location == null){
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
		}else{
			handleNewLocation(location);
		}
	}
	
	private void handleNewLocation(Location location) {
		// TODO Auto-generated method stub
		lat = location.getLatitude();
		lng = location.getLongitude();
		Log.d("myTag", lat + " - " + lng);
		getAddress(lat, lng);
		mLocatePref = getApplicationContext().getSharedPreferences(LOCATE_PRE_NAME, 0);
		SharedPreferences.Editor loc = mLocatePref.edit();
		loc.putString("city", city);
		loc.putString("country", country);
		loc.putString("lat", String.valueOf(lat));
		loc.putString("lng", String.valueOf(lng));
		loc.commit();
	}
	
	public void getAddress(double lat, double lng) {
		Geocoder geocoder = new Geocoder(LoginOrRegisterActivity.this, Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);

	        if(addresses != null) {
	        	if(addresses.get(0).getLocality() != null){
	        		city = addresses.get(0).getLocality();
	        	}
	            if(addresses.get(0).getCountryName() != null){
	            	country = addresses.get(0).getCountryName();
	            }
	            Log.d("myTag", city + " - " + country);
	        }
	        else{
	            Toast.makeText(getApplicationContext(), "No Address returned!", Toast.LENGTH_SHORT).show();
	        }
	        
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
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
		switch (v.getId()) {
		case R.id.btnLogin:
			Intent i = new Intent(LoginOrRegisterActivity.this, LoginActivity.class);
			startActivity(i);
			break;
		case R.id.btnRegister:
			Intent i2 = new Intent(LoginOrRegisterActivity.this, RegisterActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("country", country);
			bundle.putString("city", city);
			bundle.putString("lat", String.valueOf(lat));
			bundle.putString("lng", String.valueOf("lng"));
			i2.putExtras(bundle);
			startActivity(i2);
			break;
		case R.id.fb_ic:
			new FacebookLogin(LoginOrRegisterActivity.this, country, city, String.valueOf(lat), String.valueOf(lng), facebook).Login();
			break;
		case R.id.tt_ic:
			new TwitterLogin(LoginOrRegisterActivity.this, mLocatePref.getString("country", ""), mLocatePref.getString("city", ""), mLocatePref.getString("lat", ""), mLocatePref.getString("lng", "")).Login();
			break;
		case R.id.gg_ic:
			new GoogleLogin(LoginOrRegisterActivity.this, country, city, String.valueOf(lat), String.valueOf(lng), mGoogleApiClient).Login();
			break;
		default:
			break;
		}
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mGoogleApiClient.connect();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mGoogleApiClient.connect();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(mGoogleApiClient.isConnected()){
			LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
			mGoogleApiClient.disconnect();
		}
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
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
