package com.bean;

public class LoginUser {
	public int flag;
	public String username;
	public String password;
	public String action;
	
	public int getFlag(){
		return flag;
	}
	
	public void setFlag(int flag){
		this.flag = flag;
	}
	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword(){
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getAction(){
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
}
