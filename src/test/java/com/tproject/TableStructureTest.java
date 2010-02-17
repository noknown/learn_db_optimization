package com.tproject;

import java.util.List;

import junit.framework.TestCase;
/**
 * 
 * @author no_known
 *
 */
public class TableStructureTest extends TestCase {

	TableStructure ts;
	@Override
	protected void setUp() {
		// TODO Auto-generated method stub
		ts = new TableStructure("tablename");
		
	}
	
	/**
	 * 生成直後のschemaには何も入っていないことをテスト
	 */
	public void testIsEmpty() {
		assertTrue(ts.getSchemas().isEmpty());
	}
	
	/**
	 * 適切なテーブル名が設定されるかのテスト
	 */
	public void testTableName() {
		ts.setTableName("emp0");
		
		assertEquals(ts.getTableName(), "emp0");
		
		ts.setTableName("w123456789012345678901234567890");
		
		assertEquals(ts.getTableName(), "emp0");
		
	}
	
	/**
	 * テーブルID番号が設定されるかのテスト
	 */
	public void testTableId() {
		ts.setTableId("00001");
		
		assertEquals(ts.getTableId(), "00001");
		
		ts.setTableId(null);
		
		assertEquals(ts.getTableId(), "00001");
	}
	
	/**
	 * カラム追加処理がちゃんと動くかのテスト
	 */
	public void testAddSchema() {
		ColumnConstraint cc = new ColumnConstraint();
		cc.setPrimaryConstName("PK_DOC");
		
		TableColumnM m = new TableColumnM("primary", "VARCHAR2", 20, cc);
		
		boolean bool = ts.addSchema(m);
		assertTrue(bool);
		
		List<TableColumnM> tlist = ts.getSchemas();
		assertEquals(tlist.get(0).getType(), "VARCHAR2");
		
		ColumnConstraint cc2 = new ColumnConstraint();
		cc2.setPrimaryConstName("PK_SS");
		cc2.setCheck("FFD > 100");
		TableColumnM m2 = new TableColumnM("foreign", "NUMBER", 10, cc2);
		
		bool = ts.addSchema(m2);
		assertTrue(bool);
		
		tlist = ts.getSchemas();
		assertEquals(tlist.get(1).getName(), "foreign");
		
		// カラム名が同じものが入ったとき。
		TableColumnM m3 = new TableColumnM("primary", "NUMBER", 21, cc);
		
		bool = ts.addSchema(m3);
		assertFalse(bool);
		
		tlist = ts.getSchemas();
		
		try {
			assertNull(tlist.get(2));
			
			fail();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * カラムの削除処理がきちんと実行されるかのテスト
	 */
	public void testRemove() {
		TableColumnM m1 = new TableColumnM("column1", "", 0, null),
		m2 = new TableColumnM("column2", "", 0, null),
		m3 = new TableColumnM("column3", "", 0, null),
		m4 = new TableColumnM("column4", "", 0, null);
		
		ts.addSchema(m1);
		ts.addSchema(m2);
		ts.addSchema(m3);
		
		ts.removeSchema(m2);
		
		List<TableColumnM> col = ts.getSchemas();
		
		assertTrue(col.contains(m1));
		assertFalse(col.contains(m2));
		assertTrue(col.contains(m3));
		
		ts.removeSchema(m4);
		
		col = ts.getSchemas();
		
		assertTrue(col.contains(m1));
		assertFalse(col.contains(m2));
		assertTrue(col.contains(m3));
		assertFalse(col.contains(m4));
		
	}
	
	/**
	 * オーバーライドされたequalsメソッドのテスト
	 */
	public void testEquals() {
		ts.setTableName("emp0");
		
		TableStructure ts2 = new TableStructure("emp1");
		
		TableStructure ts3 = new TableStructure("emp0");
		
		assertFalse(ts.equals(ts2));
		assertTrue(ts.equals(ts3));
	}
	
	/**
	 * ディープコピーのテスト
	 */
	public void testDeepCopy() {
		
		TableStructure ts0 = (TableStructure) ts.clone();
		
		assertNotSame(ts0, ts);
		
		assertEquals(ts0.getTableName(), ts.getTableName());
		assertNotSame(ts0.getTableName(), ts.getTableName());
		
		assertEquals(ts0.getTableId(), ts.getTableId());
		assertNotSame(ts0.getTableId(), ts.getTableId());
		
		assertEquals(ts0.getSchemas(), ts.getSchemas());
		for(int i = 0; i < ts0.getSchemas().size(); i++) {
			assertNotSame(ts0.getSchemas().get(i), ts.getSchemas().get(i));
		}
		
		for(int i = 0; i < ts0.getSchemas().size(); i++) {
			assertEquals(ts0.getSchemas().get(i).getName(), ts.getSchemas().get(i).getName());
		}
		
	}
	
	/**
	 * 一意なIDが付与されているかのテスト
	 */
	public void testIdentity() {
		TableStructure ts1 = (TableStructure) ts.clone();
		TableStructure ts2 = new TableStructure("tablename");
		
		assertEquals(ts1.getId(), ts.getId());
		assertFalse(ts2.getId().equals(ts.getId()));
		
		ts1.setTableName("sseffd");
		ts2.setTableName("sseffd");
		
		assertEquals(ts1.getId(), ts.getId());
		assertFalse(ts2.getId().equals(ts.getId()));

	}
	
	/**
	 * 主キーが設定されている項目があれば、その項目名を返却するかのテスト
	 */
	public void testPrimarykeyget() {
		ColumnConstraint cc = new ColumnConstraint();
		cc.setPrimary(true);
		TableColumnM tcm = new TableColumnM("column1", "NUMBER", 1, cc);
		
		TableStructure ts = new TableStructure("table1");
		ts.addSchema(tcm);
		
		for(String sname : ts.getPrimarycolumn()) {
			assertEquals(sname, "column1");
		}
		
		cc.setPrimary(false);
		tcm.setConstraint(cc);
		ts.getSchemas().clear();
		ts.addSchema(tcm);
		
		assertTrue(ts.getPrimarycolumn().isEmpty());
		
	}
	
	/**
	 * 外部キーが設定されている項目があれば、その項目名を返却するかのテスト
	 */
	public void testForeignkeyget() {
		ForeignConstraint fc = new ForeignConstraint();
		fc.setReferenceTableColumn("foreign");
		ColumnConstraint cc = new ColumnConstraint();
		cc.setForeign(fc);
		TableColumnM tcm = new TableColumnM("column1", "NUMBER", 1, cc);
		TableStructure ts = new TableStructure("table1");
		ts.addSchema(tcm);
		
		for(String sname : ts.getForeigncolumn()) {
			assertEquals(sname, "foreign");
		}
		
		ColumnConstraint cc2 = new ColumnConstraint();
		tcm.setConstraint(cc2);
		ts.addSchema(tcm);
		
		assertTrue(ts.getForeigncolumn().isEmpty());
	}
	
	/**
	 * 自分とあるテーブルと関連性があるならばtrueを返すことをテスト
	 * 通常の交換の場合。
	 */
	public void testIsRelation() {
		ColumnConstraint cc = new ColumnConstraint();
		cc.setPrimary(true);
		TableColumnM tcm = new TableColumnM("column1", "NUMBER", 1, cc);
		TableStructure ts = new TableStructure("table1");
		// 主キーが設定されているカラムを含むテーブル
		ts.addSchema(tcm);
		
		ForeignConstraint fc = new ForeignConstraint();
		fc.setReferenceTable("TABLE1");
		ColumnConstraint cc1 = new ColumnConstraint();
		cc1.setForeign(fc);
		TableColumnM tcmt = new TableColumnM("column2", "NUMBER", 1, cc1);
		TableStructure ts2 = new TableStructure("table2");
		// 主キーを参照している外部制約のあるカラムを含むテーブル
		ts2.addSchema(tcmt);
		
		assertTrue(ts.isRelation(ts2));
		assertTrue(ts2.isRelation(ts));
		assertFalse(ts.isRelation(null));
	}
	
	/**
	 * 自分とあるテーブルと関連性があるならばtrueを返すことをテスト
	 * 交換後に戻す処理の場合。
	 */
	public void testIsRelationMoved() {
		TableColumnM tcm = new TableColumnM("column1", "NUMBER", 1, new ColumnConstraint());
		tcm.setTableName("TABLE1");
		TableStructure ts = new TableStructure("table2");
		ts.addSchema(tcm);
		
		TableStructure ts2 = new TableStructure("table1");
		
		assertTrue(ts.isRelation(ts2));
		assertTrue(ts2.isRelation(ts));
		assertFalse(ts.isRelation(null));
	}
}
