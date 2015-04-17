package com.muslimmarry.model;


public class DashBoardMessageItem {
	private String id;
	private long time;
	private int status;
	private String content;
	private String user_id;
	private String username_send;
	private String photo;
	private int numMes; 
	public DashBoardMessageItem(String id, long time, int status, String content, String user_id, String username_send, String photo, int numMes){
		this.id = id;
		this.time = time;
		this.status = status;
		this.content = content;
		this.user_id = user_id;
		this.username_send = username_send;
		this.photo = photo;
		this.numMes = numMes;
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
	
	public void setContent(String content){
		this.content = content;
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
	
	public void setNumMes(int numMes){
		this.numMes = numMes;
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
	
	public String getContent(){
		return content;
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
	
	public int getNumMes(){
		return numMes;
	}
}
