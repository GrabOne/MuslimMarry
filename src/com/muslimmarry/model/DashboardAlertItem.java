package com.muslimmarry.model;

public class DashboardAlertItem {
	private String id;
	private long time;
	private int status;
	private String gift;
	private String user_id;
	private String username_send;
	private String photo;
	
	public DashboardAlertItem(String id, long time, int status, String gift, String user_id, String username_send, String photo){
		this.id = id;
		this.time = time;
		this.status = status;
		this.gift = gift;
		this.user_id = user_id;
		this.username_send = username_send;
		this.photo = photo;
	}
	
	// settor
	public void setId(String id){
		this.id = id;
	}
	
	public void setTime(long time){
		this.time = time;
	}
	
	public void setStatus(int status){
		this.status = status;
	}
	
	public void setGift(String gift){
		this.gift = gift;
	}
	
	public void setUserId(String user_id){
		this.user_id = user_id;
	}
	
	public void setUsernameSend(String username_send){
		this.username_send = username_send;
	}
	
	public void setPhoto(String photo){
		this.photo = photo;
	}
	
	// gettor
	public String getId(){
		return id;
	}
	
	public long getTime(){
		return time;
	}
	
	public int getStatus(){
		return status;
	}
	
	public String getGift(){
		return gift;
	}
	
	public String getUserId(){
		return user_id;
	}
	
	public String getUsernameSend(){
		return username_send;
	}
	
	public String getPhoto(){
		return photo;
	}
}
