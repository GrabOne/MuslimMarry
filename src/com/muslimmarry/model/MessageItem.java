package com.muslimmarry.model;

public class MessageItem {
	public boolean left;
	public String comment;
	public String time;

	public MessageItem(boolean left, String comment, String time) {
		super();
		this.left = left;
		this.comment = comment;
		this.time = time;
	}
	public boolean getLeft(){
		return left;
	}
	public String getComment(){
		return comment;
	}
	public String getTime(){
		return time;
	}
}