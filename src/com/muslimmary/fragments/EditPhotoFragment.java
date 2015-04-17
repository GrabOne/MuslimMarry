package com.muslimmary.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muslimmarry.R;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.helpers.HttpDeleteWithBody;
import com.muslimmarry.helpers.UploadImage;
import com.muslimmarry.helpers.helpers;
import com.muslimmarry.model.AlbumItem;
import com.muslimmarry.sharedpref.prefUser;
import com.muslimmary.fragments.EditProfileFragment.SendDataToSharePhoto;
import com.squareup.picasso.Picasso;

public class EditPhotoFragment extends Fragment implements OnClickListener, OnTouchListener {
	
	ImageView back;
	ImageView option;
	ImageView share;
	
	RelativeLayout main_photo_group;
	RelativeLayout photo1_group;
	RelativeLayout photo2_group;
	RelativeLayout photo3_group;
	RelativeLayout photo4_group;
	RelativeLayout photo5_group;
	RelativeLayout fc_btn_main_photo;
	RelativeLayout fc_btn_photo1;
	RelativeLayout fc_btn_photo2;
	RelativeLayout fc_btn_photo3;
	RelativeLayout fc_btn_photo4;
	RelativeLayout fc_btn_photo5;
	ImageView main_photo;
	ImageView photo1;
	ImageView photo2;
	ImageView photo3;
	ImageView photo4;
	ImageView photo5;
	ImageView delete_icon_main_photo;
	ImageView delete_icon_photo1;
	ImageView delete_icon_photo2;
	ImageView delete_icon_photo3;
	ImageView delete_icon_photo4;
	ImageView delete_icon_photo5;
	
	private static int RESULT_LOAD_IMAGE = 1;
	private static final int CAMERA_REQUEST = 2;
    final int CROP_PIC = 3;
	private Uri picUri;
	String photoUrl = "";
	
	prefUser user;
	String userid = "";
	String token = "";
	String photo = "";
	String album = "";
	String resultString = "";
	
	SharedPreferences prefs;
	
	ArrayList<AlbumItem> albums = new ArrayList<AlbumItem>();
	
	SendPhotoToShareFromEdit mCallback;
	
	public interface SendPhotoToShareFromEdit{
		public void SendPhotoToShare(String photo);
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try{
			mCallback = (SendPhotoToShareFromEdit)activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_edit_photo, container, false);
		helpers.setTouch(rootView);
		
		back = (ImageView)rootView.findViewById(R.id.back);
		option = (ImageView)rootView.findViewById(R.id.option);
		share = (ImageView)rootView.findViewById(R.id.share);
		TextView title = (TextView)rootView.findViewById(R.id.title);
		title.setText("EDIT PHOTOS");
		new helpers(getActivity()).setFontTypeText(title);
		
		main_photo_group = (RelativeLayout)rootView.findViewById(R.id.main_photo_group);
		photo1_group = (RelativeLayout)rootView.findViewById(R.id.photo1_group);
		photo2_group = (RelativeLayout)rootView.findViewById(R.id.photo2_group);
		photo3_group = (RelativeLayout)rootView.findViewById(R.id.photo3_group);
		photo4_group = (RelativeLayout)rootView.findViewById(R.id.photo4_group);
		photo5_group = (RelativeLayout)rootView.findViewById(R.id.photo5_group);
		fc_btn_main_photo = (RelativeLayout)rootView.findViewById(R.id.fc_btn_main_photo);
		fc_btn_photo1 = (RelativeLayout)rootView.findViewById(R.id.fc_btn_photo1);
		fc_btn_photo2 = (RelativeLayout)rootView.findViewById(R.id.fc_btn_photo2);
		fc_btn_photo3 = (RelativeLayout)rootView.findViewById(R.id.fc_btn_photo3);
		fc_btn_photo4 = (RelativeLayout)rootView.findViewById(R.id.fc_btn_photo4);
		fc_btn_photo5 = (RelativeLayout)rootView.findViewById(R.id.fc_btn_photo5);
		
