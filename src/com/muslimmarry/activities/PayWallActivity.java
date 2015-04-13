package com.muslimmarry.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.muslimmarry.R;
import com.muslimmarry.helpers.helpers;

public class PayWallActivity extends Activity implements OnTouchListener, OnClickListener {
	
	ImageView back;
	EditText plan;
	
	AlertDialog dialog;
	String[] plans;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_paywall);
		plan = (EditText)findViewById(R.id.plan);
		TextView title = (TextView)findViewById(R.id.title);
		TextView title2 = (TextView)findViewById(R.id.title2);
		back = (ImageView)findViewById(R.id.back);
		Button upgrade = (Button)findViewById(R.id.upgrade);
		
		// set font for element
		new helpers(getApplicationContext()).setFontTypeText(title);
		new helpers(getApplicationContext()).setFontTypeText(title2);
		new helpers(getApplicationContext()).setFontTypeButton(upgrade);
		
		// set event for element
		back.setOnTouchListener(this);
		upgrade.setOnClickListener(this);
		plan.setOnClickListener(this);
	}
	
	private void SelectPlan(){
		plans = new String[] {"4.99$/MONTH"};
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(PayWallActivity.this);
		LayoutInflater inflater = getLayoutInflater();
		View convertView = inflater.inflate(R.layout.layout_my_dialog, null);
		alertDialog.setView(convertView);
		alertDialog.setTitle("Select Plan!");
		ListView lv = (ListView)convertView.findViewById(R.id.lv);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(PayWallActivity.this, android.R.layout.simple_list_item_1, plans);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.upgrade:
			Intent i = new Intent(PayWallActivity.this, MainActivity.class);
			i.putExtra("flag", 1);
			startActivity(i);
			break;
		case R.id.plan:
			SelectPlan();
			break;
		default:
			break;
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
				new helpers(PayWallActivity.this).PushActivityRight();
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
		new helpers(PayWallActivity.this).PushActivityRight();
	}
}
