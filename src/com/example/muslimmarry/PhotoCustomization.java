package com.example.muslimmarry;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.devsmart.android.ui.HorizontalListView;

public class PhotoCustomization extends Activity {
	
	ImageView back;
	ImageView photo_customize;
	private static int RESULT_LOAD_IMAGE = 1;
	private static final int CAMERA_REQUEST = 1888;
	String picturePath = "";
	
	HorizontalListView mList;
	int[] dataObjects = new int[]{ R.drawable.photo_customize_icon_01, R.drawable.photo_customize_icon_02,
			R.drawable.photo_customize_icon_03, R.drawable.photo_customize_icon_04 };
	
	String _uname = "";
	String _email = "";
	String _age = "";
	String _gender = "";
	String _pword = "";
	String country = "";
	String city = "";
	String _avatar = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.photo_customization);
		TextView title = (TextView)findViewById(R.id.title);
		back = (ImageView)findViewById(R.id.back);
		photo_customize = (ImageView)findViewById(R.id.photo_customize);
		Button btn = (Button)findViewById(R.id.btn);
		mList = (HorizontalListView)findViewById(R.id.mList);
		setFontTypeText(title);
		setFontTypeButton(btn);
		mList.setAdapter(mAdapter);
		
		// get bundle data
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
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(PhotoCustomization.this, LookingGood.class);
				Bundle bundle = new Bundle();
				bundle.putString("uname", _uname);
				bundle.putString("email", _email);
				bundle.putString("age", _age);
				bundle.putString("gender", _gender);
				bundle.putString("pword", _pword);
				bundle.putString("country", country);
				bundle.putString("city", city);
				bundle.putString("avatar", _avatar);
				bundle.putString("picturePath", picturePath);
				i.putExtras(bundle);
				startActivity(i);
			}
		});
	}
	public void setFontTypeText(TextView tv){
		Typeface face = Typeface.createFromAsset(getAssets(),
	            "fonts/moolbor_0.ttf");
		tv.setTypeface(face);
	}
	public void setFontTypeButton(Button btn){
		Typeface face = Typeface.createFromAsset(getAssets(),
	            "fonts/moolbor_0.ttf");
		btn.setTypeface(face);
	}
	
	private BaseAdapter mAdapter = new BaseAdapter() {
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_customization_row, null);
			ImageView icon = (ImageView)rowView.findViewById(R.id.icon);
			icon.setImageResource(dataObjects[position]);
			return rowView;
		}
		
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataObjects.length;
		}
	};
	private void captureImage() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
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
	    picturePath=photo.getAbsolutePath();
	    return photo;
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data){
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();
			Log.d("choose path", picturePath);
			photo_customize.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			new UploadImg().execute(picturePath);
		}else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
			Intent returnIntent = new Intent();
    		returnIntent.putExtra("Path", picturePath);
    		setResult(RESULT_OK, returnIntent);
    		Log.d("capture path", picturePath);
    		photo_customize.setImageBitmap(BitmapFactory.decodeFile(picturePath));
    		new UploadImg().execute(picturePath);
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
            HttpPost postRequest = new HttpPost("http://muslimmarry.campcoders.com/api/v1/upload-avatar");
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
            Log.d("error", s.toString());
            try{
            	_avatar = jsonReader.getString("url");
            }catch(Exception e){
            	Log.d("error", "Upload error or json not match: "+e.getMessage());
            	return null;
            }
        } catch (Exception e) {
            Log.d("error", e.getMessage(), e);
            return null;
        }
        return urlResult;
    }
}
