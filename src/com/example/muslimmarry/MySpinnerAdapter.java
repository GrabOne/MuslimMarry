package com.example.muslimmarry;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public class MySpinnerAdapter extends ArrayAdapter<String> {

	public MySpinnerAdapter(Context context, int resource, List<String> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return super.getCount() - 1;
	}
	
}