		main_photo = (ImageView)rootView.findViewById(R.id.main_photo);
		main_photo.setTag("blank");
		photo1 = (ImageView)rootView.findViewById(R.id.photo1);
		photo1.setTag("blank");
		photo2 = (ImageView)rootView.findViewById(R.id.photo2);
		photo2.setTag("blank");
		photo3 = (ImageView)rootView.findViewById(R.id.photo3);
		photo3.setTag("blank");
		photo4 = (ImageView)rootView.findViewById(R.id.photo4);
		photo4.setTag("blank");
		photo5 = (ImageView)rootView.findViewById(R.id.photo5);
		photo5.setTag("blank");
		
		delete_icon_main_photo = (ImageView)rootView.findViewById(R.id.delete_icon_main_photo);
		delete_icon_photo1 = (ImageView)rootView.findViewById(R.id.delete_icon_photo1);
		delete_icon_photo2 = (ImageView)rootView.findViewById(R.id.delete_icon_photo2);
		delete_icon_photo3 = (ImageView)rootView.findViewById(R.id.delete_icon_photo3);
		delete_icon_photo4 = (ImageView)rootView.findViewById(R.id.delete_icon_photo4);
		delete_icon_photo5 = (ImageView)rootView.findViewById(R.id.delete_icon_photo5);
		
		// set background for bottom nav element
		((MainActivity)getActivity()).setBgGroupOriginal();
		
		// create user object
		user = new prefUser(getActivity());
		HashMap<String, String> user_info = user.getUserDetail();
		userid = user_info.get(prefUser.KEY_USERID);
		token = user_info.get(prefUser.KEY_TOKEN);
		photo = user_info.get(prefUser.KEY_PHOTO);
		album = user_info.get(prefUser.KEY_ALBUM);
		if(user_info.get(prefUser.KEY_PHOTO).length() > 0){
			Picasso.with(getActivity()).load(photo).placeholder(R.drawable.avatar).fit().into(main_photo);
			main_photo.setTag(photo);
		}
		try{
			JSONArray arr = new JSONArray(album);
			albums.clear();
			for(int i=0; i<arr.length(); i++){
				if(!arr.get(i).toString().equalsIgnoreCase(photo)){
					albums.add(new AlbumItem(arr.get(i).toString()));
				}
			}
		}catch(Exception e){
			Log.e("error", e.getMessage(), e);
		}
		
		// display album
		if(albums.size() > 0){
			if(albums.size() == 1){
				Picasso.with(getActivity()).load(albums.get(0).getUrl()).placeholder(R.drawable.avatar).fit().into(photo1);
				photo1.setTag(albums.get(0).getUrl());
			}else if(albums.size() == 2){
				Picasso.with(getActivity()).load(albums.get(0).getUrl()).placeholder(R.drawable.avatar).fit().into(photo1);
				photo1.setTag(albums.get(0).getUrl());
				Picasso.with(getActivity()).load(albums.get(1).getUrl()).placeholder(R.drawable.avatar).fit().into(photo2);
				photo2.setTag(albums.get(1).getUrl());
			}else if(albums.size() == 3){
				Picasso.with(getActivity()).load(albums.get(0).getUrl()).placeholder(R.drawable.avatar).fit().into(photo1);
				photo1.setTag(albums.get(0).getUrl());
				Picasso.with(getActivity()).load(albums.get(1).getUrl()).placeholder(R.drawable.avatar).fit().into(photo2);
				photo2.setTag(albums.get(1).getUrl());
				Picasso.with(getActivity()).load(albums.get(2).getUrl()).placeholder(R.drawable.avatar).fit().into(photo3);
				photo3.setTag(albums.get(2).getUrl());
			}else if(albums.size() == 4){
				Picasso.with(getActivity()).load(albums.get(0).getUrl()).placeholder(R.drawable.avatar).fit().into(photo1);
				photo1.setTag(albums.get(0).getUrl());
				Picasso.with(getActivity()).load(albums.get(1).getUrl()).placeholder(R.drawable.avatar).fit().into(photo2);
				photo2.setTag(albums.get(1).getUrl());
				Picasso.with(getActivity()).load(albums.get(2).getUrl()).placeholder(R.drawable.avatar).fit().into(photo3);
				photo3.setTag(albums.get(2).getUrl());
				Picasso.with(getActivity()).load(albums.get(3).getUrl()).placeholder(R.drawable.avatar).fit().into(photo4);
				photo4.setTag(albums.get(3).getUrl());
			}else if(albums.size() == 5){
				Picasso.with(getActivity()).load(albums.get(0).getUrl()).placeholder(R.drawable.avatar).fit().into(photo1);
				photo1.setTag(albums.get(0).getUrl());
				Picasso.with(getActivity()).load(albums.get(1).getUrl()).placeholder(R.drawable.avatar).fit().into(photo2);
				photo2.setTag(albums.get(1).getUrl());
				Picasso.with(getActivity()).load(albums.get(2).getUrl()).placeholder(R.drawable.avatar).fit().into(photo3);
				photo3.setTag(albums.get(2).getUrl());
				Picasso.with(getActivity()).load(albums.get(3).getUrl()).placeholder(R.drawable.avatar).fit().into(photo4);
				photo4.setTag(albums.get(3).getUrl());
				Picasso.with(getActivity()).load(albums.get(4).getUrl()).placeholder(R.drawable.avatar).fit().into(photo5);
				photo5.setTag(albums.get(4).getUrl());
			}
		}
		
