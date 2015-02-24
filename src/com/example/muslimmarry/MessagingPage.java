package com.example.muslimmarry;

import java.util.HashMap;

import com.example.muslimmarry.sharedpref.prefUser;
import com.squareup.picasso.Picasso;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;


public class MessagingPage extends Fragment {
	
	private MessageArrayAdapter adapter;
	
	ScrollView scroll;
	ViewGroup message_content;
	EditText etmessage;
	Button btnSend;
    private ListView lvMsg;
    ImageView large_img;
    
    prefUser user;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.messaging_page, container, false);		
		TextView name = (TextView)rootView.findViewById(R.id.name);
		large_img = (ImageView)rootView.findViewById(R.id.large_img);
		message_content = (ViewGroup)rootView.findViewById(R.id.message_content);
		etmessage = (EditText)rootView.findViewById(R.id.etmessage);
		btnSend = (Button)rootView.findViewById(R.id.btnSend);
        lvMsg = (ListView) rootView.findViewById(R.id.listMessage);
        adapter = new MessageArrayAdapter(getActivity(), R.layout.list_item_chat_message);
        lvMsg.setAdapter(adapter);
		ImageView back = (ImageView)rootView.findViewById(R.id.back);
		rootView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		((MainActivity)getActivity()).setBgGroupOriginal();
		((MainActivity)getActivity()).setFontTypeText(name);
		
		// create user object
		user = new prefUser(getActivity());
		HashMap<String, String> user_info = user.getUserDetail();
		if(user_info.get(prefUser.KEY_AVATAR).length() > 0){
			Picasso.with(getActivity()).load(user_info.get(prefUser.KEY_AVATAR)).fit().centerCrop().into(large_img);
		}
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((MainActivity)getActivity()).hideKeyboard();
				getFragmentManager().popBackStack();
			}
		});
		etmessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    scrollToEndList();
	            }else{
	                scrollToEndList();
	            }
            }
        });
		btnSend.setOnClickListener(new OnClickListener() {
			
	        @Override
	        public void onClick(View arg0) {
	            // TODO Auto-generated method stub
	            if (etmessage.getText().toString().length() > 0) {
	
	                // Them Item vao khung chat. Xac dinh left de hien thi doan chat cua tung nguoi
	                addItems(true, etmessage.getText().toString());
	                etmessage.setText("");
	            }
	        }
	    });
		
		return rootView;
	}
	private void scrollToEndList(){
        if(adapter==null || adapter.getCount() < 1){
            Log.d("Scroll List", "Adapter null or count less than 1");
            return;
        }
        lvMsg.smoothScrollToPosition(adapter.getCount());
    }
    private void addItems(boolean left, String txt) {
        adapter.add(new MessageItem(false, txt));
        adapter.add(new MessageItem(true, txt));
    }
}
