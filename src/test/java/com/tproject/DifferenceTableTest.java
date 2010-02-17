package com.tproject;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

public class DifferenceTableTest extends TestCase {

	DifferenceTable dt;
	
	@Override
	protected void setUp() {
		// TODO Auto-generated method stub
		dt = new DifferenceTable();
	}
	
	/**
	 * テーブル情報の設定と取得ができるかテスト
	 */
	public void testTableAttribute() {
		assertNull(dt.getTableName());
		assertEquals(dt.getTableId(), "");
		
		dt.setTableName("defaultTablename");
		dt.setTableId("0001");
		
		assertEquals(dt.getTableName(), "defaultTablename");
		assertEquals(dt.getTableId(), "0001");
		
		dt.setTableName(null);
		dt.setTableId(null);
		
		assertNull(dt.getTableName());
		assertEquals(dt.getTableId(), "0001");
	}
	
	/**
	 * テーブル情報変更フラグの設定と取得ができるかのテスト
	 */
	public void testTableFlag() {
		assertFalse(dt.isTableNameFlag());
		
		dt.setTableNameFlag(true);
		
		assertTrue(dt.isTableNameFlag());
	}
	
	/**
	 * DifferenceColumnのリストを格納、取得できるかのテスト
	 */
	public void testAddDifferentColumn() {
		DifferenceColumn  dffc = new DifferenceColumn();
		dffc.setDifferentChar('A');
		dffc.setColumnName("name1");
		
		List<DifferenceColumn> dlist = Arrays.asList(dffc);
		
		assertTrue(dt.getDifferenceColumns().isEmpty());
		
		dt.setDifferenceColumns(dlist);
		
		DifferenceColumn dc = dt.getDifferenceColumns().get(0);
		
		assertEquals(dc.getDifferentChar(), new Character('A'));
		assertEquals(dc.getColumnName(), "name1");
		
		try {
			dt.getDifferenceColumns().get(1);
		} catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
}
