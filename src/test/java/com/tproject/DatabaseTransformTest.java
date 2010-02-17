package com.tproject;

import org.apache.wicket.PageParameters;

import junit.framework.TestCase;

/**
 * 
 * @author no_known
 *
 */
public class DatabaseTransformTest extends TestCase {

	ConnectDao dao;
	PageParameters param;
	
	@Override
	public void setUp(){
		// TODO Auto-generated method stub
		
		dao = ConnectDao.getInstance();
		param = new PageParameters();
		
		param.put("database", "database");
		param.put("user", "ikemotot");
		
		dao.insert("insert into system.users values ('ikemotot', 'password')");
		dao.insert("insert into system.databases values ('00001', 'database', 'ikemotot', '09-01-01', '09-01-01')");
		dao.insert("insert into system.tables values ('0000001', 'emp0', '00001')");
		dao.insert("insert into system.tables values ('0000002', 'dept0', '00001')");
		dao.insert("alter table emp0 modify ename default 'sam'");
		dao.insert("alter table emp0 add check(sal > 100)");
	}
	
	@Override
	protected void tearDown() {
		// TODO Auto-generated method stub
		ResultStruct rs = dao.read("select constraint_name from user_constraints natural join user_cons_columns where table_name = 'EMP0' and column_name = 'SAL'");
		String constname = rs.getResult().get(0).getDataList().get(0);
		
		dao.insert("alter table emp0 drop constraint "+constname);
		dao.insert("alter table emp0 modify ename default null");
		dao.insert("delete from system.tables where table_id='0000002' and table_name='dept0'");
		dao.insert("delete from system.tables where table_id='0000001' and table_name='emp0'");
		dao.insert("delete from system.databases where database_id='00001' and database_name='database'");
		dao.insert("delete from system.users where user_name='ikemotot'");
		
//		dao.disconnect();
	}
	
	/**
	 * DatabaseStructureへの変換が正常に動作するかのテスト
	 */
	public void testTransform() {
		
		DatabaseStructure ds;
		
		ds = DatabaseTransform.toDatabaseStructure(dao, param.getString("user"), param.getString("database"));
		
		assertEquals(ds.getDatabaseName(), "database");
		assertEquals(ds.getTables().get(0).getTableName(), "emp0");
		assertEquals(ds.getTables().get(0).getTableId(), "0000001");
		assertEquals(ds.getTables().get(1).getSchemas().get(0).getName(), "deptno");
		assertEquals(ds.getTables().get(1).getSchemas().get(0).getLength(), new Integer(2));
		assertEquals(ds.getTables().get(1).getSchemas().get(0).getPrecisionLength(), new Integer(0));
		assertEquals(ds.getTables().get(0).getSchemas().get(5).getName(), "sal");
		assertEquals(ds.getTables().get(0).getSchemas().get(5).getLength(), new Integer(7));
		assertEquals(ds.getTables().get(0).getSchemas().get(5).getPrecisionLength(), new Integer(2));
		
		assertEquals(ds.getTables().get(0).getSchemas().get(0).getTableName(), "emp0");

	}
	
	/**
	 * コンストラクタが正常に動作するかのテスト
	 */
	@SuppressWarnings("static-access")
	public void testConstractor() {
		DatabaseTransform dt = new DatabaseTransform();
		
		DatabaseStructure  ds = dt.toDatabaseStructure(dao, param.getString("user"), param.getString("database"));
		
		assertEquals(ds.getDatabaseName(), "database");
		assertEquals(ds.getTables().get(0).getTableName(), "emp0");
		assertEquals(ds.getTables().get(0).getTableId(), "0000001");
		
		assertEquals(ds.getTables().get(0).getSchemas().get(0).getTableName(), "emp0");
	}
	
	/**
	 * ColumnConstraintへの変換テスト
	 */
	public void testTransformConstraint() {
		ColumnConstraint cc = DatabaseTransform.toColumnConstraint(dao, "emp0", "empno");
		
		assertNull(cc.getPrimaryConstName());
		assertFalse(cc.isPrimary());
		assertNull(cc.getCheck());
		assertNull(cc.getCheckConstName());
		assertTrue(cc.isNotnull());
		assertEquals(cc.getIndex(), IndexTypes.NORMAL);
		
		dao.insert("alter table emp0 add constraint emp_fk foreign key (deptno) references dept0 (deptno) on delete cascade");
		cc = null;
		cc = DatabaseTransform.toColumnConstraint(dao, "emp0", "deptno");
		
		assertEquals(cc.getForeign().getConstraintName(), "EMP_FK");
		assertEquals(cc.getForeign().getDeleteType(), "CASCADE");
		assertEquals(cc.getForeign().getReferenceConstraintName(), "PK_DEPT0");
		assertEquals(cc.getForeign().getReferenceTable(), "DEPT0");
		assertEquals(cc.getForeign().getReferenceTableColumn(), "DEPTNO");
		assertEquals(cc.getIndex(), IndexTypes.NONE);
		
		dao.insert("alter table emp0 drop constraint emp_fk");
	
		cc = null;
		cc = DatabaseTransform.toColumnConstraint(dao, "emp0", "ename");

		assertEquals(cc.getDefault(), "'sam'");
		
		cc = null;
		cc = DatabaseTransform.toColumnConstraint(dao, "emp0", "empno");
		
		assertEquals(cc.getDefault(), null);
		
		cc = null;
		cc = DatabaseTransform.toColumnConstraint(dao, "dept0", "deptno");
		
		assertEquals(cc.getPrimaryConstName(), "PK_DEPT0");
		assertTrue(cc.isPrimary());
		assertEquals(cc.getIndex(), IndexTypes.NORMAL);
		
		cc = null;
		cc = DatabaseTransform.toColumnConstraint(dao, "emp0", "sal");
		
		assertEquals(cc.getCheck(), "sal > 100");
		assertNotNull(cc.getCheckConstName());
		assertTrue(cc.getCheckConstName().matches("SYS_.+"));
		
		// デフォルト値がない場合は両方null
		cc = null;
		cc = DatabaseTransform.toColumnConstraint(dao, "emp0", "ename");
		
		assertNull(cc.getCheck());
		assertNull(cc.getCheckConstName());
	}


}
