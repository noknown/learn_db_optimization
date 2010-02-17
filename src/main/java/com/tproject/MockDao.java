package com.tproject;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * テストのためのDaoクラス
 * @author no_known
 *
 */
public class MockDao implements IDao, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResultStruct read(String inputText) {
		// TODO Auto-generated method stub
		ResultStruct rst = new ResultStruct();
		
		MockResultSet rs = new MockResultSet();
		
		try {

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

			return rst;
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	public void connect() {
		// TODO Auto-generated method stub
		
	}

	public boolean insert(String sql) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
