package com.muslimmarry.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.muslimmarry.R;
import com.muslimmarry.model.SendGiftItem;


public class SendGiftAdapter extends ArrayAdapter<SendGiftItem> {
	
	Context mContext;
	ArrayList<SendGiftItem> mlst;
	
	public SendGiftAdapter(Context context, int resource,
			ArrayList<SendGiftItem> objects) {
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
			rowView = inflate.inflate(R.layout.row_item_gift, null);
			viewHolder = new ViewHolder();
			viewHolder.gift_box = (ImageView)rowView.findViewById(R.id.gift_box);
			viewHolder.check = (ImageView)rowView.findViewById(R.id.check);
			rowView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		SendGiftItem item = mlst.get(position);
		viewHolder.gift_box.setImageResource(item.getImage());
		if(mlst.get(position).getState() == true){
			viewHolder.check.setVisibility(View.VISIBLE);
			viewHolder.check.setImageResource(R.drawable.gift_check);
		}else{
			viewHolder.check.setVisibility(View.GONE);
		}
		return rowView;
	}
	
	static class ViewHolder{
		ImageView gift_box;
		ImageView check;
	}
	
}
