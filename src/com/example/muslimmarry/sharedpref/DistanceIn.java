package com.example.muslimmarry.sharedpref;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DistanceIn {
	SharedPreferences pref;
	
	// Editor for Shared preferences
    Editor editor;
    
    // Context
    Context _context;
    
    // Shared pref mode
    int PRIVATE_MODE = 0;
    
    // Sharedpref file name
    private static final String PREF_NAME = "distancein_pref";
    
    // All Shared Preferences Keys
    private static final String IS_CHECK_Mi = "IsCheckMi";
    
    // variable public to access from outside
    public static final String DISTANCE_IN = "distancein";
    
    public DistanceIn(Context context){
    	this._context = context;
    	pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    	editor = pref.edit();
    }
    /*
     * Create distancein session
     */
    public void createCheckMi(String distancein){
    	editor.putBoolean(IS_CHECK_Mi, true);
    	editor.putString(DISTANCE_IN, distancein);
    	editor.commit();
    }
    
    // Get DistanceIn State
    public boolean isCheckMi(){
        return pref.getBoolean(IS_CHECK_Mi, false);
    }
    public void delCheckMi(){
    	editor.clear();
        editor.commit();
    }
}
