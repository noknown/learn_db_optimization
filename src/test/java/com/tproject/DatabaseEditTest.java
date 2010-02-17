package com.tproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

/**
 * 
 * @author no_known
 *
 */
public class DatabaseEditTest extends TestCase {
	
	DatabaseEdit de;
	IDao dao;
	
	//select * from user_constraints natural join user_cons_columns where table_name = 'EMP0' and column_name = 'DEPTNO'
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		de = new DatabaseEdit();
		

		
		
		dao = ConnectDao.getInstance();
		
		dao.insert("insert into system.users values ('ikemotot', 'password')");
		dao.insert("insert into system.databases values ('00001', 'database', 'ikemotot', '09-01-01', '09-01-01')");
		dao.insert("insert into system.tables values ('0000001', 'emp0', '00001')");
		dao.insert("insert into system.tables values ('0000002', 'dept0', '00001')");
		
		dao.insert("commit");
	}
	
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		dao.insert("delete from system.tables where table_id='0000001' and table_name='emp0'");
		dao.insert("delete from system.tables where table_id='0000002' and table_name='dept0'");
		dao.insert("delete from system.databases where database_id='00001' and database_name='database'");
		dao.insert("delete from system.users where user_name='ikemotot'");

		dao.insert("commit");
//		dao.disconnect();
	}
	
	/**
	 * DifferenceTableの種類がアップデートだった場合のDB書き換えのテスト
	 */
	public void testUpdates() {
		DifferenceTable dt = new DifferenceTable();
		dt.setDifferentChar('U');
		dt.setTableNameFlag(true);
		dt.setTableName("king_cong");
		dt.setObjectId("emp0");
		dt.setTableId("0000001");
		
		List<DifferenceTable> dlist = new ArrayList<DifferenceTable>();
		dlist.add(dt);
		
		//execution
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		ResultStruct rs = dao.read("select table_name from system.tables where table_id='0000001'");

		assertFalse(rs.isEmpty());
		assertEquals(rs.getResult().get(0).getDataList().get(0), "king_cong");
		
		rs = null;
		rs = dao.read("select * from king_cong");
		assertFalse(rs.isEmpty());

		
		dao.insert("alter table king_cong rename to emp0");
		dao.insert("update system.tables set table_name = 'emp0' where table_id = '0000001'");
	}
	
	/**
	 * DifferenceTableの種類が追加だった場合のDB書き換えのテスト
	 * DifferenceTableの種類が削除だった場合のDB書き換えのテスト
	 */
	public void testAddandDelete() {
		//追加
		DifferenceColumn dc = new DifferenceColumn();
		dc.setObjectId("id1");
		dc.setDifferentChar('A');
		dc.setColumnName("new_column1");
		dc.setColumnType("VARCHAR2");
		dc.setColumnLength(20);
		
		DifferenceColumn d2 = new DifferenceColumn();
		d2.setObjectId("id2");
		d2.setDifferentChar('A');
		d2.setColumnName("new_column2");
		d2.setColumnType("VARCHAR2");
		d2.setColumnLength(20);
		d2.setColumnIndex(IndexTypes.NORMAL);
		
		DifferenceTable dt = new DifferenceTable();
		dt.setDifferentChar('A');
		dt.setTableNameFlag(true);
		dt.setTableName("new_table1");
		dt.setObjectId("new_table1:ssed");
		dt.setDifferenceColumns(Arrays.asList(dc, d2));
		
		List<DifferenceTable> dlist = new ArrayList<DifferenceTable>();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		ResultStruct rs = dao.read("select table_name from system.tables " +
				"where database_id = '00001' and table_name = 'new_table1'");
		assertFalse(rs.isEmpty());
		assertEquals(rs.getResult().get(0).getDataList().get(0), "new_table1");
		
		rs = null;
		rs = dao.read("select * from user_tables where table_name = 'NEW_TABLE1'");
		assertFalse(rs.isEmpty());
		
		rs = null;
		rs = dao.read("select column_name from user_tab_columns where table_name = 'NEW_TABLE1'");
		assertFalse(rs.isEmpty());
		try {
			assertEquals(rs.getResult().get(0).getDataList().get(0), "NEW_COLUMN1");
			assertEquals(rs.getResult().get(1).getDataList().get(0), "NEW_COLUMN2");
		} catch(IndexOutOfBoundsException e) {
			fail();
		}
		
		rs = null;
		rs = dao.read("select * from user_ind_columns where table_name = 'NEW_TABLE1' and column_name = 'NEW_COLUMN2'");
		assertFalse(rs.isEmpty());
		
		//削除
		dt = new DifferenceTable();
		dt.setDifferentChar('D');
		dt.setObjectId("new_table1");
		
		dlist.clear();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		rs = null;
		rs = dao.read("select table_name from system.tables where database_id = '00001' and table_name = 'new_table1'");
		assertTrue(rs.isEmpty());
		rs = null;
		rs = dao.read("select * from user_tables where table_name = 'NEW_TABLE1'");
		assertTrue(rs.isEmpty());
		
//		dao.insert("drop table new_table1");
//		dao.insert("delete from system.tables where database_id = '00001' and table_name = 'new_table1'");
	}
	
	/**
	 * DifferenceTable内のDifferenceColumnの種類がアップデートだった場合のDB書き換えテスト
	 * テーブル名の場合
	 */
	public void testUpdateColumn() {
		DifferenceColumn dc = new DifferenceColumn();
		dc.setDifferentChar('U');
		dc.setObjectId("ename");
		
		dc.setColumnName("isname");
		dc.setColumnNameFlag(true);
		
		DifferenceTable dt = new DifferenceTable();
		dt.setDifferentChar('U');
		dt.setObjectId("emp0");
		dt.setDifferenceColumns(Arrays.asList(dc));
		
		List<DifferenceTable> dlist = new ArrayList<DifferenceTable>();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		ResultStruct rs = dao.read("select column_name from user_tab_columns where table_name = 'EMP0' and column_name = 'ISNAME'");
		assertFalse(rs.isEmpty());
		rs = dao.read("select column_name from user_tab_columns where table_name = 'EMP0' and column_name = 'ENAME'");
		assertTrue(rs.isEmpty());
		
		dao.insert("alter table emp0 rename column isname to ename");

	}
	
	/**
	 * アップデートだった場合のテスト。索引の場合
	 */
	public void testUpdateColumnIndex() {
		DifferenceColumn dc = new DifferenceColumn();
		dc.setDifferentChar('U');
		dc.setObjectId("ename");
		
		dc.setColumnIndexFlag(true);
		dc.setColumnIndex(IndexTypes.NORMAL);
		
		DifferenceTable dt = new DifferenceTable();
		dt.setDifferentChar('U');
		dt.setObjectId("emp0");
		dt.setDifferenceColumns(Arrays.asList(dc));
		
		List<DifferenceTable> dlist = new ArrayList<DifferenceTable>();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		ResultStruct rs = dao.read("select * from user_ind_columns where table_name = 'EMP0' and column_name = 'ENAME'");
		assertFalse(rs.isEmpty());
		
		// ビットマップ索引の場合。エンタープライズエディションでなければ有効になっていない。
//		dc.setColumnIndexFlag(true);
//		dc.setColumnIndex(IndexTypes.BITMAP);
//		dt.setDifferenceColumns(Arrays.asList(dc));
//		
//		dlist.clear();
//		dlist.add(dt);
//		
//		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
//		
//		rs = dao.read("select * from user_ind_columns where table_name = 'EMP0' and column_name = 'ENAME'");
//		assertFalse(rs.empty());
//		rs = dao.read("select * from user_indexes where table_name = 'EMP0' and index_type = 'BITMAP'");
//		assertFalse(rs.empty());
		
		// 逆キー索引
		dc.setColumnIndexFlag(true);
		dc.setColumnIndex(IndexTypes.REVERSE);
		dt.setDifferenceColumns(Arrays.asList(dc));
		
		dlist.clear();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		rs = dao.read("select * from user_ind_columns where table_name = 'EMP0' and column_name = 'ENAME'");
		assertFalse(rs.isEmpty());
		rs = dao.read("select * from user_indexes where table_name = 'EMP0' and index_type = 'NORMAL/REV'");
		assertFalse(rs.isEmpty());
		
		// 元にもどす。
		dc.setColumnIndex(IndexTypes.NONE);
		dc.setColumnIndexFlag(true);
		dt.setDifferenceColumns(Arrays.asList(dc));
		
		dlist.clear();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		rs = dao.read("select * from user_ind_columns where table_name = 'EMP0' and column_name = 'ENAME'");
		assertTrue(rs.isEmpty());
		
	}
	
	/**
	 * アップデートだった場合のテスト。プライマリキーの場合
	 */
	public void testUpdateColumnPrimary() {
		DifferenceColumn dc = new DifferenceColumn();
		dc.setDifferentChar('U');
		dc.setObjectId("ename");
		
		dc.setColumnPrimary(true);
		dc.setColumnPrimaryFlag(true);
		dc.setColumnPrimaryConstName("PK_CONST");
		dc.setColumnPrimaryConstNameFlag(true);
		
		DifferenceTable dt = new DifferenceTable();
		dt.setDifferentChar('U');
		dt.setObjectId("emp0");
		dt.setDifferenceColumns(Arrays.asList(dc));
		
		List<DifferenceTable> dlist = new ArrayList<DifferenceTable>();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		ResultStruct rs = dao.read("select constraint_type, constraint_name from user_constraints natural join user_cons_columns" +
				" where table_name = 'EMP0' and column_name = 'ENAME' and constraint_type = 'P'");
		assertFalse(rs.isEmpty());
		assertEquals(rs.getResult().get(0).getDataList().get(0), "P");
		
		//元にもどす。
		dc.setColumnPrimary(false);
		dc.setColumnPrimaryFlag(true);
		dc.setColumnPrimaryConstName(rs.getResult().get(0).getDataList().get(1));
		dc.setColumnPrimaryConstNameFlag(false);
		
		dt.setDifferenceColumns(Arrays.asList(dc));
		
		dlist.clear();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		rs =  dao.read("select constraint_type, constraint_name from user_constraints natural join user_cons_columns" +
			" where table_name = 'EMP0' and column_name = 'ENAME' and constraint_type = 'P'");

		assertTrue(rs.isEmpty());
		
	}
	
	/**
	 * アップデートだった場合のテスト。タイプ、長さの場合
	 */
	public void testUpdateColumnLength() {
		DifferenceColumn dc = new DifferenceColumn();
		dc.setDifferentChar('U');
		dc.setObjectId("ename");
		
		dc.setColumnType("NUMBER");
		dc.setColumnTypeFlag(true);
		
		dc.setColumnLength(10);
		dc.setColumnLengthFlag(true);
		
		dc.setColumnPrecisionLength(2);
		dc.setColumnPrecisionLengthFlag(true);
		
		DifferenceTable dt = new DifferenceTable();
		dt.setDifferentChar('U');
		dt.setObjectId("emp0");
		dt.setDifferenceColumns(Arrays.asList(dc));
		
		List<DifferenceTable> dlist = new ArrayList<DifferenceTable>();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		ResultStruct rs = dao.read("select data_type, data_precision, data_scale from user_tab_columns where table_name = 'EMP0' and column_name = 'ENAME'");
		assertFalse(rs.isEmpty());
		assertEquals(rs.getResult().get(0).getDataList().get(0), "NUMBER");
		assertEquals(rs.getResult().get(0).getDataList().get(1), "10");
		assertEquals(rs.getResult().get(0).getDataList().get(2), "2");
		
		dc.setColumnType("VARCHAR2");
		dc.setColumnTypeFlag(true);
		
		dc.setColumnLength(10);
		dc.setColumnLengthFlag(true);
		
		dt.setDifferenceColumns(Arrays.asList(dc));
		dlist.clear();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		rs = dao.read("select data_type, data_length from user_tab_columns where table_name = 'EMP0' and column_name = 'ENAME'");
		assertFalse(rs.isEmpty());
		assertEquals(rs.getResult().get(0).getDataList().get(0), "VARCHAR2");
		assertEquals(rs.getResult().get(0).getDataList().get(1), "10");
		
//		dao.insert("alter table emp0 modify (ename VARCHAR2(10))");
		dao.insert("update emp0 set ENAME = (select ENAME from emp where emp.empno = emp0.empno)");
	}
	
	/**
	 * アップデートだった場合のテスト。デフォルトの場合
	 */
	public void testUpdateColumnDefault() {
		DifferenceColumn dc = new DifferenceColumn();
		dc.setDifferentChar('U');
		dc.setObjectId("ename");
		
		dc.setColumnDefault("nameof");
		dc.setColumnDefaultFlag(true);
		dc.setColumnType("VARCHAR2");
		
		DifferenceTable dt = new DifferenceTable();
		dt.setDifferentChar('U');
		dt.setObjectId("emp0");
		dt.setDifferenceColumns(Arrays.asList(dc));
		
		List<DifferenceTable> dlist = new ArrayList<DifferenceTable>();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		ResultStruct rs = dao.read("select data_default from user_tab_columns where table_name='EMP0' and column_name = 'ENAME'");
		assertFalse(rs.isEmpty());
		assertEquals(rs.getResult().get(0).getDataList().get(0), "'nameof'");
		
		// 戻す
		dc.setColumnDefault(null);
		dc.setColumnDefaultFlag(true);
		
		dt.setDifferenceColumns(Arrays.asList(dc));
		dlist.clear();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		rs = dao.read("select data_default from user_tab_columns where table_name='EMP0' and column_name = 'ENAME'");
		assertFalse(rs.isEmpty());
		assertEquals(rs.getResult().get(0).getDataList().get(0), "null");
		
//		dao.insert("alter table emp0 modify ename default null");
	}
	
	/**
	 * アップデートだった場合のテスト。Unique規約の場合
	 */
	public void testUpdateColumnUnique() {
		DifferenceColumn dc = new DifferenceColumn();
		dc.setDifferentChar('U');
		dc.setObjectId("ename");
		
		dc.setColumnUnique(true);
		dc.setColumnUniqueFlag(true);
		
		DifferenceTable dt = new DifferenceTable();
		dt.setDifferentChar('U');
		dt.setObjectId("emp0");
		dt.setDifferenceColumns(Arrays.asList(dc));
		
		List<DifferenceTable> dlist = new ArrayList<DifferenceTable>();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		ResultStruct rs = dao.read("select constraint_type from user_constraints natural join user_cons_columns" +
		" where table_name = 'EMP0' and column_name = 'ENAME'");
		assertFalse(rs.isEmpty());
		assertEquals(rs.getResult().get(0).getDataList().get(0), "U");
		
		dc.setColumnUnique(false);
		dc.setColumnUniqueFlag(true);
		
		dt.setDifferenceColumns(Arrays.asList(dc));
		dlist.clear();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		rs = dao.read("select constraint_type from user_constraints natural join user_cons_columns" +
		" where table_name = 'EMP0' and column_name = 'ENAME'");
		assertTrue(rs.isEmpty());
		
//		dao.insert("alter table emp0 drop unique (ename)");
		
	}
	
	/**
	 * アップデートだった場合のテスト。NotNull規約の場合
	 */
	public void testUpdateColumnNotNull() {
		DifferenceColumn dc = new DifferenceColumn();
		dc.setDifferentChar('U');
		dc.setObjectId("ename");
		
		dc.setColumnNotNull(true);
		dc.setColumnNotNullFlag(true);
		
		DifferenceTable dt = new DifferenceTable();
		dt.setDifferentChar('U');
		dt.setObjectId("emp0");
		dt.setDifferenceColumns(Arrays.asList(dc));
		
		List<DifferenceTable> dlist = new ArrayList<DifferenceTable>();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		ResultStruct rs = dao.read("select constraint_type from user_constraints natural join user_cons_columns" +
		" where table_name = 'EMP0' and column_name = 'ENAME' ");
		assertFalse(rs.isEmpty());
		assertEquals(rs.getResult().get(0).getDataList().get(0), "C");
		
		// 元に戻す
		dc.setColumnNotNull(false);
		dc.setColumnNotNullFlag(true);
		
		dt.setDifferenceColumns(Arrays.asList(dc));
		dlist.clear();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		rs = dao.read("select constraint_type from user_constraints natural join user_cons_columns" +
		" where table_name = 'EMP0' and column_name = 'ENAME' ");
		assertTrue(rs.isEmpty());
		
//		dao.insert("alter table emp0 modify (ename null)");
	}
	
	/**
	 * アップデートだった場合のテスト。Check規約の場合
	 */
	public void testUpdateColumnCheck() {
		DifferenceColumn dc = new DifferenceColumn();
		dc.setDifferentChar('U');
		dc.setObjectId("sal");
		
		dc.setColumnCheck("sal > 1");
		dc.setColumnCheckFlag(true);
		
		DifferenceTable dt = new DifferenceTable();
		dt.setDifferentChar('U');
		dt.setObjectId("emp0");
		dt.setDifferenceColumns(Arrays.asList(dc));
		
		List<DifferenceTable> dlist = new ArrayList<DifferenceTable>();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		ResultStruct rs = dao.read("select search_condition, constraint_name from user_constraints natural join user_cons_columns where table_name = 'EMP0' and column_name = 'SAL'");
		
		assertFalse(rs.isEmpty());
		assertEquals(rs.getResult().get(0).getDataList().get(0), "sal > 1");
		
		try {
			rs.getResult().get(1);
			fail();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		
		// Check制約の内容を"fixed > 100"に変更
		String const_name = rs.getResult().get(0).getDataList().get(1);
		dc.setColumnCheckFlag(true);
		dc.setColumnCheck("sal > 100");
		dc.setColumnCheckConst(const_name);
		
		dt.setDifferenceColumns(Arrays.asList(dc));
		
		dlist.clear();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		rs = dao.read("select search_condition, constraint_name from user_constraints natural join user_cons_columns where table_name = 'EMP0' and column_name = 'SAL'");
		assertFalse(rs.isEmpty());
		assertEquals(rs.getResult().get(0).getDataList().get(0), "sal > 100");
		try {
			rs.getResult().get(1);
			fail();
		} catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		
		// 元に戻す。
		dc.setColumnCheckFlag(true);
		dc.setColumnCheck(null);
		dc.setColumnCheckConst(const_name);
		
		dt.setDifferenceColumns(Arrays.asList(dc));
		dlist.clear();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		rs = dao.read("select search_condition, constraint_name from user_constraints natural join user_cons_columns where table_name = 'EMP0' and column_name = 'SAL'");
		assertTrue(rs.isEmpty());
		
//		dao.insert("alter table emp0 drop constraint "+const_name);
	}
	
	/**
	 * アップデートだった場合のテスト。外部参照規約の場合
	 */
	public void testUpdateColumnForeign() {
		DifferenceColumn dc = new DifferenceColumn();
		dc.setDifferentChar('U');
		dc.setObjectId("deptno");
		
		dc.setColumnForeignName("EMP_FK");
		dc.setColumnReferenceTable("DEPT0");
		dc.setColumnReferenceConstraintName("PK_DEPT0");
		dc.setColumnReferenceTableColumn("DEPTNO");
		dc.setColumnDeleteType("CASCADE");
		dc.setColumnForeignFlag(true);
		
		DifferenceTable dt = new DifferenceTable();
		dt.setDifferentChar('U');
		dt.setObjectId("emp0");
		dt.setDifferenceColumns(Arrays.asList(dc));
		
		List<DifferenceTable> dlist = new ArrayList<DifferenceTable>();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		ResultStruct rs = dao.read("select constraint_type, r_constraint_name, delete_rule from user_constraints" +
				" where table_name = 'EMP0' and constraint_name = 'EMP_FK'");
		assertFalse(rs.isEmpty());
		assertEquals(rs.getResult().get(0).getDataList().get(0), "R");
		assertEquals(rs.getResult().get(0).getDataList().get(1), "PK_DEPT0");
		assertEquals(rs.getResult().get(0).getDataList().get(2), "CASCADE");
		rs = dao.read("select table_name from user_constraints where constraint_name = 'PK_DEPT0'");
		assertFalse(rs.isEmpty());
		assertEquals(rs.getResult().get(0).getDataList().get(0), "DEPT0");
		
		// もどす
		dc.setColumnForeignName("EMP_FK");
		dc.setColumnReferenceTable(null);
		dc.setColumnForeignFlag(true);
		
		dt.setDifferenceColumns(Arrays.asList(dc));
		dlist.clear();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		rs = dao.read("select constraint_type, r_constraint_name, delete_rule from user_constraints" +
				" where table_name = 'EMP0' and constraint_name = 'EMP_FK'");
		assertTrue(rs.isEmpty());

	}
	
	/**
	 * 外部参照規約の場合。最初に外部参照を登録する際のテスト。ColumnForeignNameが設定されない。
	 */
	public void testUpdateColumnForeignBegin() {
		DifferenceColumn dc = new DifferenceColumn();
		dc.setDifferentChar('U');
		dc.setObjectId("deptno");
		
		dc.setColumnForeignName(null);
		dc.setColumnReferenceTable("DEPT0");
		dc.setColumnReferenceConstraintName(null);
		dc.setColumnReferenceTableColumn("DEPTNO");
		dc.setColumnDeleteType("CASCADE");
		dc.setColumnForeignFlag(true);
		
		DifferenceTable dt = new DifferenceTable();
		dt.setDifferentChar('U');
		dt.setObjectId("emp0");
		dt.setDifferenceColumns(Arrays.asList(dc));
		
		List<DifferenceTable> dlist = new ArrayList<DifferenceTable>();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		ResultStruct rs = dao.read("select constraint_name from user_cons_columns where " +
				"table_name = 'EMP0' and column_name = 'DEPTNO'");
		assertFalse(rs.isEmpty());
		
		
		// もどす
		dc.setColumnForeignName(rs.getResult().get(0).getDataList().get(0));
		dc.setColumnReferenceTable(null);
		dc.setColumnForeignFlag(true);
		
		dt.setDifferenceColumns(Arrays.asList(dc));
		dlist.clear();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		rs = dao.read("select constraint_name from user_cons_columns where " +
				"table_name = 'EMP0' and column_name = 'DEPTNO'");
		assertTrue(rs.isEmpty());
		
	}
	
	/**
	 * カラム追加、削除のテスト。
	 */
	public void testAddColumn() {
		// 追加
		DifferenceColumn dc = new DifferenceColumn();
		dc.setDifferentChar('A');
		
		dc.setObjectId("truesth");
		dc.setColumnName("columnx");
		dc.setColumnType("VARCHAR2");
		dc.setColumnLength(10);
		dc.setColumnTableName("dept0");
		dc.setColumnIndex(IndexTypes.NORMAL);
		
		DifferenceTable dt = new DifferenceTable();
		dt.setDifferentChar('U');
		dt.setObjectId("dept0");
		dt.setDifferenceColumns(Arrays.asList(dc));
		
		List<DifferenceTable> dlist = new ArrayList<DifferenceTable>();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		ResultStruct rs = dao.read("select column_name, data_type, data_length from user_tab_columns where table_name = 'DEPT0' and column_name = 'COLUMNX'");
		assertFalse(rs.isEmpty());
		assertEquals(rs.getResult().get(0).getDataList().get(0), "COLUMNX");
		assertEquals(rs.getResult().get(0).getDataList().get(1), "VARCHAR2");
		assertEquals(rs.getResult().get(0).getDataList().get(2), "10");
		
		rs = dao.read("select * from user_ind_columns where table_name = 'DEPT0' and column_name = 'COLUMNX'");
		assertFalse(rs.isEmpty());
		
//		dao.insert("alter table emp0 drop column columnx");
		
		dc.setDifferentChar('D');
		dc.setObjectId("columnx");
		
		dt.setDifferenceColumns(Arrays.asList(dc));
		dlist.clear();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		rs = dao.read("select column_name from user_tab_columns where table_name = 'DEPT0' and column_name = 'COLUMNX'");
		assertTrue(rs.isEmpty());
		
	}
	
	/**
	 * カラム追加のテスト。
	 * カラムが別のテーブルから移動してきたカラムだった場合のDB書き換えテスト
	 */
	public void testAddColumnReplace() {
		dao.insert("ALTER TABLE emp0 ADD CONSTRAINT emp_fk Foreign Key (deptno) REFERENCES dept0 (deptno)");
		
		DifferenceColumn dc = new DifferenceColumn();
		dc.setDifferentChar('A');
		
		dc.setObjectId("dname");
		dc.setColumnName("dname");
		dc.setColumnType("VARCHAR2");
		dc.setColumnLength(14);
		// dept0でない値。emp0からこのカラムは移動してきた。
		dc.setColumnTableName("emp0");
		
		DifferenceTable dt = new DifferenceTable();
		dt.setDifferentChar('U');
		dt.setObjectId("dept0");
		dt.setDifferenceColumns(Arrays.asList(dc));
		
		List<DifferenceTable> dlist = new ArrayList<DifferenceTable>();
		dlist.add(dt);
		
		DatabaseEdit.edit(dao, dlist, "ikemotot", "database");
		
		ResultStruct rs = dao.read("select * from dept0");
		assertFalse(rs.isEmpty());
		assertEquals(rs.getResult().get(0).getDataList().get(2), "ACCOUNTING");
		
		dao.insert("alter table emp0 add (dname VARCHAR2(14))");
		dao.insert("update emp0 set DNAME = (select DNAME from dept0 where dept0.deptno = emp0.deptno)");
		dao.insert("alter table dept0 drop column dname");
		dao.insert("alter table emp0 drop constraint emp_fk");
	}
	
	/**
	 * カラム追加のための文字列生成メソッドのテスト
	 */
	public void testCreateColumnString() {
		DifferenceColumn dc = new DifferenceColumn();
		dc.setDifferentChar('A');
		
		dc.setObjectId("truesth");
		dc.setColumnName("columnx");
		dc.setColumnType("NUMBER");
		dc.setColumnLength(10);
		
		String str = DatabaseEdit.createColumnString(dc);
		
		assertEquals(str, "columnx NUMBER(10)");
		
		dc.setColumnPrimary(true);
		dc.setColumnPrimaryFlag(true);
		dc.setColumnNotNull(true);
		dc.setColumnNotNullFlag(true);
		
		str = DatabaseEdit.createColumnString(dc);
		
		assertEquals(str, "columnx NUMBER(10) NOT NULL");
		
		dc.setColumnPrimary(false);
		dc.setColumnPrimaryFlag(false);
		dc.setColumnUnique(true);
		dc.setColumnUniqueFlag(true);
		
		str = DatabaseEdit.createColumnString(dc);
		
		assertEquals(str, "columnx NUMBER(10) NOT NULL UNIQUE");
		
		dc.setColumnDefaultFlag(true);
		dc.setColumnDefault("100");
		
		str = DatabaseEdit.createColumnString(dc);
		
		assertEquals(str, "columnx NUMBER(10) DEFAULT 100 NOT NULL UNIQUE");
		
		dc.setColumnForeignFlag(true);
		dc.setColumnReferenceTable("DEPT0");
		dc.setColumnReferenceTableColumn("DEPTNO");
		dc.setColumnDeleteType("CASCADE");
		dc.setColumnLength(2);
		dc.setColumnDefault("10");
		
		str = DatabaseEdit.createColumnString(dc);
		
		assertEquals(str, "columnx NUMBER(2) DEFAULT 10 NOT NULL UNIQUE ON DELETE CASCADE");
		
		dc.setColumnDeleteType("NO ACTION");
		
		str = DatabaseEdit.createColumnString(dc);
		
		assertEquals(str, "columnx NUMBER(2) DEFAULT 10 NOT NULL UNIQUE");
		
		dc.setColumnPrecisionLengthFlag(false);
		dc.setColumnPrecisionLength(2);
		
		str = DatabaseEdit.createColumnString(dc);
		
		assertEquals(str, "columnx NUMBER(2,2) DEFAULT 10 NOT NULL UNIQUE");
	}
	
}
