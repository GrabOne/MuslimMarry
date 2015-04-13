package com.muslimmarry.activities;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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
import com.muslimmarry.gps.GPSManager;
import com.muslimmarry.helpers.helpers;
import com.muslimmarry.sharedpref.prefUser;

public class LoginOrRegisterActivity extends Activity implements OnClickListener, GoogleApiClient.ConnectionCallbacks,
	GoogleApiClient.OnConnectionFailedListener, LocationListener {
	
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;

	// Your Facebook APP ID
    private static String FB_APP_ID = "1408403482790942";
    // Instance of Facebook Class
    private Facebook facebook = new Facebook(FB_APP_ID);
    
    // shared pref to save location
    public static final  String LOCATE_PRE_NAME="locate_pref";
    
    // Shared Preferences
    private static SharedPreferences mSharedPreferences;
    public static final  String LOGIN_PRE_NAME="login_pref";
    
    static String IS_GOOGLE_LOGIN="google_is_login";
    
    String country = "";
    String city = "";
    double lat = 0;
    double lng = 0;
    
    prefUser user;
    
    String resultString = "";
   
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
		
		// create user object
		user = new prefUser(LoginOrRegisterActivity.this);
		
		if (android.os.Build.VERSION.SDK_INT > 14) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
		
		// check gps
		LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE );
		boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if(!statusOfGPS){
			GPSManager gps = new GPSManager(LoginOrRegisterActivity.this);
			gps.start(true);
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
		
		// create GoogleApiClient object
		mGoogleApiClient = new GoogleApiClient.Builder(this)
		        .addConnectionCallbacks(this)
		        .addOnConnectionFailedListener(this)
		        .addApi(Plus.API, Plus.PlusOptions.builder().build())
		        .addApi(LocationServices.API)
		        .addScope(Plus.SCOPE_PLUS_LOGIN)
		        .build();
		
		// Create the LocationRequest object
		mLocationRequest = LocationRequest.create()
		        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
		        .setInterval(10 * 1000)        // 10 seconds, in milliseconds
		        .setFastestInterval(1 * 1000); // 1 second, in milliseconds
		
		// Twitter login
		mSharedPreferences = getApplicationContext().getSharedPreferences(LOCATE_PRE_NAME, 0);
		new TwitterLogin(LoginOrRegisterActivity.this, mSharedPreferences.getString("country", ""), mSharedPreferences.getString("city", ""), mSharedPreferences.getString("lat", ""), mSharedPreferences.getString("lng", "")).initTwitter();
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
			new helpers(LoginOrRegisterActivity.this).PushActivityLeft();
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
			new helpers(LoginOrRegisterActivity.this).PushActivityLeft();
			break;
		case R.id.fb_ic:
			new FacebookLogin(LoginOrRegisterActivity.this, country, city, String.valueOf(lat), String.valueOf(lng), facebook).Login();
			break;
		case R.id.tt_ic:
			new TwitterLogin(LoginOrRegisterActivity.this, mSharedPreferences.getString("country", ""), mSharedPreferences.getString("city", ""), mSharedPreferences.getString("lat", ""), mSharedPreferences.getString("lng", "")).Login();
			break;
		case R.id.gg_ic:
			new GoogleLogin(LoginOrRegisterActivity.this, country, city, String.valueOf(lat), String.valueOf(lng), mGoogleApiClient).Login();
			break;
		default:
			break;
		}
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		handleNewLocation(arg0);
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

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
	}
	private void handleNewLocation(Location location) {
		// TODO Auto-generated method stub
		lat = location.getLatitude();
		lng = location.getLongitude();
		Log.d("myTag", "Lat: " + lat + ", Lng: " + lng);
        getAddress(lat, lng);
        mSharedPreferences = getApplicationContext().getSharedPreferences(LOCATE_PRE_NAME, 0);
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString("city", city);
		editor.putString("country", country);
		editor.putString("lat", String.valueOf(lat));
		editor.putString("lng", String.valueOf(lng));
		editor.commit();
	}
	
	public void getAddress(double lat, double lng) {
		Geocoder geocoder = new Geocoder(LoginOrRegisterActivity.this, Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);

	        if(addresses != null) {
	            Address returnedAddress = addresses.get(0);
	            StringBuilder strReturnedAddress = new StringBuilder("");
	            for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
	                strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(", ");
	            }
	            
	            city = addresses.get(0).getLocality();
	            country = addresses.get(0).getCountryName();
	            Log.d("myTag", "City: " + city + ", Country: " + country);
	        }
	        else{
	            Toast.makeText(getApplicationContext(), "No Address returned!", Toast.LENGTH_SHORT).show();
	        }
	        
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    Log.e("error", e.getMessage(), e);
		}
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
}
