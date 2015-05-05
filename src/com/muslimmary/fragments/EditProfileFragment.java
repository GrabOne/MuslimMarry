package com.muslimmary.fragments;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muslimmarry.R;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.helpers.ImageManager;
import com.muslimmarry.helpers.TransparentProgressDialog;
import com.muslimmarry.helpers.helpers;
import com.muslimmarry.sharedpref.prefUser;
import com.squareup.picasso.Picasso;

public class EditProfileFragment extends Fragment implements OnClickListener {
	
	RelativeLayout name_group;
	View line1;
	ImageView wallpaper;
	ImageView photo;
	ImageView arrow_dategr;
	ImageView arrow_occgr;
	ImageView arrow_heightgr;
	ImageView arrow_citygr;
	ImageView arrow_languagegr;
	EditText name;
	EditText religion;
	EditText body;
	EditText city;
	EditText language;
	EditText birthday;
	Button done;
	
	String userid = "";
	String token = "";
	String _name = "";
	String _username = "";
	String _email = "";
	String _age = "";
	String _birthday = "";
	String _gender = "";
	String _photo = "";
	String _occupation = "";
	String _height = "";
	String _language = "";
	String _country = "";
	String _city = "";
	String lat = "";
	String lng = "";
	String code = "";
	String login_with = "";
	String social_id= "";
	String is_social = "";
	
	String[] arrHeight = {"1.30", "1.32", "1.34", "1.36", "1.38", "1.40", "1.42", "1.44", "1.46", "1.48", "1.50",
							"1.52", "1.54", "1.56", "1.58", "1.60", "1.62", "1.64", "1.66", "1.68", "1.70", "1.72",
							"1.74", "1.76", "1.78", "1.80", "1.82", "1.84", "1.86", "1.88", "1.90", "1.92", "1.94",
							"1.96", "1.98", "2.00", "2.02", "2.04", "2.06", "2.08", "2.10", "2.12", "2.14", "2.16",
							"2.18", "2.20", "2.22", "2.24", "2.26", "2.28", "2.30"};
	
	private static int RESULT_LOAD_IMAGE = 1;
	private static final int CAMERA_REQUEST = 1888;
	private String mCurrentPhotoPath;
	
	AlertDialog dialog;
	String[] speaks;
	String[] jobs;
	
	// Shared pref mode
    int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREF_NAME = "user_info_pref";
    
    SharedPreferences pref;
    Editor editor;
    
    SendDataToSharePhoto mCallback;
    
    prefUser user;
	
	String resultString = "";
	
	TransparentProgressDialog pd;
    
    public interface SendDataToSharePhoto{
    	public void SendPhoto(String photo);
    }
    
