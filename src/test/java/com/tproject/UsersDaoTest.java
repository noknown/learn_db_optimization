package com.tproject;

import junit.framework.TestCase;

public class UsersDaoTest extends TestCase {
	
	UserDao dao;
	
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		dao = UserDao.newInstance("scott", "tiger");
		
	}
	
	/**
	 * データベースの接続切断処理のテスト
	 */
	public void testDisconnect() {
		ResultStruct rs = dao.read("select empno from emp0");
		
		assertNotNull(rs);
		
		dao.disconnect();
		rs = dao.read("select empno from emp0");

		assertNull(rs);
	}


}
