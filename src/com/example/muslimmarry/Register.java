package com.example.muslimmarry;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.muslimmarry.libraries.TransparentProgressDialog;

public class Register extends Activity {
	
	Button btnNext;
	ImageView back;
	EditText uname;
	EditText mail;
	EditText age;
	EditText gender;
	//EditText dateofbirth;
	EditText pword;
	EditText cfpword;
	
	int year;
    int month;
    int day;
    static final int DATE_PICKER_ID = 1111;
    
    String country = "";
    String city = "";
    String lat = "";
    String lng = "";
    
    String resultString = "";
    
    TransparentProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		btnNext = (Button)findViewById(R.id.btnNext);
		back = (ImageView)findViewById(R.id.back);
		uname = (EditText)findViewById(R.id.uname);
		mail = (EditText)findViewById(R.id.mail);
		age = (EditText)findViewById(R.id.age);
		gender = (EditText)findViewById(R.id.gender);
		//dateofbirth = (EditText)findViewById(R.id.dateofbirth);
		pword = (EditText)findViewById(R.id.pword);
		cfpword = (EditText)findViewById(R.id.cfpword);
		setFontTypeButton(btnNext);
//		dateofbirth.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				showDialog(DATE_PICKER_ID);
//			}
//		});
		
		// get bundle data
		try{
			Bundle getResults = getIntent().getExtras();
			country = getResults.getString("country");
			city = getResults.getString("city");
			lat = getResults.getString("lat");
			lng = getResults.getString("lng");
		}catch(NullPointerException e){
			e.printStackTrace();
		}
		
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isValidUsername(uname.getText().toString())){
					Toast.makeText(getApplicationContext(), "Username is invalid", Toast.LENGTH_SHORT).show();
				}else if(uname.getText().toString().length() < 4 || uname.getText().toString().length() > 40){
					Toast.makeText(getApplicationContext(), "Username: min 4 | max 40 characters!", Toast.LENGTH_SHORT).show();
				}else if(!isValidEmail(mail.getText().toString())){
					Toast.makeText(getApplicationContext(), "Email is invalid!", Toast.LENGTH_SHORT).show();
				}else if(mail.getText().toString().length() > 40){
					Toast.makeText(getApplicationContext(), "Email: max 40 characters!", Toast.LENGTH_SHORT).show();
				}else if(age.getText().toString().length() <= 0){
					Toast.makeText(getApplicationContext(), "Please enter age!", Toast.LENGTH_SHORT).show();
				}else if(age.getText().toString().length() > 2){
					Toast.makeText(getApplicationContext(), "Age: max 2 numbers!", Toast.LENGTH_SHORT).show();
				}else if(gender.getText().toString().equalsIgnoreCase("gender")){
					Toast.makeText(getApplicationContext(), "Please select gender!", Toast.LENGTH_SHORT).show();
				}else if(pword.getText().toString().length() < 6 || pword.getText().toString().length() > 40){
					Toast.makeText(getApplicationContext(), "Password: min 6 | max 40 characters!", Toast.LENGTH_SHORT).show();
				}else if(!pword.getText().toString().equalsIgnoreCase(cfpword.getText().toString())){
					Toast.makeText(getApplicationContext(), "Password not match!", Toast.LENGTH_SHORT).show();
				}else{
					new CheckUsernameEmail().execute();
				}
			}
		});
		
		back.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				switch (arg1.getAction()) {
				case MotionEvent.ACTION_DOWN:
					back.setBackgroundColor(Color.parseColor("#2e9dbc"));
					break;
				case MotionEvent.ACTION_UP:
					back.setBackgroundColor(Color.TRANSPARENT);
					finish();
				default:
					break;
				}
				return true;
			}
		});
		
		gender.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				selectGender();
			}
		});
	}
	/*
	 * Select gender
	 */
	private void selectGender() {
		final CharSequence[] items = { "Male", "Female" };

		AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
		builder.setTitle("Your Gender?");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Male")) {
					gender.setText("male");
				}else if (items[item].equals("Female")) {
					gender.setText("female");
				}
			}
		});
		builder.show();
	}
	
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
        case DATE_PICKER_ID:
             
            // open datepicker dialog. 
            // set date picker for current date 
            // add pickerListener listner to date picker
            return new DatePickerDialog(this, pickerListener, year, month,day);
        }
		return null;
	}
	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker arg0, int selectedYear, int selectedMonth, int selectedDay) {
			// TODO Auto-generated method stub
			year = selectedYear;
			month = selectedMonth + 1;
			day = selectedDay;
			String _month;
			String _day;
			if(month < 10){
				_month = "0" + month;
			}else{
				_month = "" + month;
			}
			if(day < 10){
				_day = "0" + day;
			}else{
				_day = "" + day;
			}
			
//			dateofbirth.setText(year + "-" + _month + "-" + _day);
		}
	};
	
	public void setFontTypeButton(Button btn){
		Typeface face = Typeface.createFromAsset(getAssets(),
	            "fonts/moolbor_0.ttf");
		btn.setTypeface(face);
	}
	/*
	 * validating email
	 */
	private boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9\\+]+(\\.[_A-Za-z0-9]+)*@"
				+ "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	/*
	 * validating username
	 */
	private boolean isValidUsername(String uname){
		String UNAME_PATTERN = "^[a-zA-Z0-9-_]+$";
		Pattern pattern = Pattern.compile(UNAME_PATTERN);
		Matcher matcher = pattern.matcher(uname);
		return matcher.matches();
	}
	/*
	 * check username and email
	 */
	private class CheckUsernameEmail extends AsyncTask<String, String, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new TransparentProgressDialog(Register.this, R.drawable.loading_spinner);
			pd.show();
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			InputStream inputStream = null;
			try{
				JSONObject jObj = new JSONObject();
				jObj.put("email", mail.getText().toString());
				jObj.put("username", uname.getText().toString());
				Log.d("myTag", jObj.toString());
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://muslimmarry.campcoders.com/api/v1/check-username-and-email-exist");
				StringEntity se = new StringEntity(jObj.toString());
				httppost.setEntity(se);
				httppost.setHeader("Accept", "application/json");
				httppost.setHeader("Content-type", "application/json");
				
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
					JSONObject user = new JSONObject(data.getString("username"));
					JSONObject email = new JSONObject(data.getString("email"));
					if(user.getBoolean("exist") == true){
						Toast.makeText(getApplicationContext(), "Username already exists!", Toast.LENGTH_SHORT).show();
					}else if(email.getBoolean("exist") == true){
						Toast.makeText(getApplicationContext(), "Email already exists!", Toast.LENGTH_SHORT).show();
					}else if(user.getBoolean("exist") == false && email.getBoolean("exist") == false){
						Intent i = new Intent(Register.this, HowAboutAPhoto.class);
						Bundle bundle = new Bundle();
						bundle.putString("uname", uname.getText().toString());
						bundle.putString("email", mail.getText().toString());
						bundle.putString("age", age.getText().toString());
						bundle.putString("gender", gender.getText().toString());
						bundle.putString("pword", pword.getText().toString());
						bundle.putString("country", country);
						bundle.putString("city", city);
						i.putExtras(bundle);
						startActivity(i);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
    }
}
