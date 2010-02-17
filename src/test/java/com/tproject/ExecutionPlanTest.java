package com.tproject;

import junit.framework.TestCase;

/**
 * 
 * @author no_known
 *
 */
public class ExecutionPlanTest extends TestCase {
	ExecutionPlan ep;
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		ep = new ExecutionPlan();
	}

	/**
	 * 初期値テスト。すべて空文字
	 */
	public void testInit() {
		assertEquals(ep.getId(), "");
		assertEquals(ep.getOperation(), "");
		assertEquals(ep.getName(), "");
		assertEquals(ep.getRows(), "");
		assertEquals(ep.getBytes(), "");
		assertEquals(ep.getTime(), "");
	}
	
	/**
	 * ID情報設定、取得テスト
	 */
	public void testSetgetId() {
		ep.setId("1");
		
		assertEquals(ep.getId(), "1");
	}
	
	/**
	 * Operation情報設定、取得テスト
	 */
	public void testSetgetOperation() {
		ep.setOperation("SELECT STATEMENT");
		
		assertEquals(ep.getOperation(), "SELECT STATEMENT");
	}
	
	/**
	 * Name情報設定、取得テスト
	 */
	public void testSetgetName() {
		ep.setName("EMP1");
		
		assertEquals(ep.getName(), "EMP1");
	}
	
	/**
	 * Rows情報設定、取得テスト
	 */
	public void testSetgetRows() {
		ep.setRows("1");
		
		assertEquals(ep.getRows(), "1");
	}
	
	/**
	 * Bytes情報設定、取得テスト
	 */
	public void testSetgetBytes() {
		ep.setBytes("34");
		
		assertEquals(ep.getBytes(), "34");
	}

	/**
	 * Cost情報設定、取得テスト
	 */
	public void testSetgetCost() {
		ep.setCost("5");
		
		assertEquals(ep.getCost(), "5");
	}
	
	/**
	 * Time情報設定、取得テスト
	 */
	public void testSetgetTime() {
		ep.setTime("10:00:32");
		
		assertEquals(ep.getTime(), "10:00:32");
	}
}
