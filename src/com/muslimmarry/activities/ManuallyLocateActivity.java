package com.muslimmarry.activities;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muslimmarry.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.muslimmarry.gps.GPSManager;
import com.muslimmarry.helpers.TransparentProgressDialog;
import com.muslimmarry.helpers.helpers;
import com.muslimmarry.sharedpref.prefUser;

public class ManuallyLocateActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
	GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMarkerDragListener, OnMapClickListener, OnMapLongClickListener,
	OnTouchListener, OnClickListener {
	
	private GoogleMap googleMap;
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	
	double latitude = 0;
	double longitude = 0 ;
	boolean markerClicked;
	
	ImageView back;
	TextView location_name;
	
	String _uname = "";
	String _email = "";
	String _age = "";
	String _gender = "";
	String _pword = "";
	String photo = "";
	String country = "";
	String city = "";
	
	String resultString = "";
	
	prefUser user;
	
	TransparentProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_manually_locate);
		TextView title = (TextView)findViewById(R.id.title);
		back = (ImageView)findViewById(R.id.back);
		location_name = (TextView)findViewById(R.id.location_name);
		Button btndone = (Button)findViewById(R.id.btndone);
		
		// set event for element
		back.setOnTouchListener(this);
		btndone.setOnClickListener(this);
		
		// set font for element
		new helpers(getApplicationContext()).setFontTypeText(title);
		
		// create user object
		user = new prefUser(ManuallyLocateActivity.this);
		
		// get bundle data
		try{
			Bundle getResults = getIntent().getExtras();
			_uname = getResults.getString("uname");
			_email = getResults.getString("email");
			_age = getResults.getString("age");
			_gender = getResults.getString("gender");
			_pword = getResults.getString("pword");
			photo = getResults.getString("photo");
			country = getResults.getString("country");
			city = getResults.getString("city");
		}catch(NullPointerException e){
			e.printStackTrace();
		}
		
		// check gps
		LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE );
		boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if(!statusOfGPS){
			GPSManager gps = new GPSManager(ManuallyLocateActivity.this);
			gps.start(false);
		}
		
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
		
		// Loading map
		try {
            initilizeMap();
    		googleMap.setMyLocationEnabled(true);
 
        } catch (Exception e) {
            e.printStackTrace();
        }

		googleMap.setOnMapClickListener(this);
		googleMap.setOnMapLongClickListener(this);
		googleMap.setOnMarkerDragListener(this);
	}
	
	private void initilizeMap() {
		// TODO Auto-generated method stub
		if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
 
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initilizeMap();
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
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		handleNewLocation(location);
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

	private void handleNewLocation(Location location) {
		// TODO Auto-generated method stub
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		Log.d("myTag", "Lat: " + latitude + ", Lng: " + longitude);
		MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("I'm here");
        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(latitude, longitude)).zoom(12).build();
 
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.setMyLocationEnabled(true);
        getAddress(latitude, longitude);
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void getAddress(double lat, double lng) {
		Geocoder geocoder = new Geocoder(ManuallyLocateActivity.this, Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);

	        if(addresses != null) {
	            Address returnedAddress = addresses.get(0);
	            StringBuilder strReturnedAddress = new StringBuilder("");
	            for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
	                strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(", ");
	            }
	            
	            location_name.setText(strReturnedAddress.toString());
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
	public void onMarkerDrag(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMarkerDragEnd(Marker arg0) {
		// TODO Auto-generated method stub
		LatLng dragPosition = arg0.getPosition();
        double dragLat = dragPosition.latitude;
        double dragLong = dragPosition.longitude;
        getAddress(dragLat, dragLong);
        Toast.makeText(getApplicationContext(), "Marker Dragged..!", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMapLongClick(LatLng point) {
		// TODO Auto-generated method stub
		googleMap.addMarker(new MarkerOptions()
	       .position(point)
	       .draggable(true));
	  
		markerClicked = false;
	}

	@Override
	public void onMapClick(LatLng point) {
		// TODO Auto-generated method stub
		googleMap.animateCamera(CameraUpdateFactory.newLatLng(point));
		  
		markerClicked = false;
	}
	/*
	 * Signup
	 */
	private class Signup extends AsyncTask<String, String, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new TransparentProgressDialog(ManuallyLocateActivity.this, R.drawable.loading_spinner);
			pd.show();
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			InputStream inputStream = null;
			try{
				JSONObject jObj = new JSONObject();
				jObj.put("username", _uname);
				jObj.put("nickname", "");
				jObj.put("email", _email);
				jObj.put("avatar", photo);
				jObj.put("age", _age);
				if(_gender.equalsIgnoreCase("male")){
					jObj.put("gender", "men");
				}else{
					jObj.put("gender", "women");
				}
				jObj.put("password", _pword);
				
				JSONObject locate = new JSONObject();
				if(country != null){
					locate.put("country", country);
				}else{
					locate.put("country", "");
				}
				if(city != null){
					locate.put("city", city);
				}else{
					locate.put("city", "");
				}
				
				JSONObject coordinates = new JSONObject();
				coordinates.put("lat", String.valueOf(latitude));
				coordinates.put("lng", String.valueOf(longitude));
				locate.put("coordinates", coordinates);
				jObj.put("location", locate);
				Log.d("obj", jObj.toString());
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(helpers.url+"signup");
				httppost.setEntity(new ByteArrayEntity(jObj.toString().getBytes("UTF8")));
				httppost.setHeader("Accept", "application/json");
				httppost.setHeader("Content-type", "application/json;charset=UTF-8");
				httppost.setHeader("Accept-Charset", "utf-8");
				
				HttpResponse response = httpClient.execute(httppost);
				
				inputStream = response.getEntity().getContent();
				if(inputStream != null){
					resultString = helpers.convertInputStreamToString(inputStream);
					Log.d("result", resultString);
				}
			}catch(Exception e){
				Log.e("error", e.getMessage(), e);
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pd.dismiss();
			try{
				JSONObject jObj = new JSONObject(resultString);
				if(jObj.getString("status").equalsIgnoreCase("success")){
					JSONObject data = new JSONObject(jObj.getString("data"));
					JSONObject locate = new JSONObject(data.getString("location"));
					JSONObject coordinates = new JSONObject(locate.getString("coordinates"));
					if(!locate.isNull("city")){
						city = locate.getString("city");
					}
					int mes = 0;
					if(!data.isNull("messages")){
						JSONObject message = new JSONObject(data.getString("messages"));
						mes = message.getInt("unread");
					}
					int gift = 0;
					if(!data.isNull("gifts")){
						JSONObject message = new JSONObject(data.getString("gifts"));
						gift = message.getInt("unread");
					}
					String album = "";
					if(!data.isNull("images")){
						JSONArray albumArr = data.getJSONArray("images");
						album = albumArr.toString();
					}
					user.createUserSession(data.getString("_id"), "", data.getString("username"), data.getString("email"), data.getString("age"),
							"", data.getString("gender"), data.getString("avatar"), album, data.getString("remember_token"), "", "", "", locate.getString("country"),
							city, coordinates.getString("lat"), coordinates.getString("lng"), data.getString("promocode"), "", "", "false", String.valueOf(mes), 
							String.valueOf(gift));
					Intent i = new Intent(ManuallyLocateActivity.this, MainActivity.class);
					startActivity(i);
					new helpers(ManuallyLocateActivity.this).PushActivityRight();
					
				}else{
					Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e){
				Log.e("error", e.getMessage(), e);
			}
		}
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
				new helpers(ManuallyLocateActivity.this).PushActivityRight();
			default:
				break;
			}
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.btndone){
			new Signup().execute();
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		new helpers(ManuallyLocateActivity.this).PushActivityRight();
	}
}
