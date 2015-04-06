package com.muslimmary.fragments;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.gps.GPSManager;
import com.muslimmarry.helpers.helpers;

public class SendLocationFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
	GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMarkerDragListener, OnMapClickListener, OnMapLongClickListener,
	OnTouchListener, OnClickListener {
	
	ImageView back;
	
	private GoogleMap googleMap;
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	
	double latitude = 0;
	double longitude = 0 ;
	boolean markerClicked;
	
	String location_name = "";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_send_location, container, false);
		helpers.setTouch(rootView);
		
		back = (ImageView)rootView.findViewById(R.id.back);
		Button send = (Button)rootView.findViewById(R.id.send);
		
		// set event for element
		back.setOnTouchListener(this);
		send.setOnClickListener(this);
		
		// set background for bottom nav element
		((MainActivity)getActivity()).setBgGroupOriginal();
		
		// check gps
		LocationManager manager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE );
		boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if(!statusOfGPS){
			GPSManager gps = new GPSManager(getActivity());
			gps.start(false);
		}
		
		// create GoogleApiClient object
		mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();

		// Create the LocationRequest object
		mLocationRequest = LocationRequest.create()
		        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
		        .setInterval(10 * 1000)        // 10 seconds, in milliseconds
		        .setFastestInterval(1 * 1000); // 1 second, in milliseconds
		
		try {
		    // Loading map
		    initilizeMap();
			googleMap.setMyLocationEnabled(true);
		
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		googleMap.setOnMapClickListener(this);
		googleMap.setOnMapLongClickListener(this);
		googleMap.setOnMarkerDragListener(this);
		
		return rootView;
	}
	
	private void initilizeMap() {
		// TODO Auto-generated method stub
		if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
 
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getActivity(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
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
        Toast.makeText(getActivity(), "Marker Dragged..!", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		// TODO Auto-generated method stub
		
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

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
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
	public void getAddress(double lat, double lng) {
		Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);

	        if(addresses != null) {
	            Address returnedAddress = addresses.get(0);
	            StringBuilder strReturnedAddress = new StringBuilder("");
	            for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
	                strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(", ");
	            }
	            
	            location_name = strReturnedAddress.toString();
	        }
	        else{
	            Toast.makeText(getActivity(), "No Address returned!", Toast.LENGTH_SHORT).show();
	        }
	        
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initilizeMap();
		mGoogleApiClient.connect();
		((MainActivity)getActivity()).showTopNav(false);
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(mGoogleApiClient.isConnected()){
			LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent arg1) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.back){
			switch (arg1.getAction()) {
			case MotionEvent.ACTION_DOWN:
				back.setBackgroundColor(Color.parseColor("#2e9dbc"));
				break;
			case MotionEvent.ACTION_UP:
				back.setBackgroundColor(Color.TRANSPARENT);
				getFragmentManager().popBackStack();
			default:
				break;
			}
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.send:
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, location_name);
			startActivity(Intent.createChooser(intent, "Share with..."));
			break;

		default:
			break;
		}
	}
}
