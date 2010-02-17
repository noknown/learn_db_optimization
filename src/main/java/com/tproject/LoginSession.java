package com.tproject;


import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebSession;

import com.tproject.ConnectDao;
import com.tproject.ResultStruct;


/**
 * 
 * 
 */
@SuppressWarnings("serial")
public final class LoginSession extends WebSession
{
	
	private String userName;
	private String password;
	@SuppressWarnings("deprecation")
	public LoginSession(Request request)
	{
		super(request);
	}

	public final boolean authenticate(final String userName, final String password)
	{   
		ConnectDao s = ConnectDao.getInstance();
		ResultStruct rs= s.read("select * from system.users where user_name = '"+userName+"'");
		String u_n=null;
		String p_w=null;
		if(!rs.isEmpty()){
			u_n = rs.getResult().get(0).getDataList().get(0);
			p_w = rs.getResult().get(0).getDataList().get(1);
		}
		if(u_n != null){
			if (this.userName == null) {
				if ((u_n.equals(userName) && p_w.equals(password))) {
					this.userName = userName;
					this.password = password;
				}
			}
		}

		return (this.userName != null);	
	}

	
	public String getUserName(){
		return this.userName;
	}
	public String getPassword() {
		return this.password;
	}
	public final boolean isLoggedIn()
	{
		return (userName != null);
	}

}

