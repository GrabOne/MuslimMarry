package com.muslimmarry.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.muslimmarry.R;

public class NavDrawerAdapter extends BaseAdapter {
	
	String[] datas;
	Context mContext;
	LayoutInflater inflater;
	
	public NavDrawerAdapter(Context context, String[] datas){
		super();
		this.mContext = context;
		this.datas = datas;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return datas[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView == null){
			inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_nav_drawer, null);
			viewHolder = new ViewHolder();
			viewHolder.txt = (TextView)convertView.findViewById(R.id.txt);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.txt.setText(datas[position].toUpperCase());
		return convertView;
	}
	private class ViewHolder {
        TextView txt;
	}
	
}
