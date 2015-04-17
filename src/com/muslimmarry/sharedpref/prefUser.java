package com.muslimmarry.sharedpref;


import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.muslimmarry.activities.LoginOrRegisterActivity;
import com.muslimmarry.authenticator.GoogleLogin;

public class prefUser {
	SharedPreferences pref;
	
	// Editor for Shared preferences
    Editor editor;
    
    // Context
    Context _context;
    
    // Shared pref mode
    int PRIVATE_MODE = 0;
    
    // Sharedpref file name
    private static final String PREF_NAME = "user_info_pref";
    
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    
    // variable public to access from outside
    public static final String KEY_USERID = "userid";
    public static final String KEY_NAME = "name";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_AGE = "age";
    public static final String KEY_BIRTH_DAY = "birthday";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_ALBUM = "album";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_OCCUPATION = "occupation";
    public static final String KEY_HEIGHT = "height";
    public static final String KEY_LANGUAGE = "language";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_CITY = "city";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LNG = "lng";
    public static final String KEY_CODE = "code";
    public static final String KEY_LOGIN_WITH = "login_with";
    public static final String KEY_SOCIAL_ID = "social_id";
    public static final String KEY_IS_SOCIAL = "is_social";
    public static final String KEY_UNREAD = "unread";
    public static final String KEY_GIFT_UNREAD = "gift_unread";
    
    public prefUser(Context context){
    	this._context = context;
    	pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    	editor = pref.edit();
    }
    
    /*
     * Create user session
     */
    public void createUserSession(String userid, String name, String username, String email, String age, String birthday, String gender,
    	String photo, String album, String token, String occupation, String height, String language, String country, String city, String lat, String lng,
    	String code, String login_with, String social_id, String is_social, String unread, String gift_unread){
    	
    	editor.putBoolean(IS_LOGIN, true);
    	editor.putString(KEY_USERID, userid);
    	editor.putString(KEY_NAME, name);
    	editor.putString(KEY_USERNAME, username);
    	editor.putString(KEY_EMAIL, email);
    	editor.putString(KEY_AGE, age);
    	editor.putString(KEY_BIRTH_DAY, birthday);
    	editor.putString(KEY_GENDER, gender);
    	editor.putString(KEY_PHOTO, photo);
    	editor.putString(KEY_ALBUM, album);
    	editor.putString(KEY_TOKEN, token);
    	editor.putString(KEY_OCCUPATION, occupation);
    	editor.putString(KEY_HEIGHT, height);
    	editor.putString(KEY_LANGUAGE, language);
    	editor.putString(KEY_COUNTRY, country);
    	editor.putString(KEY_CITY, city);
    	editor.putString(KEY_LAT, lat);
    	editor.putString(KEY_LNG, lng);
    	editor.putString(KEY_CODE, code);
    	editor.putString(KEY_LOGIN_WITH, login_with);
    	editor.putString(KEY_SOCIAL_ID, social_id);
    	editor.putString(KEY_IS_SOCIAL, is_social);
    	editor.putString(KEY_UNREAD, unread);
    	editor.putString(KEY_GIFT_UNREAD, gift_unread);
    	editor.commit();
    }
    /*
     * Get stored session data
     */
    public HashMap<String, String> getUserDetail(){
    	HashMap<String, String> user = new HashMap<String, String>();
    	user.put(KEY_USERID, pref.getString(KEY_USERID, null));
    	user.put(KEY_NAME, pref.getString(KEY_NAME, null));
    	user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
    	user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
    	user.put(KEY_AGE, pref.getString(KEY_AGE, null));
    	user.put(KEY_BIRTH_DAY, pref.getString(KEY_BIRTH_DAY, null));
    	user.put(KEY_GENDER, pref.getString(KEY_GENDER, null));
    	user.put(KEY_PHOTO, pref.getString(KEY_PHOTO, null));
    	user.put(KEY_ALBUM, pref.getString(KEY_ALBUM, null));
    	user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));
    	user.put(KEY_OCCUPATION, pref.getString(KEY_OCCUPATION, null));
    	user.put(KEY_HEIGHT, pref.getString(KEY_HEIGHT, null));
    	user.put(KEY_LANGUAGE, pref.getString(KEY_LANGUAGE, null));
    	user.put(KEY_COUNTRY, pref.getString(KEY_COUNTRY, null));
    	user.put(KEY_CITY, pref.getString(KEY_CITY, null));
    	user.put(KEY_LAT, pref.getString(KEY_LAT, null));
    	user.put(KEY_LNG, pref.getString(KEY_LNG, null));
    	user.put(KEY_CODE, pref.getString(KEY_CODE, null));
    	user.put(KEY_LOGIN_WITH, pref.getString(KEY_LOGIN_WITH, KEY_LOGIN_WITH));
    	user.put(KEY_SOCIAL_ID, pref.getString(KEY_SOCIAL_ID, null));
    	user.put(KEY_IS_SOCIAL, pref.getString(KEY_IS_SOCIAL, null));
    	user.put(KEY_UNREAD, pref.getString(KEY_UNREAD, null));
    	user.put(KEY_GIFT_UNREAD, pref.getString(KEY_GIFT_UNREAD, null));
    	return user;
    }
    /*
     * Get Login State
     */
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
    /*
     * Check login
     */
    public void checkLogin(){
    	// check login status
    	if(!this.isLoggedIn()){
    		// user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginOrRegisterActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             
            // Staring Login Activity
            _context.startActivity(i);
    	}
    }
    
    public void LogoutUser(){
    	// delete shared pref google or twitter
        _context.getSharedPreferences(GoogleLogin.LOGIN_PRE_NAME, 0).edit().clear().commit();
        
    	// Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
         
        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginOrRegisterActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         
        // Staring Login Activity
        _context.startActivity(i);
    }
}
