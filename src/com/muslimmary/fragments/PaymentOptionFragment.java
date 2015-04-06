package com.muslimmary.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.muslimmarry.R;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.helpers.helpers;

public class PaymentOptionFragment extends Fragment implements OnClickListener {
	
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
		helpers.setTouch(rootView);
		
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
		
		// set font for element
		Button c_upgrade = (Button)rootView.findViewById(R.id.c_upgrade);
		new helpers(getActivity()).setFontTypeText(b_service_price);
		new helpers(getActivity()).setFontTypeText(c_service_price);
		new helpers(getActivity()).setFontTypeText(pp_service_price);
		new helpers(getActivity()).setFontTypeButton(b_upgrade);
		new helpers(getActivity()).setFontTypeButton(pp_upgrade);
		new helpers(getActivity()).setFontTypeButton(c_upgrade);
		
		// set event for element
		b_bank.setOnClickListener(this);
		b_paypal.setOnClickListener(this);
		b_card.setOnClickListener(this);
		
		return rootView;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((MainActivity)getActivity()).setElementTopNav(true, true, false, false);
		((MainActivity)getActivity()).setTitle("settings");
		((MainActivity)getActivity()).showTopNav(true);
	}
//	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		((MainActivity)getActivity()).setTitle("search filters");
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.b_bank:
			b_bank.setBackgroundResource(R.drawable.bank_btn_click);
			b_bank.setTextColor(Color.parseColor("#FFFFFF"));
			b_paypal.setBackgroundResource(R.drawable.paypal_btn);
			b_paypal.setTextColor(Color.parseColor("#6fcbe7"));
			b_card.setBackgroundColor(Color.TRANSPARENT);
			b_card.setTextColor(Color.parseColor("#6fcbe7"));
			rlbank.setVisibility(View.VISIBLE);
			rlpaypal.setVisibility(View.GONE);
			rlcard.setVisibility(View.GONE);
			break;
		case R.id.b_paypal:
			b_bank.setBackgroundColor(Color.TRANSPARENT);
			b_bank.setTextColor(Color.parseColor("#6fcbe7"));
			b_paypal.setBackgroundColor(Color.parseColor("#6fcbe7"));
			b_paypal.setTextColor(Color.parseColor("#FFFFFF"));
			b_card.setBackgroundColor(Color.TRANSPARENT);
			b_card.setTextColor(Color.parseColor("#6fcbe7"));
			rlbank.setVisibility(View.GONE);
			rlpaypal.setVisibility(View.VISIBLE);
			rlcard.setVisibility(View.GONE);
			break;
		case R.id.b_card:
			b_bank.setBackgroundColor(Color.TRANSPARENT);
			b_bank.setTextColor(Color.parseColor("#6fcbe7"));
			b_paypal.setBackgroundResource(R.drawable.paypal_btn_2);
			b_paypal.setTextColor(Color.parseColor("#6fcbe7"));
			b_card.setBackgroundResource(R.drawable.card_btn_click);
			b_card.setTextColor(Color.parseColor("#FFFFFF"));
			rlbank.setVisibility(View.GONE);
			rlpaypal.setVisibility(View.GONE);
			rlcard.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}
}
