package com.tproject;

import java.util.List;

import junit.framework.TestCase;
/**
 * 
 * @author no_known
 *
 */
public class ResultDataTest extends TestCase{
	ResultData rdata;
	String[] results;
	
	/**
	 * getDataListのテスト。コンストラクタで入力した値が取得できるかをテスト<br/>
	 * <b>[事前条件]</b><br/>
	 * なし<br/>
	 * <b>[事後条件]</b><br/>
	 * 入力した値と出力が同じ文字列であり、インスタンスは別であること
	 */
	public void testGetDataList() {
		results = new String[]{"7369"};
		rdata = new ResultData(results);
		
		List<String> strs = rdata.getDataList();
		
		for(int i = 0; i < strs.size(); i++) {
			assertEquals(strs.get(i), results[i]);
			assertNotSame(strs.get(i), results[i]);
		}
		
		// 値が複数だった場合のテスト
		results = new String[]{"string", "33431"};
		rdata = new ResultData(results);
		
		strs = rdata.getDataList();
		
		for(int i = 0; i < strs.size(); i++) {
			assertEquals(strs.get(i), results[i]);
			assertNotSame(strs.get(i), results[i]);
		}

	}
	
	/**
	 * コンストラクタにnull値を含むString配列を渡した場合、null値の部分を空文字列
	 * に変換して格納することをテスト。
	 */
	public void testGetDataListTransNull() {
		results = new String[]{"99830", "sdkf", null, "9900", "string"};
		rdata = new ResultData(results);
		
		List<String> strs = rdata.getDataList();
		
		for(int i = 0; i < strs.size(); i++) {
			assertEquals(strs.get(i), results[i] == null ? "" : results[i]);
		}
	}

}
