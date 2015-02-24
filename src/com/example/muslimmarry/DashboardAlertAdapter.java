package com.example.muslimmarry;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DashboardAlertAdapter extends ArrayAdapter<DashboardAlertItem> {
	
	Context mContext;
	ArrayList<DashboardAlertItem> mlst;
	
	public DashboardAlertAdapter(Context context, int resource,
			ArrayList<DashboardAlertItem> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mlst = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View rowView = convertView;
		ViewHolder viewHolder = null;
		if(rowView == null){
			LayoutInflater inflate = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflate.inflate(R.layout.dashboard_alert_row, null);
			viewHolder = new ViewHolder();
			viewHolder.avatar = (ImageView)rowView.findViewById(R.id.avatar);
			viewHolder.name = (TextView)rowView.findViewById(R.id.name);
			viewHolder.alert = (TextView)rowView.findViewById(R.id.alert);
			viewHolder.time = (TextView)rowView.findViewById(R.id.time);
			viewHolder.icon = (ImageView)rowView.findViewById(R.id.icon);
			viewHolder.arrow = (ImageView)rowView.findViewById(R.id.arrow);
			
			rowView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		DashboardAlertItem item = mlst.get(position);
		viewHolder.avatar.setImageResource(item.getAvatar());
		viewHolder.name.setText(item.getName().toString());
		viewHolder.alert.setText(item.getAlert().toString());
		viewHolder.time.setText(item.getTime().toString());
		viewHolder.icon.setImageResource(R.drawable.ic_gift);
		viewHolder.arrow.setImageResource(R.drawable.arrow_right);
		if(item.getState().equals("unread")){
			rowView.setBackgroundColor(Color.parseColor("#e3f4f8"));
			viewHolder.icon.setImageResource(R.drawable.gift_icon);
			viewHolder.name.setTextColor(Color.parseColor("#101010"));
			viewHolder.name.setTypeface(null, Typeface.BOLD);
			viewHolder.alert.setTypeface(null, Typeface.BOLD);
		}
		
		return rowView;
	}
	
	static class ViewHolder{
		ImageView avatar;
		TextView name;
		TextView alert;
		TextView time;
		ImageView icon;
		ImageView arrow;
	}
}
