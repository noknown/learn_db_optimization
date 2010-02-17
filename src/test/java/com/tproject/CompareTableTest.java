package com.tproject;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class CompareTableTest extends TestCase {

	CompareTable ct;
	TableStructure ts1, ts2;
	List<TableStructure> oldTables, newTables;
	
	@Override
	protected void setUp() {
		// TODO Auto-generated method stub
		ct = new CompareTable();
		
		ts1 = new TableStructure("table1");
		ts1.setTableId("0002");
		ts2 = new TableStructure("table2");
		ts2.setTableId("0001");
		
		oldTables = new ArrayList<TableStructure>();
		oldTables.add(ts1);
		oldTables.add(ts2);
		oldTables.add(new TableStructure("table3"));
		
		newTables = new ArrayList<TableStructure>();
		newTables.add((TableStructure)oldTables.get(0).clone());
		newTables.add((TableStructure)oldTables.get(1).clone());
		newTables.add((TableStructure)oldTables.get(2).clone());
	}
	
	/**
	 * クローンを用いてコピーしたオブジェクトリストを渡した場合は空のリストが
	 * 返ることをテスト
	 */
	public void testCompareEqualsList() {
		List<? extends AbstractDifferenceObject> tslist = ct.compare(oldTables, newTables);
		
		assertTrue(tslist.isEmpty());
		
		oldTables.add(new TableStructure("table4"));
		newTables.add((TableStructure)oldTables.get(3).clone());
		
		tslist = null;
		tslist = ct.compare(oldTables, newTables);
		
		assertTrue(tslist.isEmpty());
	}
	
	/**
	 * TableStructure内のTableColumnMのリストに変更があった場合にその変更が
	 * DifferenceColumnのリストで返ることをテスト
	 */
	public void testCompareColumnList() {
		TableColumnM t = new TableColumnM("col1", "NUMBER", 10, new ColumnConstraint());
		newTables.get(0).getSchemas().add(t);
		
		List<? extends AbstractDifferenceObject> tslist = ct.compare(oldTables, newTables);
		
		assertFalse(tslist.isEmpty());
		
		DifferenceTable dt = (DifferenceTable) tslist.get(0);
		assertEquals(dt.getDifferentChar(), new Character('U'));
		assertEquals(dt.getObjectId(), newTables.get(0).getId());
		assertEquals(dt.getTableId(), newTables.get(0).getTableId());
		
		assertEquals(dt.getDifferenceColumns().get(0).getColumnName(), "col1");
		
	}
	
	/**
	 * クローンを用いてコピーしたオブジェクトリストを渡した場合で何らかの値が変わっている
	 * 時はアップデートを表すDifferentTableが返ることをテスト
	 */
	public void testCompareNotEqualsList() {
		newTables.get(0).setTableName("tttt");
		
		List<? extends AbstractDifferenceObject> tslist = ct.compare(oldTables, newTables);
		
		assertFalse(tslist.isEmpty());
		
		DifferenceTable dt = (DifferenceTable) tslist.get(0);
		
		assertTrue(dt.isTableNameFlag());
		assertEquals(dt.getTableName(), "tttt");
		assertEquals(dt.getDifferentChar(), new Character('U'));
		assertEquals(dt.getObjectId(), newTables.get(0).getId());
		assertEquals(dt.getTableId(), newTables.get(0).getTableId());
	}
	
	
	/**
	 * 二つのTableStructureオブジェクトを比較する処理のテスト
	 */
	public void testDiffTable() {
		TableStructure t1 = new TableStructure("t1");
		TableStructure t2 = (TableStructure) t1.clone();
		
		DifferenceTable dt = (DifferenceTable) ct.generateSubtraction(t1, t2);
		
		assertNull(dt);
		
		t2.setTableName("t2");
		
		dt = null;
		dt = (DifferenceTable) ct.generateSubtraction(t1, t2);
		
		assertEquals(dt.getDifferentChar(), new Character('U'));
		assertEquals(dt.getObjectId(), t1.getId());
		assertTrue(dt.isTableNameFlag());
		assertEquals(dt.getTableName(), "t2");
	}
	
	/**
	 * 新しく追加されたテーブルがあれば追加データを表す
	 * DifferenceTableオブジェクトが返る
	 */
	public void testCompareAddList() {
		TableColumnM tcm = new TableColumnM("col1", "NUMBER", 10, new ColumnConstraint());
		TableColumnM tc2 = new TableColumnM("col2", "NUMBER", 10, new ColumnConstraint());
		TableStructure ts = new TableStructure("table4");
		ts.addSchema(tcm);
		ts.addSchema(tc2);
		
		newTables.add(ts);
		
		List<? extends AbstractDifferenceObject> dclist = ct.compare(oldTables, newTables);
		
		assertFalse(dclist.isEmpty());
		assertEquals(dclist.get(0).getDifferentChar(), new Character('A'));
		assertEquals(dclist.get(0).getObjectId(), newTables.get(3).getId());
		
		DifferenceTable dt = (DifferenceTable) dclist.get(0);
		assertTrue(dt.isTableNameFlag());
		assertEquals(dt.getTableName(), "table4");
		
		assertEquals(dt.getDifferenceColumns().get(0).getDifferentChar(), new Character('A'));
		assertEquals(dt.getDifferenceColumns().get(0).getColumnName(), "col1");
		assertEquals(dt.getDifferenceColumns().get(0).getColumnType(), "NUMBER");
		assertEquals(dt.getDifferenceColumns().get(1).getDifferentChar(), new Character('A'));
		assertEquals(dt.getDifferenceColumns().get(1).getColumnName(), "col2");
		assertEquals(dt.getDifferenceColumns().get(1).getColumnType(), "NUMBER");		
		try {
			dclist.get(1);
		} catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 削除されたデータがあれば、削除データを表すDifferenceColumnオブジェクト
	 * を返す。
	 */
	public void testCompareRemoveList() {
		newTables.remove(1);
		
		List<? extends AbstractDifferenceObject> dclist = ct.compare(oldTables, newTables);
		
		assertFalse(dclist.isEmpty());
		assertEquals(dclist.get(0).getDifferentChar(), new Character('D'));
		assertEquals(dclist.get(0).getObjectId(), oldTables.get(1).getId());
		
		DifferenceTable dt = (DifferenceTable) dclist.get(0);
		assertEquals(dt.getTableId(), "0001");
		
		try {
			dclist.get(1);
		} catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
	
}
