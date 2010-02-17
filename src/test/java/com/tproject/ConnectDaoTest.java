package com.tproject;

import java.sql.SQLException;

import junit.framework.TestCase;
/**
 * 
 * @author no_known and Oh
 *
 */
public class ConnectDaoTest extends TestCase {
	ConnectDao dao;
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		dao = ConnectDao.getInstance();
	}

	/**
	 * ConnectDaoのreadメソッドが値を受け取り適切な結果を返すかをテスト
	 */
	public void testRead() {
		
		ResultStruct rs;
		
		rs = dao.read("select empno from emp0");
		
		assertNotNull(rs);
		
		String str = "EMPNO";
		String[] strs = {
				"7369",
				"7499",
				"7521",
				"7566",
				"7654",
				"7698",
				"7782",
				"7788",
				"7839",
				"7844",
				"7876",
				"7900",
				"7902",
				"7934"
		};
		
		assertEquals(rs.getSchema().getDataList().get(0), str);
		for(int i = 0; i < rs.getResult().size(); i++) {
			assertEquals(rs.getResult().get(i).getDataList().get(0), strs[i]);
		}
		
	}

	public void testInsert(){
		
		dao.insert("insert into system.users values ('oh','ikemoto1')");
		ResultStruct r = null;
		r = dao.read("select * from system.users where user_name='oh'");
		assertNotNull(r);
//		try {
//			while(((ResultSet) r).next()){	
				for(int i = 0; i < r.getResult().size(); i++) {
					assertEquals(r.getResult().get(i).getDataList().get(0), "oh");
					assertEquals(r.getResult().get(i).getDataList().get(1), "ikemoto1");
//						assertEquals("oh",((ResultSet) r).getString(1));
//						assertEquals("ikemoto", ((ResultSet) r).getString(2));
			}
			
			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			fail();
//		}finally{
		dao.insert("delete from system.users where user_name='oh'");
//		}
		
	}
	public void testSuccessMassage(){
		boolean a,b;
		
		a = dao.insert("insert into system.users (user_name,password) values ('oh','ikemotoii')");
		assertTrue(a);
		b = dao.insert("insert into system.users (user_name,password) values ('oh','ikemotoii')");
		assertFalse(b);
		
		dao.insert("delete from system.users where user_name='oh'");
		
		
	}
	


	/**
	 * readメソッドにSQL文以外の値が与えられた場合、nullを返すことをテスト
	 */
	public void testReadResultNull() {
		ResultStruct s;
				
		s = dao.read("sasda; asdjaeiifahi uiswhea");
		
		assertNull(s);
		
	}

}
