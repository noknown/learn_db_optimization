package com.tproject;

import java.io.Serializable;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 各ユーザのために利用されるDao
 * @author no_known
 *
 */
public class UserDao extends AbstractDao implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 344117080697163736L;

	private UserDao(String user, String pass) {
		try {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			
			connect = DriverManager.getConnection("jdbc:oracle:thin:@150.89.234.14:1521:orcl",
					user, pass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void disconnect() {
		// TODO Auto-generated method stub
			try {
				connect.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static UserDao newInstance(String user, String pass) {
		return new UserDao(user, pass);
	}

}
