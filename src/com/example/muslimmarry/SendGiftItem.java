package com.example.muslimmarry;

public class SendGiftItem {
	int image;
	boolean state;
	public SendGiftItem(int image, boolean state){
		this.image = image;
		this.state = state;
	}
	
	public int getImage(){
		return image;
	}
	
	public void setImage(int image){
		this.image = image;
	}
	
	public boolean getState(){
		return state;
	}
	
	public void setState(boolean state){
		this.state = state;
	}
}
