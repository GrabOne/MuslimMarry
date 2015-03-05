package com.muslimmarry.item;

public class DashBoardMessageItem {
	private int avatar;
	private String name;
	private String mes_qty;
	private String message;
	private String time;
	private String state;
	public DashBoardMessageItem(int avatar, String name, String mes_qty, String message, String time, String state){
		this.avatar = avatar;
		this.name = name;
		this.mes_qty = mes_qty;
		this.message = message;
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
	
	public String getMesQty(){
		return mes_qty;
	}
	public void setMesQty(String mes_qty){
		this.mes_qty = mes_qty;
	}
	
	public String getMessage(){
		return message;
	}
	public void setMessage(String message){
		this.message = message;
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
