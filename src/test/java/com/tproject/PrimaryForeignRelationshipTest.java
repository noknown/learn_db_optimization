package com.tproject;

import junit.framework.TestCase;

/**
 * 
 * @author no_known
 *
 */
public class PrimaryForeignRelationshipTest extends TestCase{
	
	PrimaryForeignRelationship pfr;
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		
		pfr = new PrimaryForeignRelationship();
	}
	
	/**
	 * リレーションを追加すると、対応した番号が取得できることをテスト
	 */
	public void testAddRelation() {
		pfr.addRelation("relate");
		
		assertEquals(pfr.getRelationNumber("relate"), 0);
	}
	
	/**
	 * リレーションを追加するごとに、番号が増えることをテスト
	 */
	public void testAddNumber() {
		pfr.addRelation("a");
		assertEquals(pfr.getRelationNumber("a"), 0);
		
		pfr.addRelation("b");
		assertEquals(pfr.getRelationNumber("b"), 1);

		pfr.addRelation("c");
		assertEquals(pfr.getRelationNumber("c"), 2);
	}
	
	/**
	 * 追加されていないリレーションの番号を参照された場合は
	 * UNDEFINED_RELATION定数を返す
	 */
	public void testUndefinedReference() {
		assertEquals(pfr.getRelationNumber("a"), PrimaryForeignRelationship.UNDEFINED_RELATION);
	}
	
	/**
	 * リレーションとして追加された文字列は大文字小文字を区別しないことをテスト
	 */
	public void testNonCaseSensitive() {
		pfr.addRelation("a");
		assertEquals(pfr.getRelationNumber("A"), 0);
		
		pfr.addRelation("B");
		assertEquals(pfr.getRelationNumber("b"), 1);
	}
	
	/**
	 * すでに追加されているリレーションが再び追加された場合のテスト
	 */
	public void testAddExistsRelation() {
		pfr.addRelation("a");
		assertEquals(pfr.getRelationNumber("a"), 0);
		
		pfr.addRelation("a");
		assertEquals(pfr.getRelationNumber("a"), 0);
	}
}
