package com.example.muslimmarry;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpeakDialogAdapter extends BaseAdapter {
	
	ArrayList<GetSpeaks> mlst;
	Context mContext;
	LayoutInflater inflater;
	
	public SpeakDialogAdapter(Context context, ArrayList<GetSpeaks> mlst){
		super();
		this.mContext = context;
		this.mlst = mlst;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlst.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mlst.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView == null){
			inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.dialog_speak_row, null);
			viewHolder = new ViewHolder();
			viewHolder.txt = (TextView)convertView.findViewById(R.id.txt);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		GetSpeaks item = mlst.get(position);
		viewHolder.txt.setText(item.getValue().substring(0, 1).toUpperCase() + item.getValue().substring(1));
		return convertView;
	}
	private class ViewHolder {
        TextView txt;
	}
}
