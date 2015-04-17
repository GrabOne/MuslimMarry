package com.muslimmarry.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.muslimmarry.R;
import com.muslimmarry.model.MessageItem;

public class MessagingPageAdapter extends ArrayAdapter<MessageItem> {
	
	private RelativeLayout frame;
	private TextView comment;
	private TextView time;
	private ArrayList<MessageItem> comments = new ArrayList<MessageItem>();
	private RelativeLayout wrapper;
	private Context mContext;

	public MessagingPageAdapter(Context context, int textViewResourceId, ArrayList<MessageItem> objects) {
		super(context, textViewResourceId, objects);
		this.mContext = context;
		this.comments = objects;
	}

	public int getCount() {
		return this.comments.size();
	}

	public MessageItem getItem(int index) {
		return this.comments.get(index);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.row_chat_message, parent, false);
		}

		wrapper = (RelativeLayout)row.findViewById(R.id.wrapper);

		MessageItem coment = getItem(position);

		frame = (RelativeLayout)row.findViewById(R.id.frame);
		comment = (TextView) row.findViewById(R.id.comment);
		time = (TextView)row.findViewById(R.id.time);

		comment.setText(coment.comment);
		time.setText(coment.time);

		frame.setBackgroundResource(coment.left ? R.drawable.bubble_gray : R.drawable.bubble_green);
		wrapper.setGravity(coment.left ? Gravity.LEFT : Gravity.RIGHT);

		return row;
	}

	public Bitmap decodeToBitmap(byte[] decodedByte) {
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

}