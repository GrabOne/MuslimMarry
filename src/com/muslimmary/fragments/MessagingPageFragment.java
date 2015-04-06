package com.muslimmary.fragments;

import java.net.URISyntaxException;
import java.util.HashMap;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.muslimmarry.R;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.muslimmarry.activities.MainActivity;
import com.muslimmarry.adapters.MessagingPageAdapter;
import com.muslimmarry.helpers.helpers;
import com.muslimmarry.model.MessageItem;
import com.muslimmarry.sharedpref.prefUser;
import com.squareup.picasso.Picasso;


public class MessagingPageFragment extends Fragment implements OnClickListener {
	
	private MessagingPageAdapter adapter;
	
	ScrollView scroll;
	ViewGroup message_content;
	EditText etmessage;
	Button btnSend;
    private ListView lvMsg;
    ImageView large_img;
    
    prefUser user;
    String userid = "";
    String token = "";
    
    Socket socket;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_messaging_page, container, false);
		helpers.setTouch(rootView);
		
		TextView name = (TextView)rootView.findViewById(R.id.name);
		large_img = (ImageView)rootView.findViewById(R.id.large_img);
		message_content = (ViewGroup)rootView.findViewById(R.id.message_content);
		etmessage = (EditText)rootView.findViewById(R.id.etmessage);
		btnSend = (Button)rootView.findViewById(R.id.btnSend);
        lvMsg = (ListView) rootView.findViewById(R.id.listMessage);
        adapter = new MessagingPageAdapter(getActivity(), R.layout.row_chat_message);
        lvMsg.setAdapter(adapter);
		ImageView back = (ImageView)rootView.findViewById(R.id.back);
		
		// set event for element
		back.setOnClickListener(this);
		btnSend.setOnClickListener(this);
		
		// set background for bottom nav element
		((MainActivity)getActivity()).setBgGroupOriginal();
		
		// set font for element
		new helpers(getActivity()).setFontTypeText(name);
		
		// create user object
		user = new prefUser(getActivity());
		HashMap<String, String> user_info = user.getUserDetail();
		userid = user_info.get(prefUser.KEY_USERID);
		token = user_info.get(prefUser.KEY_TOKEN);
		
		// display photo url
		if(user_info.get(prefUser.KEY_PHOTO).length() > 0){
			Picasso.with(getActivity()).load(user_info.get(prefUser.KEY_PHOTO)).fit().centerCrop().into(large_img);
		}
		
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
    @Override
    public void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	((MainActivity)getActivity()).showTopNav(false);
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			((MainActivity)getActivity()).hideKeyboard();
			getFragmentManager().popBackStack();
			break;
		case R.id.btnSend:
			if (etmessage.getText().toString().length() > 0) {
				
                // Them Item vao khung chat. Xac dinh left de hien thi doan chat cua tung nguoi
                addItems(true, etmessage.getText().toString());
                etmessage.setText("");
                
                try{
                	socket = IO.socket("http://campcoders.com:7777/chat?user_id="+ userid +"&token=" + token);
                	
                	socket.connect();
                }catch(URISyntaxException e){
                	e.printStackTrace();
                }
            }
		default:
			break;
		}
	}
}