    @Override
    public void onAttach(Activity activity) {
    	// TODO Auto-generated method stub
    	super.onAttach(activity);
    	try{
			mCallback = (SendDataToSharePhoto)activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
		}
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
		helpers.setTouch(rootView);
		
		name_group = (RelativeLayout)rootView.findViewById(R.id.name_group);
		line1 = (View)rootView.findViewById(R.id.line1);
		wallpaper = (ImageView)rootView.findViewById(R.id.wallpaper);
		photo = (ImageView)rootView.findViewById(R.id.photo);
		arrow_dategr = (ImageView)rootView.findViewById(R.id.arrow_dategr);
		arrow_occgr = (ImageView)rootView.findViewById(R.id.arrow_occgr);
		arrow_heightgr = (ImageView)rootView.findViewById(R.id.arrow_heightgr);
		arrow_citygr = (ImageView)rootView.findViewById(R.id.arrow_citygr);
		arrow_languagegr = (ImageView)rootView.findViewById(R.id.arrow_languagegr);
		name = (EditText)rootView.findViewById(R.id.name);
		religion = (EditText)rootView.findViewById(R.id.religion);
		body = (EditText)rootView.findViewById(R.id.body);
		city = (EditText)rootView.findViewById(R.id.city);
		language = (EditText)rootView.findViewById(R.id.language);
		birthday = (EditText)rootView.findViewById(R.id.birthday);
		done = (Button)rootView.findViewById(R.id.done);
		TextView tvshare = (TextView)rootView.findViewById(R.id.tvshare);
		
		// set background for bottom nav element
		((MainActivity)getActivity()).setBgGroupOriginal();
		
		// set font for element
		new helpers(getActivity()).setFontTypeButton(done);
		
		// set event for element
		photo.setOnClickListener(this);
		tvshare.setOnClickListener(this);
		arrow_dategr.setOnClickListener(this);
		arrow_occgr.setOnClickListener(this);
		arrow_heightgr.setOnClickListener(this);
		arrow_citygr.setOnClickListener(this);
		arrow_languagegr.setOnClickListener(this);
		done.setOnClickListener(this);
		
		// create pref object
		pref = getActivity().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
		
		// create user object
		user = new prefUser(getActivity());
		HashMap<String, String> user_info = user.getUserDetail();
		name.setText(user_info.get(prefUser.KEY_USERNAME));
		userid = user_info.get(prefUser.KEY_USERID);
		token = user_info.get(prefUser.KEY_TOKEN);
		_name = user_info.get(prefUser.KEY_NAME);
		_username = user_info.get(prefUser.KEY_USERNAME);
		_email = user_info.get(prefUser.KEY_EMAIL);
		_age = user_info.get(prefUser.KEY_AGE);
		_birthday = user_info.get(prefUser.KEY_BIRTH_DAY);
		_gender = user_info.get(prefUser.KEY_GENDER);
		_photo = user_info.get(prefUser.KEY_PHOTO);
		_occupation = user_info.get(prefUser.KEY_OCCUPATION);
		_height = user_info.get(prefUser.KEY_HEIGHT);
		_language = user_info.get(prefUser.KEY_LANGUAGE);
		_country = user_info.get(prefUser.KEY_COUNTRY);
		_city = user_info.get(prefUser.KEY_CITY);
		lat = user_info.get(prefUser.KEY_LAT);
		lng = user_info.get(prefUser.KEY_LNG);
		code = user_info.get(prefUser.KEY_CODE);
		login_with = user_info.get(prefUser.KEY_LOGIN_WITH);
		social_id = user_info.get(prefUser.KEY_SOCIAL_ID);
		is_social = user_info.get(prefUser.KEY_IS_SOCIAL);
		if(is_social.equalsIgnoreCase("true")){
			name_group.setVisibility(View.GONE);
			line1.setVisibility(View.GONE);
		}
		birthday.setText(user_info.get(prefUser.KEY_BIRTH_DAY));
		if(user_info.get(prefUser.KEY_OCCUPATION).length() > 0){
			religion.setText(user_info.get(prefUser.KEY_OCCUPATION).substring(0, 1).toUpperCase() + user_info.get(prefUser.KEY_OCCUPATION).substring(1));
		}
		body.setText(_height);
		city.setText(_city);
		
		try{
			JSONArray jArr = new JSONArray(user_info.get(prefUser.KEY_LANGUAGE));
			if(jArr.length() <= 0){
				language.setText("");
			}else{
				String str = "";
				for(int i=0; i<jArr.length(); i++){
					if(jArr.get(i).toString().length() > 0){
						str += (jArr.getString(i).substring(0, 1).toUpperCase() + jArr.getString(i).substring(1)) +", ";
					}
				}
				language.setText(str.replaceAll(", $", ""));
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		// display photo url
		if(_photo.length() > 0){
			Picasso.with(getActivity()).load(_photo).placeholder(R.drawable.avatar).into(photo);
			Picasso.with(getActivity()).load(_photo).fit().centerCrop().into(wallpaper);
		}else{
			photo.setImageResource(R.drawable.avatar);
		}
		
		return rootView;
	}
	
	public void setTextYearMonthDay(int year, int month, int day){
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
		birthday.setText(year + "-" + _month + "-" + _day);
	}
	public void selectFragCustomize(Fragment fr){
		FragmentManager fm = getFragmentManager();
	    FragmentTransaction fragmentTransaction = fm.beginTransaction();
	    fragmentTransaction.replace(R.id.frag, fr);
	    fragmentTransaction.addToBackStack(null).commit();
	}
	/*
	 * Update normal account
	 */
	private class UpdateNormalAccount extends AsyncTask<String, String, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new TransparentProgressDialog(getActivity(), R.drawable.loading_spinner);
			pd.show();
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			InputStream inputStream = null;
			try{
				JSONObject jObj = new JSONObject();
				jObj.put("user_id", userid);
				jObj.put("remember_token", token);
				jObj.put("username", name.getText().toString());
				jObj.put("birthday", birthday.getText().toString());
				if(religion.getText().toString().length() > 0){
					jObj.put("occupation", religion.getText().toString());
				}
				if(body.getText().toString().length() > 0){
					jObj.put("height", body.getText().toString());
				}
				jObj.put("city", city.getText().toString());
				
				StringTokenizer st = new StringTokenizer(language.getText().toString(), ",");
				JSONArray lanArr = new JSONArray();;
				while(st.hasMoreElements()){
					String str = st.nextElement().toString();
					lanArr.put(str.replace(" ", ""));
				}
				jObj.put("language", lanArr);
				
				//Log.d("myTag", jObj.toString());
				HttpClient httpClient = new DefaultHttpClient();
				HttpPut httpput = new HttpPut(helpers.url+"edit-normal-account");
				httpput.setEntity(new ByteArrayEntity(jObj.toString().getBytes("UTF8")));
				httpput.setHeader("Accept", "application/json");
				httpput.setHeader("Content-type", "application/json;charset=UTF-8");
				httpput.setHeader("Accept-Charset", "utf-8");
				
				HttpResponse response = httpClient.execute(httpput);
				
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
					
					editor.putString("birthday", birthday.getText().toString());
					editor.putString("occupation", religion.getText().toString());
					editor.putString("height", body.getText().toString());
					editor.putString("city", city.getText().toString());
					editor.putString("language", languageArr.toString());
					editor.commit();
					
					name.setFocusable(false);
					name.setBackgroundColor(Color.TRANSPARENT);
					city.setFocusable(false);
					city.setBackgroundColor(Color.TRANSPARENT);
					language.setFocusable(false);
					language.setBackgroundColor(Color.TRANSPARENT);
					Toast.makeText(getActivity(), "Save successful!", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	/*
	 * Update social account
	 */
	private class UpdateSocialAccount extends AsyncTask<String, String, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new TransparentProgressDialog(getActivity(), R.drawable.loading_spinner);
			pd.show();
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			InputStream inputStream = null;
			try{
				JSONObject jObj = new JSONObject();
				jObj.put("user_id", userid);
				jObj.put("remember_token", token);
				if(birthday.getText().toString().length() > 0){
					jObj.put("birthday", birthday.getText().toString());
				}
				if(religion.getText().toString().length() > 0){
					jObj.put("occupation", religion.getText().toString());
				}
				if(body.getText().toString().length() > 0){
					jObj.put("height", body.getText().toString());
				}
				if(city.getText().toString().length() > 0){
					jObj.put("city", city.getText().toString());
				}
				
				if(language.getText().toString().length() > 0){
					StringTokenizer st = new StringTokenizer(language.getText().toString(), ",");
					JSONArray lanArr = new JSONArray();;
					while(st.hasMoreElements()){
						String str = st.nextElement().toString();
						lanArr.put(str.replace(" ", ""));
					}
					jObj.put("language", lanArr);
				}
				
				//Log.d("myTag", jObj.toString());
				HttpClient httpClient = new DefaultHttpClient();
				HttpPut httpput = new HttpPut(helpers.url+"edit-social-account");
				httpput.setEntity(new ByteArrayEntity(jObj.toString().getBytes("UTF8")));
				httpput.setHeader("Accept", "application/json");
				httpput.setHeader("Content-type", "application/json;charset=UTF-8");
				httpput.setHeader("Accept-Charset", "utf-8");
				
				HttpResponse response = httpClient.execute(httpput);
				
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
					
					editor.putString("birthday", birthday.getText().toString());
					editor.putString("occupation", religion.getText().toString());
					editor.putString("height", body.getText().toString());
					editor.putString("city", city.getText().toString());
					editor.putString("language", languageArr.toString());
					editor.commit();
					
					city.setFocusable(false);
					city.setBackgroundColor(Color.TRANSPARENT);
					language.setFocusable(false);
					language.setBackgroundColor(Color.TRANSPARENT);
					Toast.makeText(getActivity(), "Save successful!", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Library",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					captureImage();
				} else if (items[item].equals("Choose from Library")) {
					Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(i, RESULT_LOAD_IMAGE);
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}
	private void captureImage() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
	        File photoFile = null;
	        try {
	            photoFile = createImageFile();
	        } catch (IOException ex) {
	        	Log.e("Mainactivity", "Cannot create image: "+ex.getMessage(), ex);
	        }
	        if (photoFile != null) {
	            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
	                    Uri.fromFile(photoFile));
	            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
	        }
	    }
    }
	private File createImageFile() throws IOException {
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + ".jpg";

	    File photo = new File(Environment.getExternalStorageDirectory(),  imageFileName);
	    mCurrentPhotoPath=photo.getAbsolutePath();
	    return photo;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == RESULT_LOAD_IMAGE && null != data){
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			Bitmap bm = BitmapFactory.decodeFile(picturePath);
			photo.setImageBitmap(ImageManager.scaleBitmap(bm, 480, 480));
			new UploadImg().execute(picturePath);
		}else if (requestCode == CAMERA_REQUEST) {  
			Intent returnIntent = new Intent();
    		returnIntent.putExtra("Path", mCurrentPhotoPath);
    		getActivity().setResult(getActivity().RESULT_OK, returnIntent);
    		Log.d("capture path", mCurrentPhotoPath);
    		Bitmap bm = BitmapFactory.decodeFile(mCurrentPhotoPath);
			photo.setImageBitmap(ImageManager.scaleBitmap(bm, 480, 480));
    		new UploadImg().execute(mCurrentPhotoPath);
        }
	}
	private class UploadImg extends AsyncTask<String, String, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				uploadImage(params[0]);
			} catch (Exception e) {
				Log.d("error", e.getMessage(), e);
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
	}
	
