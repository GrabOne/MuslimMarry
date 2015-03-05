package com.muslimmarry.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.example.muslimmarry.R;


public class Notify {
	private static AlertDialog dialogDetails;
	public static void show(Context context, String notify){
		   LayoutInflater inflater = LayoutInflater.from(context);
		   View dialogview = inflater.inflate(R.drawable.notify_layout, null);
		   TextView tvnotify = (TextView)dialogview.findViewById(R.id.notify);
		   Button btnok = (Button)dialogview.findViewById(R.id.btnok);
		   tvnotify.setText(notify);
		   AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(context);
		   dialogbuilder.setView(dialogview);
		   dialogDetails = dialogbuilder.create();
		   dialogDetails.setCancelable(true);
		   dialogDetails.show();
		   dialogDetails.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		   
		   btnok.setOnClickListener(new OnClickListener() {
			
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialogDetails.dismiss();
				}
			});
	}
}
