package com.example.muslimmarry;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
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
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.example.muslimmarry.SearchFilter.SendDataToSearchResult;
import com.example.muslimmarry.libraries.TransparentProgressDialog;
import com.example.muslimmarry.sharedpref.prefUser;
import com.google.android.gms.plus.PlusShare;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.squareup.picasso.Picasso;

public class MainActivity extends Activity implements SendDataToSearchResult, OnClickListener {
	
	Fragment fragment = null;
	SlidingMenu menu;
//	DrawerLayout drawer;
	ListView navList;
	PopupWindow popup;
	View dim;
	GridView mGrid;
	ViewGroup bell;
	ViewGroup find_user;
	ViewGroup message;
	ArrayList<SendGiftItem> mlst = new ArrayList<SendGiftItem>();
	SendGiftAdapter adapter;
	
	int year;
    int month;
    int day;
 
    static final int DATE_PICKER_ID = 1111; 
    
    prefUser user;
    TransparentProgressDialog pd;
    String resultString = "";
    
    final String[] data ={"edit profile","account","app settings", "billing", "invite a friend", "contact us", "log out"};
	
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
		
		setBgGroupFindUser();
		
		// redirect to payment option
		try{
			if(getIntent().getExtras().getInt("flag") == 1){
				setBgGroupOriginal();
				PaymentOption fr = new PaymentOption();
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
		
		// navigation drawer
//		NavDrawerAdapter adapter = new NavDrawerAdapter(this, data);
//		drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
//		navList = (ListView) findViewById(R.id.drawer);
//		navList.setAdapter(adapter);
//		navList.setOnItemClickListener(new SlideMenuClickListener());
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.RIGHT);
		menu.setBehindOffset(120);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.right_menu);
		NavDrawerAdapter adapter = new NavDrawerAdapter(this, data);
		navList = (ListView)findViewById(R.id.drawer);
		navList.setAdapter(adapter);
		navList.setOnItemClickListener(new SlideMenuClickListener());
		ImageView large_img = (ImageView)findViewById(R.id.large_img);
		// create user object
		user = new prefUser(this);
		HashMap<String, String> user_info = user.getUserDetail();
		if(user_info.get(prefUser.KEY_AVATAR).length() > 0){
			Picasso.with(this).load(user_info.get(prefUser.KEY_AVATAR)).fit().centerCrop().into(large_img);
		}
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
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		switch (position) {
		case 0:
			fragment = new EditProfile();
			break;
		case 1:
			fragment = new AccountSettings();
			break;
		case 2:
			fragment = new AppSettings();
			break;
		case 3:
			fragment = null;
			Intent i = new Intent(MainActivity.this, PayWall.class);
			startActivity(i);
			break;
		case 4:
			fragment = new Invite();
			break;
		case 5:
			break;
		case 6:
			fragment = null;
			user = new prefUser(MainActivity.this);
			user.LogoutUser();
			break;
		default:
			break;
		}

		if (fragment != null) {
			// update selected item and title, then close the drawer
			navList.setItemChecked(position, true);
			navList.setSelection(position);
//			drawer.closeDrawer(navList);
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
			 fr = new DashboardMessage();
		 
		 }else if(view == findViewById(R.id.bell)) {
			 fr = new DashboardAlert();
		 
		 }else if(view == findViewById(R.id.find_user)) {
			 fr = new SearchFilter();
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
//		drawer.openDrawer(navList);
		menu.toggle();
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
	/*
	 * popup send gift
	 */
	public void displayPopupSendGift(View anchorView){
		View layout = getLayoutInflater().inflate(R.layout.send_gift_layout, null);
		ImageView close = (ImageView)layout.findViewById(R.id.close);
		Button sendgift = (Button)layout.findViewById(R.id.sendgift);
		setFontTypeButton(sendgift);
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
				pd = new TransparentProgressDialog(MainActivity.this, R.drawable.loading_spinner);
				pd.show();
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						pd.dismiss();
						dim.setVisibility(View.GONE);
						popup.dismiss();
					}
				}, 2000);
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
		mlst.clear();
		mlst.add(new SendGiftItem(R.drawable.gift_box_01,  false));
		mlst.add(new SendGiftItem(R.drawable.gift_box_02,  false));
		mlst.add(new SendGiftItem(R.drawable.gift_box_03,  false));
		mlst.add(new SendGiftItem(R.drawable.gift_box_04,  false));
		mlst.add(new SendGiftItem(R.drawable.gift_box_05,  false));
		mlst.add(new SendGiftItem(R.drawable.gift_box_06,  false));
		mlst.add(new SendGiftItem(R.drawable.gift_box_07,  false));
		mlst.add(new SendGiftItem(R.drawable.gift_box_08,  false));
		mlst.add(new SendGiftItem(R.drawable.gift_box_09,  false));
		adapter = new SendGiftAdapter(getApplicationContext(), R.layout.send_gift_grid_item, mlst);
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
		View layout = getLayoutInflater().inflate(R.layout.share_promocode_layout, null);
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
            // add pickerListener listner to date picker
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
            ((EditProfile)getFragmentManager().findFragmentById(R.id.frag)).setTextYearMonthDay(year, month, day);
        }
    };
    
    public void setYearMonthDay(String birthday){
    	year = Integer.parseInt(birthday.substring(0, 4));
    	month = Integer.parseInt(birthday.substring(5, 7));
    	day = Integer.parseInt(birthday.substring(8, 10));
    }
    
    public void backActivity(){
    	finish();
    }
	@Override
	public void SendArrList(String arrList) {
		// TODO Auto-generated method stub
		SearchResult fr = new SearchResult();
		Bundle bundle = new Bundle();
		bundle.putString("arrList", arrList);
		fr.setArguments(bundle);
		FragmentManager fm = getFragmentManager();
	    FragmentTransaction fragmentTransaction = fm.beginTransaction();
	    fragmentTransaction.replace(R.id.frag, fr);
	    fragmentTransaction.addToBackStack(null).commit();
	}
	/*
	 * Send data from Search result to profile
	 */
	public void SendUserInfo(String id, String avatar, String name, String username, String age, String language, String height, String occupation) {
		// TODO Auto-generated method stub
		Profile fr = new Profile();
		Bundle bundle = new Bundle();
		bundle.putString("id", id);
		bundle.putString("avatar", avatar);
		bundle.putString("name", name);
		bundle.putString("username", username);
		bundle.putString("age", age);
		bundle.putString("language", language);
		bundle.putString("height", height);
		bundle.putString("occupation", occupation);
		fr.setArguments(bundle);
		FragmentManager fm = getFragmentManager();
	    FragmentTransaction fragmentTransaction = fm.beginTransaction();
	    fragmentTransaction.replace(R.id.frag, fr);
	    fragmentTransaction.addToBackStack(null).commit();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
