package com.tproject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao implements IDao {
	
	protected Connection connect;

	public abstract void disconnect();

	public boolean insert(String sql) {
		// TODO Auto-generated method stub
		Statement stm = null;
		
		try {
			stm = connect.createStatement();
			
			stm.executeUpdate(sql);
			
			stm.close();
			return true;						
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			
			try {
				if(stm != null) stm.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				System.err.println("internal error insert");
				e1.printStackTrace();
				return false;
			}
			
			if(e.getMessage().indexOf("一意制約")!= -1){
				return false;
			}
			return true;
		}
	}

	/**
	 * 値を返すSQL文を実行するためのメソッド。SQLが実行できなかった場合はnullを返す
	 * @param sql 実行させるSQL文
	 * @return SQL文の実行結果の入ったResultStruct
	 */
	public ResultStruct read(String sql) {
		// TODO Auto-generated method stub
		Statement stm = null;
		ResultSet rs = null;
		ResultStruct rst = new ResultStruct();
		
	
		try {
		
			stm = connect.createStatement();

			rs = stm.executeQuery(sql);
			
			int size = rs.getMetaData().getColumnCount();

			// 列の情報
			ResultSetMetaData rsmd = rs.getMetaData();
			String[] sts = new String[size];
			for(int i = 1; i <= size; i++) {
				sts[i-1] = rsmd.getColumnName(i);
			}
			rst.setSchema(new ResultData(sts));
			
			List<ResultData> rdlist = new ArrayList<ResultData>();
			// データ
			while(rs.next()) {
				String[] strs = new String[size];
				for(int i = 1; i <= size; i++) {
					strs[i-1] = rs.getString(i);
				}
				
				rdlist.add(new ResultData(strs));
			}
			rst.setResult(rdlist);
			
			rs.close();
			stm.close();
			
			return rst;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			try {
				if(rs != null) rs.close();
				if(stm != null) stm.close();
			} catch(SQLException ee) {
				System.err.println("internal error read");
				ee.printStackTrace();
				return null;
			}
			
			return null;
		}
	}

}
