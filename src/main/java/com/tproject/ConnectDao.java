package com.tproject;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * データベースに接続するためのDaoクラス
 * @author no_known
 *
 */
public class ConnectDao extends AbstractDao implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3235877343850600686L;
	private static ConnectDao dao = new ConnectDao();
	
	private ConnectDao() {
		// TODO Auto-generated constructor stub

		try {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			
			connect = DriverManager.getConnection("jdbc:oracle:thin:@150.89.234.14:1521:orcl",
						"scott", "tiger");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	/**
	 * 確立されているセッションを切断する
	 */
	public void disconnect() {
		// TODO Auto-generated method stub
		try {
			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ConnectDao getInstance() {
		return dao;
	}

}
