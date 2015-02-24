package com.example.muslimmarry;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.example.muslimmarry.libraries.TransparentProgressDialog;
import com.example.muslimmarry.sharedpref.prefUser;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class Option extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
	LocationListener, OnClickListener {
	
	Button btnLogin;
	Button btnRegister;
	ImageView fb_ic;
	ImageView tt_ic;
	ImageView gg_ic;
	
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	
	// Google client to interact with Google API
	private static final int RC_SIGN_IN = 0;
    // Logcat tag
    private static final String TAG = "LOGIN";

    private boolean mSignInClicked;

    private boolean mIntentInProgress;
    private ConnectionResult mConnectionResult;
    public static final  String LOGIN_PRE_NAME="login_pref";
    static String IS_GOOGLE_LOGIN="google_is_login";
	
	// Your Facebook APP ID
    private static String FB_APP_ID = "1408403482790942";
    
    // Instance of Facebook Class
    private Facebook facebook = new Facebook(FB_APP_ID);
    private AsyncFacebookRunner mAsyncRunner;
    
    JSONObject fb_profile = null;
    String newFbAvatarUrl = "";
    
    String country = "";
    String city = "";
    double lat = 0;
    double lng = 0;
    
    // Twitter
    static String CONSUMER_KEY = "UnuOVcKU923lCnrTzUt8grwbI";
    static String CONSUMER_SECRET ="ZkWegjjOvAGih9Jvg3ZQyHGZYPRHwQ4TVlADt4eukawsWw6R9k";
    static String TWITTER_PRE_NAME="twitter_pre";
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

    static final String TWITTER_CALLBACK_URL= "oauth://t4jsample";

    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
    // Twitter
    private static Twitter twitter;
    private static RequestToken requestToken;
    
    // Shared Preferences
    private static SharedPreferences mSharedPreferences;
    
    prefUser user;
    String resultString = "";
    
    TransparentProgressDialog pd;
    
    // shared preferences to save locate
    public static final  String LOCATE_PRE_NAME="locate_pref";
    private static SharedPreferences mLocatePref;
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.option);
		btnLogin = (Button)findViewById(R.id.btnLogin);
		setFontTypeButton(btnLogin);
		btnRegister = (Button)findViewById(R.id.btnRegister);
		setFontTypeButton(btnRegister);
		fb_ic = (ImageView)findViewById(R.id.fb_ic);
		tt_ic = (ImageView)findViewById(R.id.tt_ic);
		gg_ic = (ImageView)findViewById(R.id.gg_ic);
		
		// create user object
		user = new prefUser(Option.this);
		if(user.isLoggedIn()){
			Intent i = new Intent(Option.this, MainActivity.class);
			startActivity(i);
		}
		if (android.os.Build.VERSION.SDK_INT > 14) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
		
		btnLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		
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
		// create AsyncFacebookRunner object
		mAsyncRunner = new AsyncFacebookRunner(facebook);
		
		fb_ic.setOnClickListener(this);
		tt_ic.setOnClickListener(this);
		gg_ic.setOnClickListener(this);
		
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
		mSharedPreferences = getApplicationContext().getSharedPreferences(LOGIN_PRE_NAME, 0);
		
		// Twitter
        initTwitter();
	}
	
    private void initTwitter() {
        if (!isTwitterLoggedInAlready()) {
            getTTProfileInfo();
        }

    }
    /*
     * Get twitter profile
     */
    private void getTTProfileInfo(){
        Log.d(TAG, "Get TT Info ");
        Uri uri = getIntent().getData();
        Log.d(TAG, uri==null?"uri is null":"uri: "+uri.toString());
        if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
            // oAuth verifier
            String verifier = uri.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
            try {
                // Get the access token
                AccessToken accessToken = twitter.getOAuthAccessToken(
                        requestToken, verifier);
                // Shared Preferences
                SharedPreferences.Editor e = mSharedPreferences.edit();
                // After getting access token, access token secret
                // store them in application preferences
                e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
                e.putString(PREF_KEY_OAUTH_SECRET,accessToken.getTokenSecret());
                // Store login status - true
                e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
                e.commit(); // save changes
                Log.e("Twitter OAuth Token", "" + accessToken.getToken());
                // Getting user details from twitter
                // For now i am getting his name only
                long userID = accessToken.getUserId();
                User user = twitter.showUser(userID);
                String username = new String(user.getName().getBytes("UTF-8"), "UTF-8");
                String gender = "";
                String avatar = user.getBiggerProfileImageURL();
                String language = user.getLang();
                String id = user.getId()+"";
//                Log.d("tt", username + " " + avatar + " " + language + " " + social_id);
                new TTLogin().execute(username, avatar, gender, id);
            } catch (Exception e) {
                Log.e("Twitter Login Error", e.getMessage());
            }
        }else{
            Log.e(TAG, "Login twitter error");
        }
    }
    private void loginToTwitter() {
        if (!isTwitterLoggedInAlready()) {
            Log.d(TAG, "Login TT");
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(CONSUMER_KEY);
            builder.setOAuthConsumerSecret(CONSUMER_SECRET);
            builder.setUseSSL(true);
            Configuration configuration = builder.build();

            TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();

            try {
                requestToken = twitter
                        .getOAuthRequestToken(TWITTER_CALLBACK_URL);
                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.twitter.com/oauth/authenticate?oauth_token=" +
                        requestToken.getToken())));
            } catch (TwitterException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        } else {
            // user already logged into twitter
            Toast.makeText(getApplicationContext(),
                    "Already Logged into twitter", Toast.LENGTH_LONG).show();
        }

    }
    /*
     * Twitter login
     */
    private class TTLogin extends AsyncTask<String, String, Void>{
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		pd = new TransparentProgressDialog(Option.this, R.drawable.loading_spinner);
    		pd.show();
    	}
    	@Override
    	protected Void doInBackground(String... params) {
    		// TODO Auto-generated method stub
    		InputStream inputStream = null;
    		try{
    			JSONObject jObj = new JSONObject();
				jObj.put("nickname", params[0]);
				jObj.put("email", "");
			    jObj.put("avatar", params[1]);
			    jObj.put("age", "");
			    jObj.put("gender", params[2]);
			    jObj.put("birthday", "");
			    jObj.put("twitter_id", params[3]);
			    
			    mLocatePref = getApplicationContext().getSharedPreferences(LOCATE_PRE_NAME, 0);
			    JSONObject locate = new JSONObject();
			    locate.put("country", mLocatePref.getString("country", ""));
			    locate.put("city", mLocatePref.getString("city", city));
			    JSONObject latlng = new JSONObject();
			    latlng.put("lat", mLocatePref.getString("lat", ""));
			    latlng.put("lng", mLocatePref.getString("lng", ""));
			    locate.put("coordinates", latlng);
			    
			    jObj.put("location", locate);
			    Log.d("obj", jObj.toString());
			    
			    HttpClient httpClient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://muslimmarry.campcoders.com/api/v1/login-social");
				httppost.setEntity(new ByteArrayEntity(jObj.toString().getBytes("UTF8")));
				httppost.setHeader("Accept", "application/json");
				httppost.setHeader("Content-type", "application/json;charset=UTF-8");
				httppost.setHeader("Accept-Charset", "utf-8");
				
				HttpResponse response = httpClient.execute(httppost);
				
				inputStream = response.getEntity().getContent();
				if(inputStream != null){
					resultString = convertInputStreamToString(inputStream);
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
							locate.getString("city"), coordinates.getString("lat"), coordinates.getString("lng"), data.getString("promocode"), "tt", data.getString("twitter_id"), "true");
					Intent i = new Intent(Option.this, MainActivity.class);
					startActivity(i);
					
				}else{
					Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
    	}
    }
    private boolean isTwitterLoggedInAlready() {
        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
    }
	
	private boolean isGGLogin(){
	    return mSharedPreferences.getBoolean(IS_GOOGLE_LOGIN, false);
	}
	
	public void setFontTypeButton(Button btn){
		Typeface face = Typeface.createFromAsset(getAssets(),
	            "fonts/moolbor_0.ttf");
		btn.setTypeface(face);
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		if (!result.hasResolution()) {
            Log.d(TAG, "GG ERROR CODE: "+result.getErrorCode());
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
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
    private void resolveSignInError() {
        Log.d(TAG, "Has a error. Resolving !");
        if (mConnectionResult!=null && mConnectionResult.hasResolution()) {
            Log.d(TAG, "ERROR CODE: "+mConnectionResult.getErrorCode());
            try {
                mIntentInProgress = true;
                startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(),
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

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub	
		
		Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		if(location == null){
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
		}else{
			handleNewLocation(location);
		}
		Log.d(TAG, "On Connected ");
        mSignInClicked = false;
        Log.d(TAG, "User is connected");
        getGGProfileInformation();
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
		Geocoder geocoder = new Geocoder(Option.this, Locale.getDefault());
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
	/*
	 * Login to facebook
	 */
	protected void loginToFacebook(){
		if(!facebook.isSessionValid()){
			facebook.authorize(this, new String[] { "email", "publish_stream" }, new DialogListener() {
				
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
					fb_profile = new JSONObject(json);
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
	private class FbLogin extends AsyncTask<String, String, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Option.this.runOnUiThread(new Runnable() {
			    public void run() {
					pd = new TransparentProgressDialog(Option.this, R.drawable.loading_spinner);
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
				jObj.put("nickname", new String(fb_profile.getString("name").getBytes("UTF-8"), "Utf-8"));
				jObj.put("email", new String(fb_profile.getString("email").getBytes("UTF-8"), "Utf-8"));
				
				URL url;
			    try {
			        url = new URL("https://graph.facebook.com/"+fb_profile.getString("id")+"/picture?type=large");
			        HttpURLConnection.setFollowRedirects(false); //Do _not_ follow redirects!
			        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			        newFbAvatarUrl = connection.getHeaderField("Location");
			    } catch (MalformedURLException e) {
			        // TODO Auto-generated catch block
			        e.printStackTrace();
			    } catch (IOException e) {
			        // TODO Auto-generated catch block
			        e.printStackTrace();
			    }
			    jObj.put("avatar", newFbAvatarUrl);
			    jObj.put("age", "");
			    if(fb_profile.getString("gender").equalsIgnoreCase("male")){
			    	jObj.put("gender", "men");
			    }else{
			    	jObj.put("gender", "women");
			    }
			    if(fb_profile.isNull("birthday")){
					jObj.put("birthday", "");
				}else{
					jObj.put("birthday", fb_profile.getString("birthday"));
				}
			    jObj.put("facebook_id", fb_profile.getString("id"));
			    
			    JSONObject locate = new JSONObject();
			    locate.put("country", country);
			    locate.put("city", city);
			    JSONObject latlng = new JSONObject();
			    latlng.put("lat", String.valueOf(lat));
			    latlng.put("lng", String.valueOf(lng));
			    locate.put("coordinates", latlng);
			    
			    jObj.put("location", locate);
//			    Log.d("obj", jObj.toString());
			    
			    HttpClient httpClient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://muslimmarry.campcoders.com/api/v1/login-social");
				httppost.setEntity(new ByteArrayEntity(jObj.toString().getBytes("UTF8")));
				httppost.setHeader("Accept", "application/json");
				httppost.setHeader("Content-type", "application/json;charset=UTF-8");
				httppost.setHeader("Accept-Charset", "utf-8");
				
				HttpResponse response = httpClient.execute(httppost);
				
				inputStream = response.getEntity().getContent();
				if(inputStream != null){
					resultString = convertInputStreamToString(inputStream);
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
			Option.this.runOnUiThread(new Runnable() {
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
					Intent i = new Intent(Option.this, MainActivity.class);
					startActivity(i);
					
				}else{
					Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
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
	 * Google login
	 */
	private class GGLogin extends AsyncTask<String, String, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new TransparentProgressDialog(Option.this, R.drawable.loading_spinner);
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
			    locate.put("country", country);
			    locate.put("city", city);
			    JSONObject latlng = new JSONObject();
			    latlng.put("lat", String.valueOf(lat));
			    latlng.put("lng", String.valueOf(lng));
			    locate.put("coordinates", latlng);
			    
			    jObj.put("location", locate);
//			    Log.d("obj", jObj.toString());
			    
			    HttpClient httpClient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://muslimmarry.campcoders.com/api/v1/login-social");
				httppost.setEntity(new ByteArrayEntity(jObj.toString().getBytes("UTF8")));
				httppost.setHeader("Accept", "application/json");
				httppost.setHeader("Content-type", "application/json;charset=UTF-8");
				httppost.setHeader("Accept-Charset", "utf-8");
				
				HttpResponse response = httpClient.execute(httppost);
				
				inputStream = response.getEntity().getContent();
				if(inputStream != null){
					resultString = convertInputStreamToString(inputStream);
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
	                Intent i = new Intent(Option.this, MainActivity.class);
	                startActivity(i);
				}else{
					Toast.makeText(Option.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	/*
	 * get result from server
	 */
	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
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
			Intent i = new Intent(Option.this, Login.class);
			startActivity(i);
			break;
		case R.id.btnRegister:
			Intent i2 = new Intent(Option.this, Register.class);
			Bundle bundle = new Bundle();
			bundle.putString("country", country);
			bundle.putString("city", city);
			bundle.putString("lat", String.valueOf(lat));
			bundle.putString("lng", String.valueOf("lng"));
			i2.putExtras(bundle);
			startActivity(i2);
			break;
		case R.id.fb_ic:
			loginToFacebook();
			break;
		case R.id.tt_ic:
			loginToTwitter();
			break;
		case R.id.gg_ic:
			signInWithGplus();
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
			Intent i = new Intent(Option.this, MainActivity.class);
			startActivity(i);
		}
	}
}
