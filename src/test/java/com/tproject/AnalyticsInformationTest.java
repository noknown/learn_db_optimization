package com.tproject;

import java.util.List;

import junit.framework.TestCase;

/**
 * 
 * @author no_known
 *
 */
public class AnalyticsInformationTest extends TestCase {
	
	AnalyticsInformation ai;
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		ai = new AnalyticsInformation();
	}
	
	/**
	 * ExecutionPlanリストの追加と取得のテスト
	 */
	public void testGetAddExecutionPlan() {
		ai.addExecutionPlan(new ExecutionPlan());
		ai.addExecutionPlan(new ExecutionPlan());
		
		List<ExecutionPlan> eplist = ai.getExecutionList();
		
		assertEquals(eplist.get(0).getId(), "");
		assertEquals(eplist.get(1).getId(), "");
		
		try {
			eplist.get(2);
			fail();
		} catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Statisticsリストの追加と取得のテスト
	 */
	public void testSetgetStatistics() {
		ai.setStatistics(new Statistics());

		assertEquals(ai.getStatistics().getRecursiveCalls(), 0);
	}
	
	/**
	 * 応答時間の設定
	 */
	public void testSetgetResponseTime() {
		ai.setResponseTime("00:00:00:01");
		
		assertEquals(ai.getResponseTime(), "00:00:00:01");
	}
	
}
