package com.muslimmarry.activities;

import java.io.File;
import java.util.Random;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.muslimmarry.helpers.UploadImage;
import com.muslimmarry.helpers.helpers;

public class PhotoCustomizationActivity extends Activity implements OnClickListener, OnTouchListener {
	
	ImageView back;
	ImageView photo_customize;
	private static int RESULT_LOAD_IMAGE = 1;
	private static final int CAMERA_REQUEST = 2;
    final int CROP_PIC = 3;
	private Uri picUri;
	String picturePath = "";
	
	String _uname = "";
	String _email = "";
	String _age = "";
	String _gender = "";
	String _pword = "";
	String country = "";
	String city = "";
	String _photo = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_photo_customization);
		
		TextView title = (TextView)findViewById(R.id.title);
		back = (ImageView)findViewById(R.id.back);
		photo_customize = (ImageView)findViewById(R.id.photo_customize);
		Button btn = (Button)findViewById(R.id.btn);
		
		// set font for element
		new helpers(getApplicationContext()).setFontTypeText(title);
		new helpers(getApplicationContext()).setFontTypeButton(btn);
		
		// set event for element
		back.setOnTouchListener(this);
		btn.setOnClickListener(this);
		
		// get data bundle
		try{
			Bundle getResults = getIntent().getExtras();
			_uname = getResults.getString("uname");
			_email = getResults.getString("email");
			_age = getResults.getString("age");
			_gender = getResults.getString("gender");
			_pword = getResults.getString("pword");
			country = getResults.getString("country");
			city = getResults.getString("city");
		}catch(NullPointerException e){
			e.printStackTrace();
		}
		
		try{
			if(getIntent().getExtras().getString("flag").equals("upload photo")){
				Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}else if(getIntent().getExtras().getString("flag").equals("take photo")){
				captureImage();
			}else{
				
			}
		}catch(NullPointerException e){}
	}
	
	private void captureImage() {
		// use standard intent to capture an image
        Intent captureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        // we will handle the returned data in onActivityResult
        startActivityForResult(captureIntent, CAMERA_REQUEST);
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data){
			picUri = data.getData();
			performCrop();
		}else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
			picUri = data.getData();
			performCrop();
        }else if (requestCode == CROP_PIC) {
            // get the returned data
            Bundle extras = data.getExtras();
            
            if (extras != null) {               
                Bitmap photo = extras.getParcelable("data");
                photo_customize.setImageBitmap(photo);
                
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/saved_images");    
                myDir.mkdirs();
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                picturePath = myDir + "/Image-"+ n +".jpg";
                Log.d("myTag", picturePath);
                new UploadImg().execute(picturePath);
            }
        }
	};
	/*
	 * Upload image to server
	 */
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
				_photo = UploadImage.uploadImage(params[0]);
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

	/**
     * this function does the crop operation.
     */
    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 2);
            cropIntent.putExtra("aspectY", 2);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
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
			default:
				break;
			}
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.btn){
			Intent i = new Intent(PhotoCustomizationActivity.this, PhotoConfirmationActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("uname", _uname);
			bundle.putString("email", _email);
			bundle.putString("age", _age);
			bundle.putString("gender", _gender);
			bundle.putString("pword", _pword);
			bundle.putString("country", country);
			bundle.putString("city", city);
			bundle.putString("photo", _photo);
			bundle.putString("picturePath", picturePath);
			i.putExtras(bundle);
			startActivity(i);
		}
	}
}
