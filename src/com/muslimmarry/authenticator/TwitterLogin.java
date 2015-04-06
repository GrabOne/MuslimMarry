package com.muslimmarry.authenticator;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.muslimmarry.R;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.helpers.TransparentProgressDialog;
import com.muslimmarry.helpers.helpers;
import com.muslimmarry.sharedpref.prefUser;

public class TwitterLogin {
	
	Context mContext;
	
	// Logcat tag
    private static final String TAG = "LOGIN";
    
    // Twitter
    static String CONSUMER_KEY = "UnuOVcKU923lCnrTzUt8grwbI";
    static String CONSUMER_SECRET ="ZkWegjjOvAGih9Jvg3ZQyHGZYPRHwQ4TVlADt4eukawsWw6R9k";
    static String PREFERENCE_NAME = "twitter_oauth";

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
    static final String LOGIN_PRE_NAME="login_pref";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
    
    prefUser user;
    String resultString = "";
    String _country;
    String _city;
    String _lat;
    String _lng;
    
    TransparentProgressDialog pd;
	
	public TwitterLogin(Context ctx, String country, String city, String lat, String lng){
		this.mContext = ctx;
		this._country = country;
		this._city = city;
		this._lat = lat;
		this._lng = lng;
	}
	public void Login(){
		mSharedPreferences = mContext.getSharedPreferences(LOGIN_PRE_NAME, 0);
		user = new prefUser(mContext);
		loginToTwitter();
	}
	public void initTwitter() {
        if (!isTwitterLoggedInAlready()) {
            getTTProfileInfo();
        }

    }
    /*
     * Get twitter profile
     */
    private void getTTProfileInfo(){
        Log.d(TAG, "Get TT Info ");
        Uri uri = ((Activity)mContext).getIntent().getData();
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
                String id = user.getId()+"";
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
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.twitter.com/oauth/authenticate?oauth_token=" +
                        requestToken.getToken())));
            } catch (TwitterException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        } else {
            // user already logged into twitter
            Toast.makeText(mContext,
                    "Already Logged into twitter", Toast.LENGTH_LONG).show();
        }

    }
    /*
     * Send twitter user information to server
     */
    private class TTLogin extends AsyncTask<String, String, Void>{
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
				jObj.put("email", "");
			    jObj.put("avatar", params[1]);
			    jObj.put("age", "");
			    jObj.put("gender", params[2]);
			    jObj.put("birthday", "");
			    jObj.put("twitter_id", params[3]);
			    
			    JSONObject locate = new JSONObject();
			    locate.put("country", _country);
			    locate.put("city", _city);
			    JSONObject latlng = new JSONObject();
			    latlng.put("lat", _lat);
			    latlng.put("lng", _lng);
			    locate.put("coordinates", latlng);
			    
			    jObj.put("location", locate);
			    Log.d("obj", jObj.toString());
			    
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
					user = new prefUser(mContext);
					user.createUserSession(data.getString("_id"), new String(data.getString("nickname").getBytes("UTF-8"), "UTF-8"), "", new String(data.getString("email").getBytes("UTF-8"), "UTF-8"), data.getString("age"),
							data.getString("birthday"), data.getString("gender"), data.getString("avatar"), data.getString("remember_token"), data.getString("occupation"), data.getString("height"), languageArr.toString(), locate.getString("country"),
							locate.getString("city"), coordinates.getString("lat"), coordinates.getString("lng"), data.getString("promocode"), "tt", data.getString("twitter_id"), "true");
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
    private boolean isTwitterLoggedInAlready() {
    	mSharedPreferences = mContext.getSharedPreferences(LOGIN_PRE_NAME, 0);
        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
    }
}
