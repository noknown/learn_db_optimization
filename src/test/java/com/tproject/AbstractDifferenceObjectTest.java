package com.tproject;

import junit.framework.TestCase;

/**
 * 
 * @author no_known
 *
 */
public class AbstractDifferenceObjectTest extends TestCase {
	
	AbstractDifferenceObject adc;
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		adc = new AbstractDifferenceObject() {
			
		};
	}
	
	/**
	 * 変更種類を表す１文字を設定、取得できるかのテスト
	 */
	public void testDifferentChar() {
		assertNull(adc.getDifferentChar());
		
		adc.setDifferentChar('U');
		assertEquals(adc.getDifferentChar(), new Character('U'));
		
		adc.setDifferentChar('A');
		assertEquals(adc.getDifferentChar(), new Character('A'));
		
		adc.setDifferentChar('D');
		assertEquals(adc.getDifferentChar(), new Character('D'));
		
		adc.setDifferentChar('F');
		assertNull(adc.getDifferentChar());
		
		adc.setDifferentChar('D');
		assertEquals(adc.getDifferentChar(), new Character('D'));
		
		adc.setDifferentChar(null);
		assertNull(adc.getDifferentChar());
	}
	
	/**
	 * オブジェクトIDを設定し、取得するテスト
	 */
	public void testObjectId() {
		
		assertNull(adc.getObjectId());
		
		adc.setObjectId("table_id_1");
		assertEquals(adc.getObjectId(), "table_id_1");
		
		adc.setObjectId(null);
		assertNull(adc.getObjectId());
	}
}