	private String getNameFromPath(String path){
		return path;
	}
	private CompressFormat getFormat(String path){
		return CompressFormat.JPEG;
	}
	@SuppressWarnings("deprecation")
	private String uploadImage(String filePath) throws Exception {
		String urlResult=null;
        try {
        	Bitmap bm = BitmapFactory.decodeFile(filePath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(getFormat(filePath), 75, bos);
            byte[] data = bos.toByteArray();
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(helpers.url+"upload-avatar");
            ByteArrayBody bab = new ByteArrayBody(data, getNameFromPath(filePath));
            MultipartEntity reqEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("image", bab);
            postRequest.setEntity(reqEntity);

            HttpResponse response = httpClient.execute(postRequest);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            StringBuilder s = new StringBuilder();
            
            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }
            JSONObject jsonReader = new JSONObject(s.toString());
            Log.d("result", s.toString());
            try{
            	_photo = jsonReader.getString("url");
            	new ChangeAvatar().execute(_photo);
            }catch(JSONException e){
            	Log.d("error", "Upload error or json not match: "+e.getMessage());
            	return null;
            }
        } catch (Exception e) {
            Log.d("error", e.getMessage(), e);
            return null;
        }
        return urlResult;
    }
	/*
	 * Change user avatar
	 */
	private class ChangeAvatar extends AsyncTask<String, String, Void>{
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			InputStream inputStream = null;
			try{
				JSONObject jObj = new JSONObject();
				jObj.put("user_id", userid);
				jObj.put("remember_token", token);
				jObj.put("avatar", params[0]);
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpPut httpput = new HttpPut(helpers.url+"change-avatar");
				StringEntity se = new StringEntity(jObj.toString());
				httpput.setEntity(se);
				httpput.setHeader("Accept", "application/json");
				httpput.setHeader("Content-type", "application/json");
				
				HttpResponse response = httpClient.execute(httpput);
				
				inputStream = response.getEntity().getContent();
				if(inputStream != null){
					resultString = helpers.convertInputStreamToString(inputStream);
					Log.d("myTag", resultString);
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
			try{
				JSONObject jObj = new JSONObject(resultString);
				if(jObj.getString("status").equalsIgnoreCase("success")){
					editor.putString("photo", _photo);
					editor.commit();
				}else{
					Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tvshare:
			if(_photo.length() > 0){
				((MainActivity)getActivity()).hideKeyboard();
				mCallback.SendPhoto(_photo);
			}else{
				Toast.makeText(getActivity(), "Image not found to share", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.arrow_dategr:
			if(birthday.getText().toString().length() > 0){
				((MainActivity)getActivity()).setYearMonthDay(birthday.getText().toString());
			}
			((MainActivity)getActivity()).showDateDialog();
			break;
		case R.id.arrow_occgr:
			selectJob();
			break;
		case R.id.arrow_heightgr:
			selectHeight();
			break;
		case R.id.arrow_citygr:
			city.setFocusableInTouchMode(true);
			city.setCursorVisible(true);
			city.requestFocus();
			break;
		case R.id.arrow_languagegr:
			language.setFocusableInTouchMode(true);
			language.setCursorVisible(true);
			language.requestFocus();
			break;
		case R.id.done:
			if(is_social.equalsIgnoreCase("true")){
				new UpdateSocialAccount().execute();
			}else{
				new UpdateNormalAccount().execute();
			}
			break;
		case R.id.photo:
			EditPhotoFragment fr = new EditPhotoFragment();
			FragmentManager fm = getFragmentManager();
		    FragmentTransaction fragmentTransaction = fm.beginTransaction();
		    fragmentTransaction.replace(R.id.frag, fr);
		    fragmentTransaction.addToBackStack(null).commit();
		    break;
		default:
			break;
		}
	}
	/*
	 * Select job
	 */
	private void selectJob(){
		jobs = new String[] {"Student", "Doctor" , "Teacher"};
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View convertView = inflater.inflate(R.layout.layout_my_dialog, null);
		alertDialog.setView(convertView);
		alertDialog.setTitle("Select Occupation!");
		ListView lv = (ListView)convertView.findViewById(R.id.lv);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, jobs);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				religion.setText(jobs[arg2].toString());
			}
		});
		dialog = alertDialog.create();
		dialog.show();
	}
	/*
	 * Select height
	 */
	private void selectHeight(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View convertView = inflater.inflate(R.layout.layout_my_dialog, null);
		alertDialog.setView(convertView);
		alertDialog.setTitle("Select Height!");
		ListView lv = (ListView)convertView.findViewById(R.id.lv);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrHeight);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				body.setText(arrHeight[arg2].toString());
			}
		});
		dialog = alertDialog.create();
		dialog.show();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((MainActivity)getActivity()).setElementTopNav(true, true, false, false);
		((MainActivity)getActivity()).setTitle("edit profile");
		((MainActivity)getActivity()).showTopNav(true);
	}
}
