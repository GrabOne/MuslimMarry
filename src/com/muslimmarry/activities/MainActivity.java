package com.muslimmarry.activities;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.muslimmarry.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.plus.PlusShare;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.muslimmarry.adapters.NavDrawerAdapter;
import com.muslimmarry.adapters.SendGiftAdapter;
import com.muslimmarry.helpers.TransparentProgressDialog;
import com.muslimmarry.helpers.helpers;
import com.muslimmarry.model.SendGiftItem;
import com.muslimmarry.sharedpref.prefUser;
import com.muslimmary.fragments.AccountSettingFragment;
import com.muslimmary.fragments.AppSettingFragment;
import com.muslimmary.fragments.DashboardAlertFragment;
import com.muslimmary.fragments.DashboardAlertFragment.SendDataToGiftReceivePage;
import com.muslimmary.fragments.DashboardMessageFragment;
import com.muslimmary.fragments.DashboardMessageFragment.SendDataToMessagingPage;
import com.muslimmary.fragments.EditProfileFragment;
import com.muslimmary.fragments.EditPhotoFragment.SendPhotoToShareFromEdit;
import com.muslimmary.fragments.EditProfileFragment.SendDataToSharePhoto;
import com.muslimmary.fragments.FavoriteFragment;
import com.muslimmary.fragments.GiftReceivePageFragment;
import com.muslimmary.fragments.InviteFragment;
import com.muslimmary.fragments.MessagingPageFragment;
import com.muslimmary.fragments.PaymentOptionFragment;
import com.muslimmary.fragments.ProfileFragment;
import com.muslimmary.fragments.SearchFilterFragment;
import com.muslimmary.fragments.SearchFilterFragment.SendDataToSearchResult;
import com.muslimmary.fragments.SearchResultFragment;
import com.muslimmary.fragments.SharePhotoFragment;
import com.squareup.picasso.Picasso;

