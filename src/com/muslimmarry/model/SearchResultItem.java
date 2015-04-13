package com.muslimmarry.model;

public class SearchResultItem {
	private String id;
	private String name;
	private String username;
	private String occupation;
	private String age;
	private String photo;
	private String birthday;
	private String email;
	private String height;
	private String language;
	private String promocode;
	private String country;
	private String city;
	private String lat;
	private String lng;
	private double distance;
	private boolean click = false;
	
	public SearchResultItem(String id, String name, String username, String occupation, String age, String photo, String birthday, String email,
			String height, String language, String promocode, String country, String city, String lat, String lng, double distance, boolean click){
		this.id = id;
		this.name = name;
		this.username = username;
		this.occupation = occupation;
		this.age = age;
		this.photo = photo;
		this.birthday = birthday;
		this.email = email;
		this.height = height;
		this.language = language;
		this.promocode = promocode;
		this.country = country;
		this.city = city;
		this.lat = lat;
		this.lng = lng;
		this.distance = distance;
		this.click = click;
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getOccupation(){
		return occupation;
	}
	
	public void setOccupation(String occupation){
		this.occupation = occupation;
	}
	
	public String getAge(){
		return age;
	}
	
	public void setAge(String age){
		this.age = age;
	}
	
	public String getPhoto(){
		return photo;
	}
	
	public void setPhoto(String photo){
		this.photo = photo;
	}
	
	public String getBirthday(){
		return birthday;
	}
	
	public void setBirthday(String birthday){
		this.birthday = birthday;
	}
	
	public String getEmail(){
		return email;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	
	public String getHeight(){
		return height;
	}
	
	public void setHeight(String height){
		this.height = height;
	}
	
	public String getLanguage(){
		return language;
	}
	
	public void setLanguage(String language){
		this.language = language;
	}
	
	public String getPromocode(){
		return promocode;
	}
	
	public void setPromocde(String promocode){
		this.promocode = promocode;
	}
	
	public String getCountry(){
		return country;
	}
	
	public void setCountry(String country){
		this.country = country;
	}
	
	public String getCity(){
		return city;
	}
	
	public void setCity(String city){
		this.city = city;
	}
	
	public String getLat(){
		return lat;
	}
	
	public void setLat(String lat){
		this.lat = lat;
	}
	
	public String getLng(){
		return lng;
	}
	
	public void setLng(String lng){
		this.lng = lng;
	}
	
	public double getDistance(){
		return distance;
	}
	
	public void setDistance(double distance){
		this.distance = distance;
	}
	
	public boolean getClick(){
		return click;
	}
	
	public void setClick(boolean click){
		this.click = click;
	}
	
}
