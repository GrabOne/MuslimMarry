package com.muslimmarry.model;

public class DashboardAlertItem {
	private int avatar;
	private String name;
	private String alert;
	private String time;
	private String state;
	
	public DashboardAlertItem(int avatar, String name, String alert, String time, String state){
		this.avatar = avatar;
		this.name = name;
		this.alert = alert;
		this.time = time;
		this.state = state;
	}
	
	public int getAvatar(){
		return avatar;
	}
	
	public void setAvatar(int avatar){
		this.avatar = avatar;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	public String getAlert(){
		return alert;
	}
	public void setAlert(String alert){
		this.alert = alert;
	}
	
	public String getTime(){
		return time;
	}
	public void setTime(String time){
		this.time = time;
	}
	
	public String getState(){
		return state;
	}
	public void setState(String state){
		this.state = state;
	}
}