public class MainActivity extends Activity implements SendDataToSearchResult, SendDataToSharePhoto, SendPhotoToShareFromEdit, SendDataToGiftReceivePage,
	SendDataToMessagingPage, OnTouchListener {
	
	RelativeLayout top_nav;
	ImageView back;
	ImageView option;
	TextView title;
	ImageView muslim_icon;
	ImageView gift_icon;
	RelativeLayout notifi_mes_box;
	TextView num_notifi_mes;
	RelativeLayout notifi_gift_box;
	TextView num_notifi_gift;
	Fragment fragment = null;
	SlidingMenu menu;
	ListView navList;
	PopupWindow popup;
	View dim;
	GridView mGrid;
	ViewGroup bell;
	ViewGroup find_user;
	ViewGroup message;
	SendGiftAdapter adapter;
	
	int year;
    int month;
    int day;
 
    static final int DATE_PICKER_ID = 1111; 
    
    prefUser user;
    String userid = "";
    String token = "";
    String photo = "";
    String username = "";
    String gift = "";
    
    String resultString = "";
    
    final String[] data ={"edit profile","account","app settings", "billing", "favorites", "invite a friend", "contact us", "log out"};
	
    TransparentProgressDialog pd;
    
    SharedPreferences prefs;
    
    Socket socket;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		dim = (View)findViewById(R.id.dim);
		bell = (ViewGroup)findViewById(R.id.bell);
		find_user = (ViewGroup)findViewById(R.id.find_user);
		message = (ViewGroup)findViewById(R.id.message);
		back = (ImageView)findViewById(R.id.back);
		option = (ImageView)findViewById(R.id.option);
		title = (TextView)findViewById(R.id.title);
		muslim_icon = (ImageView)findViewById(R.id.muslim_icon);
		gift_icon = (ImageView)findViewById(R.id.gift_icon);
		top_nav = (RelativeLayout)findViewById(R.id.top_nav);
		notifi_mes_box = (RelativeLayout)findViewById(R.id.notifi_mes_box);
		num_notifi_mes = (TextView)findViewById(R.id.num_notifi_mes);
		notifi_gift_box = (RelativeLayout)findViewById(R.id.notifi_gift_box);
		num_notifi_gift = (TextView)findViewById(R.id.num_notifi_gift);
		
		// set event for element
		back.setOnTouchListener(this);
		option.setOnTouchListener(this);
		
		// set font for element
		new helpers(getApplicationContext()).setFontTypeText(title);
		
		// set background bottom nav elements
		setBgGroupFindUser();
    	
		// redirect to payment option
		try{
			if(getIntent().getExtras().getInt("flag") == 1){
				setBgGroupOriginal();
				PaymentOptionFragment fr = new PaymentOptionFragment();
				FragmentManager fm = getFragmentManager();
			    FragmentTransaction fragmentTransaction = fm.beginTransaction();
			    fragmentTransaction.replace(R.id.frag, fr);
			    fragmentTransaction.addToBackStack(null).commit();
			}
		}catch(NullPointerException e){}
		
		// capture event dialog dismiss
		popup = new PopupWindow(this);
		popup.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				dim.setVisibility(View.GONE);
			}
		});
		
		// sliding menu
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.RIGHT);
		menu.setBehindOffset(140);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.right_menu);
		NavDrawerAdapter adapter = new NavDrawerAdapter(this, data);
		navList = (ListView)findViewById(R.id.drawer);
		navList.setAdapter(adapter);
		navList.setOnItemClickListener(new SlideMenuClickListener());
		ImageView wallpaper = (ImageView)findViewById(R.id.wallpaper);
		// create user object
		user = new prefUser(this);
		HashMap<String, String> user_info = user.getUserDetail();
		userid = user_info.get(prefUser.KEY_USERID);
		token = user_info.get(prefUser.KEY_TOKEN);
		photo = user_info.get(prefUser.KEY_PHOTO);
		if(user_info.get(prefUser.KEY_NAME).length() > 0){
			username = user_info.get(prefUser.KEY_NAME);
		}else{
			username = user_info.get(prefUser.KEY_USERNAME);
		}
		if(photo.length() > 0){
			Picasso.with(this).load(photo).fit().centerCrop().into(wallpaper);
		}
		
		// get num notifi message
		if(Integer.parseInt(user_info.get(prefUser.KEY_UNREAD)) > 0){
			SetNumNotifiMes(true, user_info.get(prefUser.KEY_UNREAD));
		}else{
			SetNumNotifiMes(false, user_info.get(prefUser.KEY_UNREAD));
		}
		
		// get num notifi gift
		if(Integer.parseInt(user_info.get(prefUser.KEY_GIFT_UNREAD)) > 0){
			SetNumNotifiGift(true, user_info.get(prefUser.KEY_GIFT_UNREAD));
		}else{
			SetNumNotifiGift(false, user_info.get(prefUser.KEY_GIFT_UNREAD));
		}
		
		// create prefs object
		prefs = getSharedPreferences("user_info_pref", 0);
		
		// connect socket
		ConnSocket();
		
	}
	/**
	 * Connect socket
	 */
	private void ConnSocket(){
		try{
			IO.Options opts = new IO.Options();
			opts.forceNew = true;
			opts.reconnection = true;
        	socket = IO.socket("http://muslimmarry.campcoders.com:7777/chat?user_id="+ userid +"&token=" + token, opts);
        	socket.on("send_message", new Emitter.Listener() {
				
				@Override
				public void call(Object... arg0) {
					// TODO Auto-generated method stub
					final JSONObject obj = (JSONObject)arg0[0];
					Log.d("myTag", obj.toString());
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try{
								// update num notifi message
								int unread = GetNumNotifiMes() + obj.getInt("unread");
								SetNumNotifiMes(true, String.valueOf(unread));
								// update unread value
								SharedPreferences.Editor editor = prefs.edit();
								editor.putString("unread", String.valueOf(unread)).commit();
							}catch(Exception e){
								Log.e("error", e.getMessage(), e);
							}
						}
					});
				}
			}).on("send_gift", new Emitter.Listener() {
				
				@Override
				public void call(Object... arg0) {
					// TODO Auto-generated method stub
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							int unread = Integer.parseInt(num_notifi_gift.getText().toString()) + 1;
							SetNumNotifiGift(true, String.valueOf(unread));
						}
					});
				}
			});
        	socket.connect();
        }catch(URISyntaxException e){
        	Log.e("error", e.getMessage(), e);
        }
	}
	/**
	 * Set title
	 */
	public void setTitle(String str){
		title.setText(str.toUpperCase());
	}
	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}
	/**
	 * Displaying fragment view for selected sliding menu list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		switch (position) {
		case 0:
			fragment = new EditProfileFragment();
			break;
		case 1:
			fragment = new AccountSettingFragment();
			break;
		case 2:
			fragment = new AppSettingFragment();
			break;
		case 3:
			fragment = null;
			Intent i = new Intent(MainActivity.this, PayWallActivity.class);
			startActivity(i);
			break;
		case 4:
			fragment = new FavoriteFragment();
			break;
		case 5:
			fragment = new InviteFragment();
			break;
		case 6:
			break;
		case 7:
			fragment = null;
			user = new prefUser(MainActivity.this);
			user.LogoutUser();
			break;
		default:
			break;
		}
		if (fragment != null) {
			// update selected item and title, then close the menu
			navList.setItemChecked(position, true);
			navList.setSelection(position);
			menu.toggle();
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frag, fragment).addToBackStack(null).commit();
				}
			}, 400);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}
	public void selectFrag(View view) {
		 Fragment fr;
		 if(view == findViewById(R.id.message)) {
			 fr = new DashboardMessageFragment();
		 
		 }else if(view == findViewById(R.id.bell)) {
			 fr = new DashboardAlertFragment();
		 
		 }else if(view == findViewById(R.id.find_user)) {
			 fr = new SearchFilterFragment();
			 setBgGroupFindUser();
		 }else{
			 fr = null;
		 }
		 
		 if(fr != null){
			 FragmentManager fm = getFragmentManager();
		     FragmentTransaction fragmentTransaction = fm.beginTransaction();
		     fragmentTransaction.replace(R.id.frag, fr);
		     fragmentTransaction.addToBackStack(null).commit();
		 }
	}
	
	public void showRightMenu(){
		menu.toggle();
	}
	
	/*
	 * popup send gift
	 */
	public void displayPopupSendGift(View anchorView, final ArrayList<SendGiftItem> mlst, final String user_recei_name, final String user_recei_id){
		View layout = getLayoutInflater().inflate(R.layout.layout_send_gift, null);
		ImageView close = (ImageView)layout.findViewById(R.id.close);
		Button sendgift = (Button)layout.findViewById(R.id.sendgift);
		TextView note = (TextView)layout.findViewById(R.id.note);
		note.setText("Send " + user_recei_name + " a gift to show your interest ");
		new helpers(MainActivity.this).setFontTypeButton(sendgift);
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				popup.dismiss();
			}
		});
		sendgift.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int check = 0;
				for(int i=0; i<mlst.size(); i++){
					if(mlst.get(i).getState() == true){
						gift = mlst.get(i).getImage();
						check += 1;
					}
				}
				if(check == 0){
					Toast.makeText(getApplicationContext(), "Please select at least a gift!", Toast.LENGTH_SHORT).show();
				}else if(check > 1){
					Toast.makeText(getApplicationContext(), "Please select only one a gift!", Toast.LENGTH_SHORT).show();
				}else{
					dim.setVisibility(View.GONE);
					popup.dismiss();
					try{
						IO.Options opts = new IO.Options();
						opts.forceNew = true;
						opts.reconnection = true;
			        	socket = IO.socket("http://muslimmarry.campcoders.com:7777/chat?user_id="+ userid +"&token=" + token, opts);
			        	socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
							
							@Override
							public void call(Object... arg0) {
								// TODO Auto-generated method stub
								try{
									JSONObject obj = new JSONObject();
									obj.put("user_send", userid);
									obj.put("user_recei", user_recei_id);
									obj.put("image_url", gift);
									obj.put("username", username);
									obj.put("avatar", photo);
									socket.emit("send_gift", obj);
								}catch(Exception e){}
							}
						}).on("send_gift", new Emitter.Listener() {
							
							@Override
							public void call(Object... arg0) {
								// TODO Auto-generated method stub
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										int unread = Integer.parseInt(num_notifi_gift.getText().toString()) + 1;
										SetNumNotifiGift(true, String.valueOf(unread));
									}
								});
							}
						}).on("send_gift_success", new Emitter.Listener() {
							
							@Override
							public void call(Object... arg0) {
								// TODO Auto-generated method stub
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										Toast.makeText(MainActivity.this, "Send gift successful!", Toast.LENGTH_SHORT).show();
									}
								});
							}
						}).on("send_message", new Emitter.Listener() {
							
							@Override
							public void call(Object... arg0) {
								// TODO Auto-generated method stub
								final JSONObject obj = (JSONObject)arg0[0];
								Log.d("myTag", obj.toString());
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										try{
											// update num notifi message
											int unread = GetNumNotifiMes() + obj.getInt("unread");
											SetNumNotifiMes(true, String.valueOf(unread));
											// update unread value
											SharedPreferences.Editor editor = prefs.edit();
											editor.putString("unread", String.valueOf(unread)).commit();
										}catch(Exception e){
											Log.e("error", e.getMessage(), e);
										}
									}
								});
							}
						});
			        	socket.connect();
					}catch(URISyntaxException e){
						Log.e("error", e.getMessage(), e);
					}
				}
			}
		});
		mGrid = (GridView)layout.findViewById(R.id.mGrid);
		mGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				if(mlst.get(position).getState() == false){
					mlst.get(position).setState(true);
				}else{
					mlst.get(position).setState(false);
				}
				adapter.notifyDataSetChanged();
			}
		});
		adapter = new SendGiftAdapter(getApplicationContext(), R.layout.row_item_gift, mlst);
		mGrid.setAdapter(adapter);
		
		dim.setVisibility(View.VISIBLE);
		popup.setContentView(layout);
		popup.setHeight(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        popup.setWidth(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        // Show anchored to button
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
        popup.showAsDropDown(anchorView);
	}
	/*
	 * popup share promo code
	 */
	public void displayPopupSharePromocode(View anchorView){
		View layout = getLayoutInflater().inflate(R.layout.layout_share_promocode, null);
		ImageView close = (ImageView)layout.findViewById(R.id.close);
		ImageView gg_share = (ImageView)layout.findViewById(R.id.gg_share);
		ImageView gm_share = (ImageView)layout.findViewById(R.id.gm_share);
		ImageView fb_share = (ImageView)layout.findViewById(R.id.fb_share);
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dim.setVisibility(View.GONE);
				popup.dismiss();
			}
		});
		gg_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent shareIntent = new PlusShare.Builder(getApplicationContext())
		          .setType("text/plain")
		          .setText("with my code oe5j2 we both get __ discount off the Muslim Marry app. Download the app at www.muslimmarry.de")
		          .setContentUrl(Uri.parse("https://developers.google.com/+/"))
		          .getIntent();

				startActivityForResult(shareIntent, 0);
			}
		});
		gm_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent shareIntent = new PlusShare.Builder(getApplicationContext())
		          .setType("text/plain")
		          .setText("with my code oe5j2 we both get __ discount off the Muslim Marry app. Download the app at www.muslimmarry.de")
		          .setContentUrl(Uri.parse("https://developers.google.com/+/"))
		          .getIntent();

				startActivityForResult(shareIntent, 0);
			}
		});
		fb_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		dim.setVisibility(View.VISIBLE);
		popup.setContentView(layout);
		popup.setHeight(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        popup.setWidth(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        // Show anchored to button
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
        popup.showAsDropDown(anchorView);
	}
	/*
	 * Show/hide top nav
	 */
	public void showTopNav(boolean flag){
		if(flag == true){
			top_nav.setVisibility(View.VISIBLE);
		}else{
			top_nav.setVisibility(View.GONE);
		}
	}
	
	/*
	 * Show/hide element of top nav
	 */
	public void setElementTopNav(boolean _back, boolean _title, boolean _gift_icon, boolean _muslim_icon){
		if(_back == true){
			back.setVisibility(View.VISIBLE);
		}else{
			back.setVisibility(View.GONE);
		}
		
		if(_title == true){
			title.setVisibility(View.VISIBLE);
		}else{
			title.setVisibility(View.GONE);
		}
		
		if(_gift_icon == true){
			gift_icon.setVisibility(View.VISIBLE);
		}else{
			gift_icon.setVisibility(View.GONE);
		}
		
		if(_muslim_icon == true){
			muslim_icon.setVisibility(View.VISIBLE);
		}else{
			muslim_icon.setVisibility(View.GONE);
		}
	}
	
	public void setBgGroupBell(){
		bell.setBackgroundColor(Color.parseColor("#399bb7"));
		find_user.setBackgroundColor(Color.parseColor("#53bcdc"));
		message.setBackgroundColor(Color.parseColor("#53bcdc"));
	}
	
	public void setBgGroupFindUser(){
		bell.setBackgroundColor(Color.parseColor("#53bcdc"));
		find_user.setBackgroundColor(Color.parseColor("#399bb7"));
		message.setBackgroundColor(Color.parseColor("#53bcdc"));
	}
	
	public void setBgGroupMessage(){
		bell.setBackgroundColor(Color.parseColor("#53bcdc"));
		find_user.setBackgroundColor(Color.parseColor("#53bcdc"));
		message.setBackgroundColor(Color.parseColor("#399bb7"));
	}
	
	public void setBgGroupOriginal(){
		bell.setBackgroundColor(Color.parseColor("#53bcdc"));
		find_user.setBackgroundColor(Color.parseColor("#53bcdc"));
		message.setBackgroundColor(Color.parseColor("#53bcdc"));
	}
	
	public void showDateDialog(){
		showDialog(DATE_PICKER_ID);
	}
	
	public void hideKeyboard(){
		InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
	    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
        case DATE_PICKER_ID:
             
            // open datepicker dialog. 
            // set date picker for current date 
            // add pickerListener listener to date picker
            return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
	}
	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
		 
        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                int selectedMonth, int selectedDay) {
             
            year  = selectedYear;
            month = selectedMonth+1;
            day   = selectedDay;
 
            // Show selected date
            ((EditProfileFragment)getFragmentManager().findFragmentById(R.id.frag)).setTextYearMonthDay(year, month, day);
        }
    };
    
    public void setYearMonthDay(String birthday){
    	year = Integer.parseInt(birthday.substring(0, 4));
    	month = Integer.parseInt(birthday.substring(5, 7));
    	day = Integer.parseInt(birthday.substring(8, 10));
    }
    
    /*
     * Send arrList from SearchFilterFragment to SearchResultFragment
     */
    
	@Override
	public void SendArrList(String arrList) {
		// TODO Auto-generated method stub
		SearchResultFragment fr = new SearchResultFragment();
		Bundle bundle = new Bundle();
		bundle.putString("arrList", arrList);
		fr.setArguments(bundle);
		FragmentManager fm = getFragmentManager();
	    FragmentTransaction fragmentTransaction = fm.beginTransaction();
	    fragmentTransaction.replace(R.id.frag, fr);
	    fragmentTransaction.addToBackStack(null).commit();
	}
	/*
	 * Send data from SearchResultFragment to ProfileFragment
	 */
	public void SendUserInfo(String id, String photo, String name, String username, String age, String language, String height, String occupation, String city) {
		// TODO Auto-generated method stub
		ProfileFragment fr = new ProfileFragment();
		Bundle bundle = new Bundle();
		bundle.putString("id", id);
		bundle.putString("photo", photo);
		bundle.putString("name", name);
		bundle.putString("username", username);
		bundle.putString("age", age);
		bundle.putString("language", language);
		bundle.putString("height", height);
		bundle.putString("occupation", occupation);
		bundle.putString("city", city);
		fr.setArguments(bundle);
		FragmentManager fm = getFragmentManager();
	    FragmentTransaction fragmentTransaction = fm.beginTransaction();
	    fragmentTransaction.replace(R.id.frag, fr);
	    fragmentTransaction.addToBackStack(null).commit();
	}
	/*
	 * Send image url from EditProfileFragment to SharePhotoFragment
	 */
	@Override
	public void SendPhoto(String photo) {
		// TODO Auto-generated method stub
		SharePhotoFragment fr = new SharePhotoFragment();
		Bundle bundle = new Bundle();
		bundle.putString("photo", photo);
		fr.setArguments(bundle);
		FragmentManager fm = getFragmentManager();
	    FragmentTransaction fragmentTransaction = fm.beginTransaction();
	    fragmentTransaction.replace(R.id.frag, fr);
	    fragmentTransaction.addToBackStack(null).commit();
	}
	/*
	 * Send image url from EditPhotoFragment to SharePhotoFragment
	 */
	@Override
	public void SendPhotoToShare(String photo) {
		// TODO Auto-generated method stub
		SharePhotoFragment fr = new SharePhotoFragment();
		Bundle bundle = new Bundle();
		bundle.putString("photo", photo);
		fr.setArguments(bundle);
		FragmentManager fm = getFragmentManager();
	    FragmentTransaction fragmentTransaction = fm.beginTransaction();
	    fragmentTransaction.replace(R.id.frag, fr);
	    fragmentTransaction.addToBackStack(null).commit();
	}
	/*
	 * Send data to MessagingPage
	 */
	@Override
	public void sendIdFromDashboardMessage(String id, String name) {
		// TODO Auto-generated method stub
		MessagingPageFragment fr = new MessagingPageFragment();
		Bundle bundle = new Bundle();
		bundle.putString("friendId", id);
		bundle.putString("name", name);
		fr.setArguments(bundle);
		FragmentManager fm = getFragmentManager();
	    FragmentTransaction fragmentTransaction = fm.beginTransaction();
	    fragmentTransaction.replace(R.id.frag, fr);
	    fragmentTransaction.addToBackStack(null).commit();
	}
	/*
	 * Send data from DashboardAlertFragment to GiftReceivePageFragment
	 */
	@Override
	public void SendDataToGiftReceivePageFragment(String userid_send, String username_send, String gift) {
		// TODO Auto-generated method stub
		GiftReceivePageFragment fr = new GiftReceivePageFragment();
		Bundle bundle = new Bundle();
		bundle.putString("userid_send", userid_send);
		bundle.putString("username_send", username_send);
		bundle.putString("gift", gift);
		fr.setArguments(bundle);
		FragmentManager fm = getFragmentManager();
	    FragmentTransaction fragmentTransaction = fm.beginTransaction();
	    fragmentTransaction.replace(R.id.frag, fr);
	    fragmentTransaction.addToBackStack(null).commit();
	}
	/*
	 * get num notifi mes
	 */
	public int GetNumNotifiMes(){
		return Integer.parseInt(num_notifi_mes.getText().toString());
	}
	/*
	 * set num notifi mes
	 */
	public void SetNumNotifiMes(boolean flag, String num){
		if(flag == true){
			notifi_mes_box.setVisibility(View.VISIBLE);
		}else{
			notifi_mes_box.setVisibility(View.GONE);
		}
		num_notifi_mes.setText(String.valueOf(num));
	}
	/*
	 * set num notifi gift
	 */
	public void SetNumNotifiGift(boolean flag, String num){
		if(flag == true){
			notifi_gift_box.setVisibility(View.VISIBLE);
		}else{
			notifi_gift_box.setVisibility(View.GONE);
		}
		num_notifi_gift.setText(String.valueOf(num));
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
				hideKeyboard();
				if(title.getText().toString().equalsIgnoreCase("settings")){
					finish();
				}else{
					getFragmentManager().popBackStack();
				}
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
				showRightMenu();
				break;
			default:
				break;
			}
		}
		return true;
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		new helpers(MainActivity.this).PushActivityRight();
	}
}
