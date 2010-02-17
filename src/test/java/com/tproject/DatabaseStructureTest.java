package com.tproject;

import java.util.List;

import junit.framework.TestCase;

/**
 * 
 * @author no_known
 *
 */
public class DatabaseStructureTest extends TestCase {
	DatabaseStructure ds;
	
	@Override
	protected void setUp() {
		// TODO Auto-generated method stub
		ds = new DatabaseStructure("nant");
	}
	
	/**
	 * データベース名が適切に設定されるかのテスト
	 */
	public void testDatabaseName() {
		final String str = "doublei";
		ds.setDatabaseName(str);
		
		assertEquals(ds.getDatabaseName(), str);
		
		ds.setDatabaseName("dee sooie e");
		
		assertEquals(ds.getDatabaseName(), str);
	}
	
	/**
	 * テーブルの追加が適切に行われるかのテスト
	 */
	public void testAddTable() {
		TableStructure ts = new TableStructure("emp0");
		
		ColumnConstraint cc = new ColumnConstraint();
		cc.setPrimaryConstName("PK_EMP");
		
		TableColumnM tcm = new TableColumnM("empno", "VARCHAR2", 20, cc);
		ts.addSchema(tcm);
		
		boolean bool = ds.addTable(ts);
		assertTrue(bool);
		
		List<TableStructure> ltable = ds.getTables();
		
		assertEquals(ltable.get(0), ts);
		
		TableStructure ts2 = new TableStructure("emp0");
		ColumnConstraint cc2 = new ColumnConstraint();
		cc2.setNotnull(true);
		cc2.setUnique(true);
		
		//同じ名前のテーブル
		TableColumnM tcm2 = new TableColumnM("empno", "VARCHAR2", 10, cc2);
		ts2.addSchema(tcm2);
		
		bool = ds.addTable(ts2);
		assertFalse(bool);
		
		ltable = ds.getTables();
		
		try {
			ltable.get(1);
			
			fail();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * オーバーライドされたequalsメソッドのテスト
	 */
	public void testEquals() {
		ds.setDatabaseName("database1");
		
		DatabaseStructure ds2 = new DatabaseStructure("database2");
		DatabaseStructure ds3 = new DatabaseStructure("database1");
		
		assertFalse(ds.equals(ds2));
		assertTrue(ds.equals(ds3));
	}
	
	/**
	 * ディープコピーのテスト
	 */
	public void testDeepCopy() {
		DatabaseStructure ds0 = (DatabaseStructure) ds.clone();
		
		assertEquals(ds0.getDatabaseName(), ds.getDatabaseName());
		assertNotSame(ds0.getDatabaseName(), ds.getDatabaseName());
		
		assertEquals(ds0.getTables(), ds0.getTables());
		for(int i = 0; i < ds0.getTables().size(); i++) {
			assertNotSame(ds0.getTables().get(i), ds.getTables().get(i));
		}
	}
	
}
