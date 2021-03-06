package com.muslimmarry.adapters;

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

import com.example.muslimmarry.R;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.muslimmarry.model.DashBoardMessageItem;
import com.squareup.picasso.Picasso;

public class DashBoardMessageAdapter extends ArrayAdapter<DashBoardMessageItem> {
	
	Context mContext;
	ArrayList<DashBoardMessageItem> mlst;
	
	public DashBoardMessageAdapter(Context context, int resource,
			ArrayList<DashBoardMessageItem> objects) {
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
			rowView = inflate.inflate(R.layout.row_dashboard_message, null);
			viewHolder = new ViewHolder();
			viewHolder.avatar = (ImageView)rowView.findViewById(R.id.avatar);
			viewHolder.name = (TextView)rowView.findViewById(R.id.name);
			viewHolder.mes_qty = (TextView)rowView.findViewById(R.id.mes_qty);
			viewHolder.message = (TextView)rowView.findViewById(R.id.message);
			viewHolder.time = (RelativeTimeTextView)rowView.findViewById(R.id.time);
			viewHolder.arrow = (ImageView)rowView.findViewById(R.id.arrow);
			
			rowView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		DashBoardMessageItem item = mlst.get(position);
		if(item.getPhoto().length() > 0){
			Picasso.with(mContext).load(item.getPhoto()).placeholder(R.drawable.avatar).into(viewHolder.avatar);
		}else{
			viewHolder.avatar.setImageResource(R.drawable.avatar);
		}
		viewHolder.name.setText(item.getUsernameSend());
		viewHolder.mes_qty.setText(item.getNumMes()+" messages");
		viewHolder.message.setText(item.getContent());
		viewHolder.time.setReferenceTime(item.getTime());
		viewHolder.arrow.setImageResource(R.drawable.arrow_right);
		
		if(item.getContent().length() > 35){
			viewHolder.message.setText(item.getContent().substring(0, 35) + "...");
		}
		
		if(item.getStatus() == 0 && item.getMyId().equalsIgnoreCase(item.getUseridRecei())){
			rowView.setBackgroundColor(Color.parseColor("#e3f4f8"));
			viewHolder.name.setTextColor(Color.parseColor("#101010"));
			viewHolder.name.setTypeface(null, Typeface.BOLD);
			viewHolder.message.setTypeface(null, Typeface.BOLD);
		}
		
		return rowView;
	}
	
	static class ViewHolder{
		ImageView avatar;
		TextView name;
		TextView mes_qty;
		TextView message;
		RelativeTimeTextView time;
		ImageView arrow;
	}
	
}
