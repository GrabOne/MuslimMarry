package com.example.muslimmarry;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class SearchResultAdapter extends ArrayAdapter<SearchResultItem> {
	
	Context mContext;
	ArrayList<SearchResultItem> mlst;
	
	public SearchResultAdapter(Context context, int resource,
			ArrayList<SearchResultItem> objects) {
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
			rowView = inflate.inflate(R.layout.search_result_row, null);
			viewHolder = new ViewHolder();
			viewHolder.bottom_bar = (ViewGroup)rowView.findViewById(R.id.bottom_bar);
			viewHolder.avatar = (ImageView)rowView.findViewById(R.id.avatar);
			viewHolder.info_bg = (ImageView)rowView.findViewById(R.id.info_bg);
			viewHolder.heart = (ImageView)rowView.findViewById(R.id.heart);
			viewHolder.play = (ImageView)rowView.findViewById(R.id.play);
			viewHolder.txtage = (TextView)rowView.findViewById(R.id.txtage);
			viewHolder.age = (TextView)rowView.findViewById(R.id.age);
			viewHolder.txtjob = (TextView)rowView.findViewById(R.id.txtjob);
			viewHolder.job = (TextView)rowView.findViewById(R.id.job);
			rowView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		SearchResultItem item = mlst.get(position);
		viewHolder.bottom_bar.setBackgroundResource(R.drawable.info_bg);
		if(item.getAvatar().length() > 0){
			Picasso.with(mContext).load(item.getAvatar()).fit().centerInside().into(viewHolder.avatar);
		}
		viewHolder.info_bg.setBackgroundColor(Color.parseColor("#80FFFFFF"));
		viewHolder.txtage.setText("AGE");
		viewHolder.age.setText(item.getAge()+"");
		viewHolder.txtjob.setText("JOB");
		viewHolder.job.setText(item.getOccupation().toString());
		
		if(item.getClick() == true){
			viewHolder.info_bg.setVisibility(View.VISIBLE);
			viewHolder.bottom_bar.setBackgroundResource(0);
			viewHolder.heart.setVisibility(View.VISIBLE);
			viewHolder.play.setVisibility(View.VISIBLE);
			viewHolder.heart.setImageResource(R.drawable.heart_icon);
			viewHolder.play.setImageResource(R.drawable.play_icon);
		}else{
			viewHolder.info_bg.setVisibility(View.GONE);
			viewHolder.bottom_bar.setBackgroundResource(R.drawable.info_bg);
			viewHolder.heart.setVisibility(View.GONE);
			viewHolder.play.setVisibility(View.GONE);
		}
		
		if(item.getOccupation().length() > 12){
			viewHolder.job.setText(item.getOccupation().substring(0, 12) + "...");
		}
		
		viewHolder.play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				View parentRow = (View) v.getParent();
				GridView gr = (GridView) parentRow.getParent();
				final int pos = gr.getPositionForView(parentRow);
				((MainActivity)mContext).SendUserInfo(mlst.get(pos).getId(), mlst.get(pos).getAvatar(), mlst.get(pos).getName(), mlst.get(pos).getUsername(), mlst.get(pos).getAge(), mlst.get(pos).getLanguage(), mlst.get(pos).getHeight(), mlst.get(pos).getOccupation());
			}
		});
		
		return rowView;
	}
	
	static class ViewHolder{
		ViewGroup bottom_bar;
		ImageView avatar;
		ImageView info_bg;
		ImageView heart;
		ImageView play;
		TextView txtage;
		TextView age;
		TextView txtjob;
		TextView job;
	}
}
