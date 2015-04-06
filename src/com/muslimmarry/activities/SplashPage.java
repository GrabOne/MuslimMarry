package com.muslimmarry.activities;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.example.muslimmarry.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.muslimmarry.gps.GPSManager;

public class SplashPage extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
	LocationListener {
	
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	
	double latitude = 0;
	double longitude = 0 ;
	String city = "";
	String country = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash_page);
		
		// check gps
		checkGPS();
		
//		try{
//			String a = getIntent().getData().getQueryParameter("a");
//			Toast.makeText(getApplicationContext(), "a = " + a, Toast.LENGTH_SHORT).show();
//		}catch(NullPointerException e){}
		
		// create GoogleApiClient object
		mGoogleApiClient = new GoogleApiClient.Builder(this)
		        .addConnectionCallbacks(this)
		        .addOnConnectionFailedListener(this)
		        .addApi(LocationServices.API)
		        .build();
		
		// Create the LocationRequest object
		mLocationRequest = LocationRequest.create()
		        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
		        .setInterval(10 * 1000)        // 10 seconds, in milliseconds
		        .setFastestInterval(1 * 1000); // 1 second, in milliseconds
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		checkGPS();
	}
	
	/*
	 * Check gps
	 */
	public void checkGPS(){
		LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE );
		boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if(!statusOfGPS){
			GPSManager gps = new GPSManager(SplashPage.this);
			gps.start(true);
		}else{
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Intent i = new Intent(SplashPage.this, LoginOrRegisterActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("lat", String.valueOf(latitude));
					bundle.putString("lng", String.valueOf(longitude));
					bundle.putString("city", city);
					bundle.putString("country", country);
					i.putExtras(bundle);
					startActivity(i);
				}
			}, 2000);
		}
	}
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		handleNewLocation(arg0);
	}
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
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
	
	private void handleNewLocation(Location location) {
		// TODO Auto-generated method stub
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		Log.d("myTag", "Lat: " + latitude + ", Lng: " + longitude);
        getAddress(latitude, longitude);
	}
	
	public void getAddress(double lat, double lng) {
		Geocoder geocoder = new Geocoder(SplashPage.this, Locale.getDefault());
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

}
