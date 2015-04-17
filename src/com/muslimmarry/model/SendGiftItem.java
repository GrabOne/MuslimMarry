package com.muslimmarry.model;

public class SendGiftItem {
	String image;
	boolean state;
	public SendGiftItem(String image, boolean state){
		this.image = image;
		this.state = state;
	}
	
	public String getImage(){
		return image;
	}
	
	public void setImage(String image){
		this.image = image;
	}
	
	public boolean getState(){
		return state;
	}
	
	public void setState(boolean state){
		this.state = state;
	}
}
