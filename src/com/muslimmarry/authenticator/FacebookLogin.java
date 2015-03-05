package com.muslimmarry.authenticator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.muslimmarry.R;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.helpers.TransparentProgressDialog;
import com.muslimmarry.helpers.helpers;
import com.muslimmarry.sharedpref.prefUser;

public class FacebookLogin {
	
	Context mContext;
    
    // Instance of Facebook Class
	Facebook _facebook;
    AsyncFacebookRunner mAsyncRunner;
    
    prefUser user;
    JSONObject profile = null;
    String fbAvatar = "";
    String resultString = "";
    String _country;
    String _city;
    String _lat;
    String _lng;
    
    TransparentProgressDialog pd;
    
    public FacebookLogin(Context ctx, String country, String city, String lat, String lng, Facebook facebook){
    	this.mContext = ctx;
    	this._country = country;
    	this._city = city;
    	this._lat = lat;
    	this._lng = lng;
    	this._facebook = facebook;
    }
	
	public void Login(){
		user = new prefUser(mContext);
		// create AsyncFacebookRunner object
		mAsyncRunner = new AsyncFacebookRunner(_facebook);
		loginToFacebook();
	}
	/*
	 * Login to facebook
	 */
	public void loginToFacebook(){
		if(!_facebook.isSessionValid()){
			_facebook.authorize((Activity)mContext, new String[] { "email", "publish_stream" }, new DialogListener() {
				
				@Override
				public void onFacebookError(FacebookError e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onError(DialogError e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onComplete(Bundle values) {
					// TODO Auto-generated method stub
					getFbProfileInformation();
				}
				
				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}
	/*
	 * Get Profile information by making request to Facebook Graph API
	 */
	public void getFbProfileInformation(){
		mAsyncRunner.request("me", new RequestListener() {
			
			@Override
			public void onMalformedURLException(MalformedURLException e, Object state) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onIOException(IOException e, Object state) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFileNotFoundException(FileNotFoundException e, Object state) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFacebookError(FacebookError e, Object state) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(String response, Object state) {
				// TODO Auto-generated method stub
				//Log.d("response", response);
				String json = response;
				try{
					profile = new JSONObject(json);
					new FbLogin().execute();
				}catch(JSONException e){
					e.printStackTrace();
				}
			}
		});
	}
	/*
	 * Facebook login
	 */
	public class FbLogin extends AsyncTask<String, String, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			((Activity)mContext).runOnUiThread(new Runnable() {
			    public void run() {
					pd = new TransparentProgressDialog(mContext, R.drawable.loading_spinner);
					pd.show();
			    }
			});
		}
		@Override
		protected Void doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			InputStream inputStream = null;
			try{
				JSONObject jObj = new JSONObject();
				jObj.put("nickname", new String(profile.getString("name").getBytes("UTF-8"), "Utf-8"));
				jObj.put("email", new String(profile.getString("email").getBytes("UTF-8"), "Utf-8"));
				
				URL url;
			    try {
			        url = new URL("https://graph.facebook.com/"+profile.getString("id")+"/picture?type=large");
			        HttpURLConnection.setFollowRedirects(false); //Do _not_ follow redirects!
			        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			        fbAvatar = connection.getHeaderField("Location");
			    } catch (MalformedURLException e) {
			        // TODO Auto-generated catch block
			        e.printStackTrace();
			    } catch (IOException e) {
			        // TODO Auto-generated catch block
			        e.printStackTrace();
			    }
			    jObj.put("avatar", fbAvatar);
			    jObj.put("age", "");
			    if(profile.getString("gender").equalsIgnoreCase("male")){
			    	jObj.put("gender", "men");
			    }else{
			    	jObj.put("gender", "women");
			    }
			    if(profile.isNull("birthday")){
					jObj.put("birthday", "");
				}else{
					jObj.put("birthday", profile.getString("birthday"));
				}
			    jObj.put("facebook_id", profile.getString("id"));
			    
			    JSONObject locate = new JSONObject();
			    locate.put("country", _country);
			    locate.put("city", _city);
			    JSONObject latlng = new JSONObject();
			    latlng.put("lat", String.valueOf(_lat));
			    latlng.put("lng", String.valueOf(_lng));
			    locate.put("coordinates", latlng);
			    
			    jObj.put("location", locate);
//			    Log.d("obj", jObj.toString());
			    
			    HttpClient httpClient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://104.131.76.17/muslim_marry/public/api/v1/login-social");
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
			((Activity)mContext).runOnUiThread(new Runnable() {
			    public void run() {
					pd.dismiss();
			    }
			});
			try{
				JSONObject jObj = new JSONObject(resultString);
				if(jObj.getString("status").equalsIgnoreCase("success")){
					JSONObject data = new JSONObject(jObj.getString("data"));
					JSONArray languageArr = data.getJSONArray("language");
					JSONObject locate = new JSONObject(data.getString("location"));
					JSONObject coordinates = new JSONObject(locate.getString("coordinates"));
					
					user.createUserSession(data.getString("_id"), new String(data.getString("nickname").getBytes("UTF-8"), "UTF-8"), "", new String(data.getString("email").getBytes("UTF-8"), "UTF-8"), data.getString("age"),
							data.getString("birthday"), data.getString("gender"), data.getString("avatar"), data.getString("remember_token"), data.getString("occupation"), data.getString("height"), languageArr.toString(), locate.getString("country"),
							locate.getString("city"), coordinates.getString("lat"), coordinates.getString("lng"), data.getString("promocode"), "fb", data.getString("facebook_id"), "true");
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
}