		// create sharedpref object
		prefs = getActivity().getSharedPreferences("user_info_pref", 0);
		
		// set event for element
		main_photo_group.setOnClickListener(this);
		photo1_group.setOnClickListener(this);
		photo2_group.setOnClickListener(this);
		photo3_group.setOnClickListener(this);
		photo4_group.setOnClickListener(this);
		photo5_group.setOnClickListener(this);
		delete_icon_main_photo.setOnClickListener(this);
		delete_icon_photo1.setOnClickListener(this);
		delete_icon_photo2.setOnClickListener(this);
		delete_icon_photo3.setOnClickListener(this);
		delete_icon_photo4.setOnClickListener(this);
		delete_icon_photo5.setOnClickListener(this);
		share.setOnClickListener(this);
		back.setOnTouchListener(this);
		option.setOnTouchListener(this);
		
		return rootView;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.main_photo_group:
			if(!main_photo.getTag().equals("blank")){
				if(fc_btn_main_photo.getVisibility() == View.VISIBLE){
					fc_btn_main_photo.setVisibility(View.GONE);
					ShowHideTopRightBtn(true, false);
				}else{
					ShowHideFcBtn(true, false, false, false, false, false);
					ShowHideTopRightBtn(false, true);
				}
			}else{
				selectImage();
			}
			break;
		case R.id.photo1_group:
			if(!photo1.getTag().equals("blank")){
				if(fc_btn_photo1.getVisibility() == View.VISIBLE){
					fc_btn_photo1.setVisibility(View.GONE);
					ShowHideTopRightBtn(true, false);
				}else{
					ShowHideFcBtn(false, true, false, false, false, false);
					ShowHideTopRightBtn(false, true);
				}
			}else{
				selectImage();
			}
			break;
		case R.id.photo2_group:
			if(!photo2.getTag().equals("blank")){
				if(fc_btn_photo2.getVisibility() == View.VISIBLE){
					fc_btn_photo2.setVisibility(View.GONE);
					ShowHideTopRightBtn(true, false);
				}else{
					ShowHideFcBtn(false, false, true, false, false, false);
					ShowHideTopRightBtn(false, true);
				}
			}else{
				selectImage();
			}
			break;
		case R.id.photo3_group:
			if(!photo3.getTag().equals("blank")){
				if(fc_btn_photo3.getVisibility() == View.VISIBLE){
					fc_btn_photo3.setVisibility(View.GONE);
					ShowHideTopRightBtn(true, false);
				}else{
					ShowHideFcBtn(false, false, false, true, false, false);
					ShowHideTopRightBtn(false, true);
				}
			}else{
				selectImage();
			}
			break;
		case R.id.photo4_group:
			if(!photo4.getTag().equals("blank")){
				if(fc_btn_photo4.getVisibility() == View.VISIBLE){
					fc_btn_photo4.setVisibility(View.GONE);
					ShowHideTopRightBtn(true, false);
				}else{
					ShowHideFcBtn(false, false, false, false, true, false);
					ShowHideTopRightBtn(false, true);
				}
			}else{
				selectImage();
			}
			break;
		case R.id.photo5_group:
			if(!photo5.getTag().equals("blank")){
				if(fc_btn_photo5.getVisibility() == View.VISIBLE){
					fc_btn_photo5.setVisibility(View.GONE);
					ShowHideTopRightBtn(true, false);
				}else{
					ShowHideFcBtn(false, false, false, false, false, true);
					ShowHideTopRightBtn(false, true);
				}
			}else{
				selectImage();
			}
			break;
		case R.id.delete_icon_main_photo:
			new RemoveImageFromCollection().execute(main_photo.getTag().toString());
			new ChangeUserAvatar().execute("");
			fc_btn_main_photo.setVisibility(View.GONE);
			main_photo.setImageBitmap(null);
			main_photo.setTag("blank");
			Editor editor = prefs.edit();
			editor.putString("photo", "").commit();
			break;
		case R.id.delete_icon_photo1:
			new RemoveImageFromCollection().execute(photo1.getTag().toString());
			fc_btn_photo1.setVisibility(View.GONE);
			photo1.setImageBitmap(null);
			photo1.setTag("blank");
			break;
		case R.id.delete_icon_photo2:
			new RemoveImageFromCollection().execute(photo2.getTag().toString());
			fc_btn_photo2.setVisibility(View.GONE);
			photo2.setImageBitmap(null);
			photo2.setTag("blank");
			break;
		case R.id.delete_icon_photo3:
			new RemoveImageFromCollection().execute(photo3.getTag().toString());
			fc_btn_photo3.setVisibility(View.GONE);
			photo3.setImageBitmap(null);
			photo3.setTag("blank");
			break;
		case R.id.delete_icon_photo4:
			new RemoveImageFromCollection().execute(photo4.getTag().toString());
			fc_btn_photo4.setVisibility(View.GONE);
			photo4.setImageBitmap(null);
			photo4.setTag("blank");
			break;
		case R.id.delete_icon_photo5:
			new RemoveImageFromCollection().execute(photo5.getTag().toString());
			fc_btn_photo5.setVisibility(View.GONE);
			photo5.setImageBitmap(null);
			photo5.setTag("blank");
			break;
		case R.id.share:
			String photoShare = "";
			if(fc_btn_main_photo.getVisibility() == View.VISIBLE){
				photoShare = main_photo.getTag().toString();
			}else if(fc_btn_photo1.getVisibility() == View.VISIBLE){
				photoShare = photo1.getTag().toString();
			}else if(fc_btn_photo2.getVisibility() == View.VISIBLE){
				photoShare = photo2.getTag().toString();
			}else if(fc_btn_photo3.getVisibility() == View.VISIBLE){
				photoShare = photo3.getTag().toString();
			}else if(fc_btn_photo4.getVisibility() == View.VISIBLE){
				photoShare = photo4.getTag().toString();
			}else if(fc_btn_photo5.getVisibility() == View.VISIBLE){
				photoShare = photo5.getTag().toString();
			}
			mCallback.SendPhotoToShare(photoShare);
			break;
		default:
			break;
		}
	}
	/*
	 * Show/hide function button
	 */
	private void ShowHideFcBtn(boolean _fc_btn_main_photo, boolean _fc_btn_photo1, boolean _fc_btn_photo2, boolean _fc_btn_photo3,
			boolean _fc_btn_photo4, boolean _fc_btn_photo5){
		if(_fc_btn_main_photo == true){
			fc_btn_main_photo.setVisibility(View.VISIBLE);
		}else{
			fc_btn_main_photo.setVisibility(View.GONE);
		}
		if(_fc_btn_photo1 == true){
			fc_btn_photo1.setVisibility(View.VISIBLE);
		}else{
			fc_btn_photo1.setVisibility(View.GONE);
		}
		if(_fc_btn_photo2 == true){
			fc_btn_photo2.setVisibility(View.VISIBLE);
		}else{
			fc_btn_photo2.setVisibility(View.GONE);
		}
		if(_fc_btn_photo3 == true){
			fc_btn_photo3.setVisibility(View.VISIBLE);
		}else{
			fc_btn_photo3.setVisibility(View.GONE);
		}
		if(_fc_btn_photo4 == true){
			fc_btn_photo4.setVisibility(View.VISIBLE);
		}else{
			fc_btn_photo4.setVisibility(View.GONE);
		}
		if(_fc_btn_photo5 == true){
			fc_btn_photo5.setVisibility(View.VISIBLE);
		}else{
			fc_btn_photo5.setVisibility(View.GONE);
		}
	}
	/*
	 * Show/hide top right button
	 */
	private void ShowHideTopRightBtn(boolean _option, boolean _share){
		if(_option == true){
			option.setVisibility(View.VISIBLE);
		}else{
			option.setVisibility(View.GONE);
		}
		if(_share == true){
			share.setVisibility(View.VISIBLE);
		}else{
			share.setVisibility(View.GONE);
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
				((MainActivity)getActivity()).hideKeyboard();
				getFragmentManager().popBackStack();
				break;
			default:
				break;
			}
		}else if(v.getId() == R.id.option){
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				option.setBackgroundColor(Color.parseColor("#2e9dbc"));
				break;
			case MotionEvent.ACTION_UP:
				option.setBackgroundColor(Color.TRANSPARENT);
				((MainActivity)getActivity()).showRightMenu();
				break;
			default:
				break;
			}
		}
		return true;
	}
	/*
	 * Select image
	 */
	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Library",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					// use standard intent to capture an image
			        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			        // we will handle the returned data in onActivityResult
			        startActivityForResult(captureIntent, CAMERA_REQUEST);
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
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data){
			picUri = data.getData();
			performCrop();
		}else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {  
			picUri = data.getData();
			performCrop();
        }else if (requestCode == CROP_PIC) {
            // get the returned data
            Bundle extras = data.getExtras();
            
            if (extras != null) {               
                Bitmap photo = extras.getParcelable("data");
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/saved_images");    
                myDir.mkdirs();
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                String fname = "Image-"+ n +".jpg";
                File file = new File (myDir, fname);
                if (file.exists ()) file.delete (); 
                try {
                   FileOutputStream out = new FileOutputStream(file);
                   photo.compress(Bitmap.CompressFormat.JPEG, 90, out);
                   out.flush();
                   out.close();
                } catch (Exception e) {
                   e.printStackTrace();
                }
                Log.d("myTag", file.toString());
                new UploadImg().execute(file.toString());
            }
        }
	}
	/*
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
                    .makeText(getActivity(), "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    /*
	 * Upload image to server and get photo url
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
				photoUrl = UploadImage.uploadImage(params[0]);
				// add image to collection
				new AddImageToCollection().execute();
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
	/*
	 * Add image to collection
	 */
	private class AddImageToCollection extends AsyncTask<String, String, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			InputStream inputStream = null;
			try{
				JSONObject obj = new JSONObject();
				obj.put("user_id", userid);
				obj.put("remember_token", token);
				obj.put("image_url", photoUrl);
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(helpers.url+"add-image-to-collection");
				StringEntity se = new StringEntity(obj.toString());
				httppost.setEntity(se);
				httppost.setHeader("Accept", "application/json");
				httppost.setHeader("Content-type", "application/json");
				HttpResponse response = httpClient.execute(httppost);
				inputStream = response.getEntity().getContent();
				if(inputStream != null){
					resultString = helpers.convertInputStreamToString(inputStream);
					Log.d("result", resultString);
				}
			}catch(Exception e){
				Log.d("error", e.getMessage(), e);
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try{
				JSONObject obj = new JSONObject(resultString);
				if(obj.getString("status").equalsIgnoreCase("success")){
					JSONObject data = new JSONObject(obj.getString("data"));
					JSONArray arr = data.getJSONArray("images");

					Editor editor = prefs.edit();
					if(main_photo.getTag().equals("blank")){
						// edit photo from sharedpref
						new ChangeUserAvatar().execute(photoUrl);
						editor.putString("photo", photoUrl);
						editor.commit();
						Picasso.with(getActivity()).load(photoUrl).placeholder(R.drawable.avatar).fit().into(main_photo);
						main_photo.setTag(photoUrl);
					}else if(photo1.getTag().equals("blank")){
						// edit album from sharedpref
						editor.putString("album", arr.toString()).commit();
						Picasso.with(getActivity()).load(photoUrl).placeholder(R.drawable.avatar).fit().into(photo1);
						photo1.setTag(photoUrl);
					}else if(photo2.getTag().equals("blank")){
						// edit album from sharedpref
						editor.putString("album", arr.toString()).commit();
						Picasso.with(getActivity()).load(photoUrl).placeholder(R.drawable.avatar).fit().into(photo2);
						photo2.setTag(photoUrl);
					}else if(photo3.getTag().equals("blank")){
						// edit album from sharedpref
						editor.putString("album", arr.toString()).commit();
						Picasso.with(getActivity()).load(photoUrl).placeholder(R.drawable.avatar).fit().into(photo3);
						photo3.setTag(photoUrl);
					}else if(photo4.getTag().equals("blank")){
						// edit album from sharedpref
						editor.putString("album", arr.toString()).commit();
						Picasso.with(getActivity()).load(photoUrl).placeholder(R.drawable.avatar).fit().into(photo4);
						photo4.setTag(photoUrl);
					}else if(photo5.getTag().equals("blank")){
						// edit album from sharedpref
						editor.putString("album", arr.toString()).commit();
						Picasso.with(getActivity()).load(photoUrl).placeholder(R.drawable.avatar).fit().into(photo5);
						photo5.setTag(photoUrl);
					}
					Toast.makeText(getActivity(), "Add image successfully!", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e){
				Log.e("error", e.getMessage(), e);
			}
		}
	}
	/*
	 * Remove image from collection
	 */
	private class RemoveImageFromCollection extends AsyncTask<String, String, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			InputStream inputStream = null;
			try{
				JSONObject obj = new JSONObject();
				obj.put("user_id", userid);
				obj.put("remember_token", token);
				obj.put("image_url", params[0]);
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpDeleteWithBody httpdelete = new HttpDeleteWithBody(helpers.url+"remove-image-to-collection");
				StringEntity se = new StringEntity(obj.toString());
				httpdelete.setEntity(se);
				httpdelete.setHeader("Accept", "application/json");
				httpdelete.setHeader("Content-type", "application/json");
				
				HttpResponse response = httpClient.execute(httpdelete);
				
				inputStream = response.getEntity().getContent();
				if(inputStream != null){
					resultString = helpers.convertInputStreamToString(inputStream);
					Log.d("result", resultString);
				}
			}catch(Exception e){
				Log.e("error", e.getMessage(), e);
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try{
				JSONObject obj = new JSONObject(resultString);
				if(obj.getString("status").equalsIgnoreCase("success")){
					JSONObject data = new JSONObject(obj.getString("data"));
					String album = "";
					if(!data.isNull("images")){
						JSONArray albumArr = data.getJSONArray("images");
						album = albumArr.toString();
					}
					Editor editor = prefs.edit();
					// edit album from sharedpref
					editor.putString("album", album).commit();
					Toast.makeText(getActivity(), "Remove image successfully!", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e){
				Log.e("error", e.getMessage(), e);
			}
		}
	}
	/*
	 * Change user avatar
	 */
	private class ChangeUserAvatar extends AsyncTask<String, String, Void>{
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			InputStream inputStream = null;
			try{
				JSONObject obj = new JSONObject();
				obj.put("user_id", userid);
				obj.put("remember_token", token);
				obj.put("avatar", params[0]);
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpPut httpput = new HttpPut(helpers.url+"change-avatar");
				StringEntity se = new StringEntity(obj.toString());
				httpput.setEntity(se);
				httpput.setHeader("Accept", "application/json");
				httpput.setHeader("Content-type", "application/json");
				HttpResponse response = httpClient.execute(httpput);
				inputStream = response.getEntity().getContent();
				if(inputStream != null){
					resultString = helpers.convertInputStreamToString(inputStream);
					Log.d("changephoto", resultString);
				}
			}catch(Exception e){
				Log.e("error", e.getMessage(), e);
			}
			return null;
		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((MainActivity)getActivity()).showTopNav(false);
	}
}
