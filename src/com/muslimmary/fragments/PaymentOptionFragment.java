package com.muslimmary.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muslimmarry.R;
import com.muslimmarry.activities.MainActivity;

public class PaymentOptionFragment extends Fragment {
	
	ImageView back;
	ImageView option;
	Button b_bank;
	Button b_paypal;
	Button b_card;
	ViewGroup rlbank;
	ViewGroup rlpaypal;
	ViewGroup rlcard;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_payment_option, container, false);
		TextView title = (TextView)rootView.findViewById(R.id.title);
		back = (ImageView)rootView.findViewById(R.id.back);
		option = (ImageView)rootView.findViewById(R.id.option);
		rlbank = (ViewGroup)rootView.findViewById(R.id.rlbank);
		rlpaypal = (ViewGroup)rootView.findViewById(R.id.rlpaypal);
		rlcard = (ViewGroup)rootView.findViewById(R.id.rlcard);
		b_bank = (Button)rootView.findViewById(R.id.b_bank);
		b_paypal = (Button)rootView.findViewById(R.id.b_paypal);
		b_card = (Button)rootView.findViewById(R.id.b_card);
		TextView b_service_price = (TextView)rootView.findViewById(R.id.b_service_price);
		TextView pp_service_price = (TextView)rootView.findViewById(R.id.pp_service_price);
		TextView c_service_price = (TextView)rootView.findViewById(R.id.c_service_price);
		Button b_upgrade = (Button)rootView.findViewById(R.id.b_upgrade);
		Button pp_upgrade = (Button)rootView.findViewById(R.id.pp_upgrade);
		Button c_upgrade = (Button)rootView.findViewById(R.id.c_upgrade);
		((MainActivity)getActivity()).setFontTypeText(b_service_price);
		((MainActivity)getActivity()).setFontTypeText(c_service_price);
		((MainActivity)getActivity()).setFontTypeText(pp_service_price);
		((MainActivity)getActivity()).setFontTypeButton(b_upgrade);
		((MainActivity)getActivity()).setFontTypeButton(pp_upgrade);
		((MainActivity)getActivity()).setFontTypeButton(c_upgrade);
		rootView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		((MainActivity)getActivity()).setFontTypeText(title);
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
					((MainActivity)getActivity()).hideKeyboard();
					((MainActivity)getActivity()).backActivity();
					break;
				default:
					break;
				}
				return true;
			}
		});
		option.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					option.setBackgroundColor(Color.parseColor("#2e9dbc"));
					break;
				case MotionEvent.ACTION_UP:
					option.setBackgroundColor(Color.TRANSPARENT);
					((MainActivity)getActivity()).showRightMenu();
				default:
					break;
				}
				return true;
			}
		});
		
		b_bank.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				b_bank.setBackgroundResource(R.drawable.bank_btn_click);
				b_bank.setTextColor(Color.parseColor("#FFFFFF"));
				b_paypal.setBackgroundResource(R.drawable.paypal_btn);
				b_paypal.setTextColor(Color.parseColor("#6fcbe7"));
				b_card.setBackgroundColor(Color.TRANSPARENT);
				b_card.setTextColor(Color.parseColor("#6fcbe7"));
				rlbank.setVisibility(View.VISIBLE);
				rlpaypal.setVisibility(View.GONE);
				rlcard.setVisibility(View.GONE);
			}
		});
		
		b_paypal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				b_bank.setBackgroundColor(Color.TRANSPARENT);
				b_bank.setTextColor(Color.parseColor("#6fcbe7"));
				b_paypal.setBackgroundColor(Color.parseColor("#6fcbe7"));
				b_paypal.setTextColor(Color.parseColor("#FFFFFF"));
				b_card.setBackgroundColor(Color.TRANSPARENT);
				b_card.setTextColor(Color.parseColor("#6fcbe7"));
				rlbank.setVisibility(View.GONE);
				rlpaypal.setVisibility(View.VISIBLE);
				rlcard.setVisibility(View.GONE);
			}
		});
		
		b_card.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				b_bank.setBackgroundColor(Color.TRANSPARENT);
				b_bank.setTextColor(Color.parseColor("#6fcbe7"));
				b_paypal.setBackgroundResource(R.drawable.paypal_btn_2);
				b_paypal.setTextColor(Color.parseColor("#6fcbe7"));
				b_card.setBackgroundResource(R.drawable.card_btn_click);
				b_card.setTextColor(Color.parseColor("#FFFFFF"));
				rlbank.setVisibility(View.GONE);
				rlpaypal.setVisibility(View.GONE);
				rlcard.setVisibility(View.VISIBLE);
			}
		});
		
		return rootView;
	}
}
