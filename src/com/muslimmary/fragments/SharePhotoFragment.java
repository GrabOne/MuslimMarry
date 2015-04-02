package com.muslimmary.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muslimmarry.R;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.helpers.helpers;
import com.squareup.picasso.Picasso;

public class SharePhotoFragment extends Fragment implements OnClickListener {
	
	ImageView photo;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_share_photo, container, false);
		helpers.setTouch(rootView);
		TextView more = (TextView)rootView.findViewById(R.id.more);
		photo = (ImageView)rootView.findViewById(R.id.photo);
		more.setOnClickListener(this);
		
		try{
			String avatar = getArguments().getString("avatar");
			Picasso.with(getActivity()).load(avatar).fit().centerCrop().into(photo);
		}catch(NullPointerException e){}
		
		((MainActivity)getActivity()).setBgGroupOriginal();
		return rootView;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((MainActivity)getActivity()).setElementTopNav(true, true, false, false);
		((MainActivity)getActivity()).setTitle("share");
		((MainActivity)getActivity()).showTopNav(true);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.more:
			Uri bmpUri = helpers.getLocalBitmapUri(photo);
		    if (bmpUri != null) {
		        // Construct a ShareIntent with link to image
		        Intent shareIntent = new Intent();
		        shareIntent.setAction(Intent.ACTION_SEND);
		        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
		        shareIntent.setType("image/*");
		        // Launch sharing dialog for image
		        startActivity(Intent.createChooser(shareIntent, "Share with..."));    
		    } else {
		        // ...sharing failed, handle error
		    }
			break;

		default:
			break;
		}
	}
}
