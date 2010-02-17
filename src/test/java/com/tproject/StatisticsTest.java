package com.tproject;

import junit.framework.TestCase;

/**
 * 
 * @author no_known
 *
 */
public class StatisticsTest extends TestCase {
	
	Statistics statistics;
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		statistics = new Statistics();
	}
	
	/**
	 * 初期値のテスト。すべて0
	 */
	public void testInit() {
		assertEquals(statistics.getRecursiveCalls(), 0);
		assertEquals(statistics.getDbBlockGets(), 0);
		assertEquals(statistics.getConsistentGets(), 0);
		assertEquals(statistics.getPhysicalReads(), 0);
		assertEquals(statistics.getRedoSize(), 0);
		assertEquals(statistics.getBytesSentViaSQL(), 0);
		assertEquals(statistics.getBytesReceivedViaSQL(), 0);
		assertEquals(statistics.getSQLRoundtrips(), 0);
		assertEquals(statistics.getSortsMemory(), 0);
		assertEquals(statistics.getSortsDisk(), 0);
		assertEquals(statistics.getPhysicalReads(), 0);
	}

	/**
	 * recursive calls情報の設定と取得テスト
	 */
	public void testSetgetRecursiveCalls() {
		statistics.setRecursiveCalls(1);
		
		assertEquals(statistics.getRecursiveCalls(), 1);
	}
	
	/**
	 * db block gets情報の設定と取得テスト
	 */
	public void testSetgetDbBlockGets() {
		statistics.setDbBlockGets(19);
		
		assertEquals(statistics.getDbBlockGets(), 19);
	}
	
	/**
	 * consistent gets情報の設定と取得テスト
	 */
	public void testSetgetConsistentGets() {
		statistics.setConsistentGets(100);
		
		assertEquals(statistics.getConsistentGets(), 100);
	}
	
	/**
	 * physical reads情報の設定と取得テスト
	 */
	public void testSetgetPhysicalReads() {
		statistics.setPhysicalReads(1);
		
		assertEquals(statistics.getPhysicalReads(), 1);
	}
	
	/**
	 * redo size情報の設定と取得テスト
	 */
	public void testSetgetRedoSize() {
		statistics.setRedoSize(90);
		
		assertEquals(statistics.getRedoSize(), 90);
	}
	
	/**
	 * bytes sent via sql*net to client情報の設定と取得テスト
	 */
	public void testSetgetBytesSentViaSQLtoClient() {
		statistics.setBytesSentViaSQL(19);
		
		assertEquals(statistics.getBytesSentViaSQL(), 19);
	}

	/**
	 * bytes received via sql*net from client情報の設定と取得テスト
	 */
	public void testSetgetBytesReceivedViaSQLfromClient() {
		statistics.setBytesReceivedViaSQL(99);
		
		assertEquals(statistics.getBytesReceivedViaSQL(), 99);
	}
	
	/**
	 * sql*net roundtrips to/from client情報の設定と取得テスト
	 */
	public void testSetgetSQLRoundtrips() {
		statistics.setSQLRoundtrips(22);
		
		assertEquals(statistics.getSQLRoundtrips(), 22);
	}
	
	/**
	 * sorts(memory)情報の設定と取得テスト
	 */
	public void testSetgetSortsMemory() {
		statistics.setSortsMemory(88);
		
		assertEquals(statistics.getSortsMemory(), 88);
	}
	
	/**
	 * sorts(disk)情報の設定と取得テスト
	 */
	public void testSetgetSortsDisk() {
		statistics.setSortsDisk(20);
		
		assertEquals(statistics.getSortsDisk(), 20);
	}
	
	/**
	 * rows processed情報の設定と取得テスト
	 */
	public void testSetgetRowsProcessed() {
		statistics.setRowsProcessed(32);
		
		assertEquals(statistics.getRowsProcessed(), 32);
	}

}
