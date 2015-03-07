package com.muslimmarry.authenticator;

import java.io.InputStream;

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
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.muslimmarry.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.helpers.TransparentProgressDialog;
import com.muslimmarry.helpers.helpers;
import com.muslimmarry.sharedpref.prefUser;

public class GoogleLogin implements ConnectionCallbacks, OnConnectionFailedListener {
	Context mContext;
	
	GoogleApiClient mGoogleApiClient;
	
	// Google client to interact with Google API
	private static final int RC_SIGN_IN = 0;
	
    // Logcat tag
    private static final String TAG = "Login";

    private boolean mSignInClicked;
    private boolean mIntentInProgress;
    private ConnectionResult mConnectionResult;
    
    // Shared Preferences
    private static SharedPreferences mSharedPreferences;
    public static final  String LOGIN_PRE_NAME="login_pref";
    
    static String IS_GOOGLE_LOGIN="google_is_login";
    
    prefUser user;
    String resultString = "";
    String _country;
    String _city;
    String _lat;
    String _lng;
    
    TransparentProgressDialog pd;
	
	public GoogleLogin(Context ctx, String country, String city, String lat, String lng, GoogleApiClient mGoogleApiClient){
		this.mContext = ctx;
		this._country = country;
		this._city = city;
		this._lat = lat;
		this._lng = lng;
		this.mGoogleApiClient = mGoogleApiClient;
	}
	
	public void Login(){
		mSharedPreferences = mContext.getSharedPreferences(LOGIN_PRE_NAME, 0);
		user = new prefUser(mContext);
		signInWithGplus();
	}
	
	private boolean isGGLogin(){
	    return mSharedPreferences.getBoolean(IS_GOOGLE_LOGIN, false);
	}
	
	private void resolveSignInError() {
        Log.d(TAG, "Has a error. Resolving !");
        if (mConnectionResult!=null && mConnectionResult.hasResolution()) {
            Log.d(TAG, "ERROR CODE: "+mConnectionResult.getErrorCode());
            try {
                mIntentInProgress = true;
                ((Activity)mContext).startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }else{
            Log.d(TAG, "mConnectionResult is null:"+(mConnectionResult==null));
            Log.d(TAG, "No errors");
        }
    }
	
	/**
	 * Sign-in into google
	 * */
	private void signInWithGplus() {
        mSignInClicked = true;
        Log.d(TAG, "Login GG");
        if(mGoogleApiClient.isConnected()){
            getGGProfileInformation();
            return;
        }
        if (!mGoogleApiClient.isConnecting()) {
            Log.d(TAG, "Not connecting");

            Log.d(TAG, "REsolve in sign");
            resolveSignInError();
        }else{
            Log.d(TAG, "GG Connected !");
        }
	}
	
	/**
	 * Fetching user's information name, email, profile picture
	 * */
	private void getGGProfileInformation() {
        Log.d(TAG, "GET GG Info");
        Log.d(TAG, "isGGLogin(): "+isGGLogin());
        Log.d(TAG, "mSignInClicked: "+mSignInClicked);
        if(!isGGLogin() && !mSignInClicked){
            return;
        }
	    try {
	        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
	            Person currentPerson = Plus.PeopleApi
	                    .getCurrentPerson(mGoogleApiClient);
	            String personGooglePlusProfile = currentPerson.getUrl();

	            String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                String id = currentPerson.getId();
                String name = new String(currentPerson.getDisplayName().getBytes("UTF-8"), "UTF-8");
                String birthday = "";
                if(currentPerson.getBirthday() != null){
                	birthday = currentPerson.getBirthday();
                }
                String gender=currentPerson.getGender()==1 ? "men":"women";
                String avatar=currentPerson.getImage().getUrl();
	            Log.d("google user info", "Name: " + name + ", plusProfile: "
	                    + personGooglePlusProfile + ", email: " + email
	                    + ", Image: " + avatar+ ", birthday: " + birthday);

	            new GGLogin().execute(name, email, avatar, gender, birthday, id);
	        } else {
                Log.e(TAG, "Person information is null");

	        }
	    } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
	        e.printStackTrace();
	    }
	}
	
	/*
	 * Send google user information to server
	 */
	private class GGLogin extends AsyncTask<String, String, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new TransparentProgressDialog(mContext, R.drawable.loading_spinner);
			pd.show();
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			InputStream inputStream = null;
			try{
				JSONObject jObj = new JSONObject();
				jObj.put("nickname", params[0]);
				jObj.put("email", params[1]);
			    jObj.put("avatar", params[2]);
			    jObj.put("age", "");
			    jObj.put("gender", params[3]);
			    jObj.put("birthday", params[4]);
			    jObj.put("google_id", params[5]);
			    
			    JSONObject locate = new JSONObject();
			    locate.put("country", _country);
			    locate.put("city", _city);
			    JSONObject latlng = new JSONObject();
			    latlng.put("lat", String.valueOf(_lat));
			    latlng.put("lng", String.valueOf(_lng));
			    locate.put("coordinates", latlng);
			    
			    jObj.put("location", locate);
			    
			    HttpClient httpClient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(helpers.url+"api/v1/login-social");
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
				e.printStackTrace();
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
					JSONArray languageArr = data.getJSONArray("language");
					JSONObject locate = new JSONObject(data.getString("location"));
					JSONObject coordinates = new JSONObject(locate.getString("coordinates"));
					
					user.createUserSession(data.getString("_id"), new String(data.getString("nickname").getBytes("UTF-8"), "UTF-8"), "", new String(data.getString("email").getBytes("UTF-8"), "UTF-8"), data.getString("age"),
							data.getString("birthday"), data.getString("gender"), data.getString("avatar"), data.getString("remember_token"), data.getString("occupation"), data.getString("height"), languageArr.toString(), locate.getString("country"),
							locate.getString("city"), coordinates.getString("lat"), coordinates.getString("lng"), data.getString("promocode"), "gg", data.getString("google_id"), "true");
					
					SharedPreferences.Editor editor =mSharedPreferences.edit();
	                editor.putBoolean(IS_GOOGLE_LOGIN, true);
	                Intent i = new Intent(mContext, MainActivity.class);
	                mContext.startActivity(i);
				}else{
					Toast.makeText(mContext, jObj.getString("message"), Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		if (!result.hasResolution()) {
            Log.d(TAG, "GG ERROR CODE: "+result.getErrorCode());
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), (Activity)mContext,
                    0).show();
			return;
		}
        if (!mIntentInProgress) {
            mConnectionResult = result;
            if (mSignInClicked) {
                Log.d(TAG, "REsolve in connect fail");
                resolveSignInError();
            }
        }
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		Log.d(TAG, "On Connected ");
        mSignInClicked = false;
        Log.d(TAG, "User is connected");
		getGGProfileInformation();
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
	}
}
