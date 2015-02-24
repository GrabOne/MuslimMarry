package com.example.muslimmarry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PayWall extends Activity {
	
	ImageView back;
	EditText plan;
	
	AlertDialog dialog;
	String[] plans;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.paywall);
		plan = (EditText)findViewById(R.id.plan);
		TextView title = (TextView)findViewById(R.id.title);
		TextView title2 = (TextView)findViewById(R.id.title2);
		back = (ImageView)findViewById(R.id.back);
		Button upgrade = (Button)findViewById(R.id.upgrade);
		setFontTypeText(title);
		setFontTypeText(title2);
		setFontTypeButton(upgrade);
		
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
		upgrade.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(PayWall.this, MainActivity.class);
				i.putExtra("flag", 1);
				startActivity(i);
			}
		});
		plan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SelectPlan();
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
	private void SelectPlan(){
		plans = new String[] {"4.99$/MONTH"};
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(PayWall.this);
		LayoutInflater inflater = getLayoutInflater();
		View convertView = inflater.inflate(R.layout.plans_list_for_dialog, null);
		alertDialog.setView(convertView);
		alertDialog.setTitle("Select Plan!");
		ListView lv = (ListView)convertView.findViewById(R.id.lvPlan);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(PayWall.this, android.R.layout.simple_list_item_1, plans);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				plan.setText(plans[arg2]);
			}
		});
		dialog = alertDialog.create();
		dialog.show();
	}
}
