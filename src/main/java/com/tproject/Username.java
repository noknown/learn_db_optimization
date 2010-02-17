package com.tproject;

import java.io.Serializable;

public class Username implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Input name of user
	private String username="Input name of user";
	private boolean usercheck = false;
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}

	public boolean isUsercheck() {
		return usercheck;
	}

	public void setUsercheck(boolean usercheck) {
		this.usercheck = usercheck;
	}
}
