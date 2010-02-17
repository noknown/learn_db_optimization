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
public class ExternalCommandTest extends TestCase {

	ExternalCommand ec;
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		
		ec = new MockExternalCommand();
		
	}
	
	/**
	 * コマンド実行のテスト
	 */
	public void testCommand() {
		AnalyticsInformation analysis = ec.execCommand("scott", "tiger", "pom.xml");

		assertEquals(analysis.getResponseTime(), "scott/tiger");
	}
	
	/**
	 * コマンド生成のテスト
	 */
	public void testCreateCommand() {
		String[] s = ec.createCommand("scott", "tiger", "p");
		
		assertEquals(s[0], "perl");
		assertEquals(s[1], "C:\\ora.pl");
		assertEquals(s[2], "scott");
		assertEquals(s[3], "tiger");
		assertEquals(s[4], "p");
	}
	
	/**
	 * resulttoAnalysisの実行テスト。正常な文字列の場合
	 */
	public void testResulttoAnalysis() {
		String result =
			"\n経過: 00:00:00.01\n\n実行計画\n"+
			"----------------------------------------------------------\nPlan hash value: 467731237\n"+
			"\n--------------------------------------------------------------------------\n"+
			"| Id  | Operation         | Name | Rows  | Bytes | Cost (%CPU)| Time     |\n"+
			"--------------------------------------------------------------------------\n"+
			"|   0 | SELECT STATEMENT  |      |    14 |   630 |     3   (0)| 00:00:01 |\n"+
			"|   1 |  TABLE ACCESS FULL| EMP0 |    14 |   630 |     3   (0)| 00:00:01 |\n"+
			"--------------------------------------------------------------------------\n"+
			"\n\n統計\n----------------------------------------------------------\n"+
			"         1  recursive calls\n         0  db block gets\n"+
			"         8  consistent gets\n         6  physical reads\n"+
			"         0  redo size\n      1595  bytes sent via SQL*Net to client\n"+
			"       385  bytes received via SQL*Net from client\n         2  SQL*Net roundtrips to/from client\n"+
			"         0  sorts (memory)\n         0  sorts (disk)\n        14  rows processed\n" +
			"(select * from emp0) \n" +
			"\n" +
			"total      4     0.02      0.03     0     4      0     8   \n" +
			"14 MERGE JOIN (se=0303 fe=322 time=12233 us)\n" +
			"\n" +
			"************************************************\n" +
			"total      5     0.06      0.04     0     5     0      9   \n" +
			"15 MERGE JOIN (se=0303 fe=322 time=165323 us)\n" +
			"************************************************\n" +
			"(:1:2:3........" +
			"total   	2 		0.08	0.90	0	3		0		7	\n";
		
		List<String> results = Arrays.asList(result.split("\n"));
		
		AnalyticsInformation an =  ec.resulttoAnalysis(results);
		
		assertEquals(an.getResponseTime(), "00:00:00.02");
		assertEquals(an.getStatistics().getRecursiveCalls(), 1);
		assertEquals(an.getStatistics().getDbBlockGets(), 0);
		assertEquals(an.getStatistics().getConsistentGets(), 8);
		assertEquals(an.getStatistics().getPhysicalReads(), 6);
		assertEquals(an.getStatistics().getRedoSize(), 0);
		assertEquals(an.getStatistics().getBytesSentViaSQL(), 1595);
		assertEquals(an.getStatistics().getBytesReceivedViaSQL(), 385);
		assertEquals(an.getStatistics().getSQLRoundtrips(), 2);
		assertEquals(an.getStatistics().getSortsMemory(), 0);
		assertEquals(an.getStatistics().getSortsDisk(), 0);
		assertEquals(an.getStatistics().getRowsProcessed(), 14);
		
		assertEquals(an.getExecutionList().get(0).getId(), "   0 ");
		assertEquals(an.getExecutionList().get(1).getOperation(), "  TABLE ACCESS FULL");
		assertEquals(an.getExecutionList().get(1).getName(), " EMP0 ");
		assertEquals(an.getExecutionList().get(0).getRows(), "    14 ");
		assertEquals(an.getExecutionList().get(0).getBytes(), "   630 ");
		assertEquals(an.getExecutionList().get(0).getCost(), "     3   (0)");
		assertEquals(an.getExecutionList().get(0).getTime() , " 00:00:01 ");
		assertEquals(an.getExecutionList().get(1).getTime(), " 00:00:01 (12233 us)");
	}
	
	/**
	 * resulttoAnalysisの実行テスト。異常な文字列の場合
	 */
	public void testResulttoAnalysisIllegality() {
		String[] strs = {"gsseddddddd sa", "経過: 00:00:.", "eedifiiosw", "|ss|sdf|ffe|", "  e3  recursive calls"};
		
		List<String> slist = Arrays.asList(strs);
		
		AnalyticsInformation an = ec.resulttoAnalysis(slist);
		
		assertNull(an);
	}
	
	class MockExternalCommand extends ExternalCommand {
		
		@Override
		public AnalyticsInformation execCommand(String user, String pass, String sql) {
			// TODO Auto-generated method stub
			AnalyticsInformation an = new AnalyticsInformation();
			an.setResponseTime(user+"/"+pass);
			
			return an;
		}
	}
	
}
