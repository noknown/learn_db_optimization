package com.tproject;

import junit.framework.TestCase;
/**
 * 
 * @author no_known
 *
 */
public class ColumnConstraintTest extends TestCase {

	ColumnConstraint rest;
	
	@Override
	protected void setUp() {
		// TODO Auto-generated method stub
		rest = new ColumnConstraint();
	}
	
	/**
	 * 初期値テスト
	 */
	public void testInitValue() {
		assertNull(rest.getPrimaryConstName());
		assertNull(rest.getCheckConstName());
		assertNull(rest.getCheck());
		assertNull(rest.getDefault());
		assertEquals(rest.getIndex(), IndexTypes.NONE);
		
		assertFalse(rest.isPrimary());
		assertFalse(rest.isUnique());
		assertFalse(rest.isNotnull());
		
		assertTrue(rest.getForeign() instanceof ForeignConstraint);
	}

	/**
	 * 主キー制約の設定テスト。
	 */
	public void testPrimaryKey() {
		rest.setPrimaryConstName("PRIMARY");
		rest.setPrimary(true);
		
		assertEquals(rest.getPrimaryConstName(), "PRIMARY");
		assertTrue(rest.isPrimary());
		
		rest.setPrimaryConstName("");
		rest.setPrimary(false);
		
		assertEquals(rest.getPrimaryConstName(), "PRIMARY");
		assertFalse(rest.isPrimary());
		
		rest.setPrimaryConstName(null);
		
		assertNull(rest.getPrimaryConstName());
	}
	
	/**
	 * 一意制約の設定テスト。
	 */
	public void testUnique() {
		rest.setUnique(true);
		
		assertTrue(rest.isUnique());
		
		rest.setUnique(false);
		
		assertFalse(rest.isUnique());
	}
	
	/**
	 * チェック制約の設定テスト。
	 */
	public void testCheck() {
		String str = "size >= 20";
		
		rest.setCheck(str);
		rest.setCheckConstName("alterof");
		
		assertEquals(str, rest.getCheck());
		assertEquals("alterof", rest.getCheckConstName());
		
		rest.setCheck("kkd elsdi eefras");
		rest.setCheckConstName("255ssdf");
		
		assertEquals(str, rest.getCheck());
		assertEquals("alterof", rest.getCheckConstName());
		
		rest.setCheck(null);
		rest.setCheckConstName(null);
		
		assertNull(rest.getCheck());
		assertEquals("alterof", rest.getCheckConstName());
		
	}
	
	/**
	 * NOT NULL規約の設定テスト。
	 */
	public void testNotNull() {
		rest.setNotnull(true);
		
		assertTrue(rest.isNotnull());
		
		rest.setNotnull(false);
		
		assertFalse(rest.isNotnull());
	}
	
	/**
	 * 外部キー制約の設定テスト。
	 */
	public void testForeignKey() {
		ForeignConstraint fc = new ForeignConstraint();
		fc.setConstraintName("FK_TEST");
		fc.setDeleteType("CASCADE");
		fc.setReferenceConstraintName("PK_TEST");
		fc.setReferenceTableColumn("column1");
		fc.setReferenceTable("EMP0");
		
		rest.setForeign(fc);
		
		ForeignConstraint ss = rest.getForeign();

		assertEquals(ss.getConstraintName(), fc.getConstraintName());
		assertEquals(ss.getDeleteType(), fc.getDeleteType());
		assertEquals(ss.getReferenceConstraintName(), fc.getReferenceConstraintName());
		assertEquals(ss.getReferenceTableColumn(), fc.getReferenceTableColumn());
		assertEquals(ss.getReferenceTable(), fc.getReferenceTable());
		
		rest.setForeign(null);
		
		ss = rest.getForeign();

		assertNull(ss);

	}
	
	/**
	 * デフォルト規約の設定テスト。
	 */
	public void testDefault() {
		rest.setDefault("20");
		
		assertEquals(rest.getDefault(), "20");
		
		rest.setDefault("");
		
		assertNull(rest.getDefault());
		
		rest.setDefault(null);
		
		assertNull(rest.getDefault());
	}
	
	/**
	 * インデックスの設定テスト
	 */
	public void testIndex() {
		rest.setIndex(IndexTypes.NORMAL);
		
		assertEquals(rest.getIndex(), IndexTypes.NORMAL);
		
		rest.setIndex(null);
		
		assertEquals(rest.getIndex(), IndexTypes.NORMAL);
		
		rest.setIndex(IndexTypes.BITMAP);
		
		assertEquals(rest.getIndex(), IndexTypes.BITMAP);
		
		rest.setIndex(IndexTypes.REVERSE);
		
		assertEquals(rest.getIndex(), IndexTypes.REVERSE);
	}
	
	/**
	 * ディープコピーのテスト
	 */
	public void testDeepCopy() {
		ColumnConstraint c0 = (ColumnConstraint) rest.clone();
		
		assertEquals(c0.getIndex(), IndexTypes.NONE);
		assertFalse(c0.isPrimary());
		assertNull(c0.getPrimaryConstName());
		assertNull(c0.getCheck());
		assertNull(c0.getCheckConstName());
		assertNull(c0.getDefault());
		assertFalse(c0.isNotnull());
		assertFalse(c0.isUnique());
		assertTrue(c0.getForeign() instanceof ForeignConstraint);

		rest.setIndex(IndexTypes.NORMAL);
		rest.setPrimary(true);
		rest.setPrimaryConstName("prim");
		rest.setCheck("che < 20");
		rest.setCheckConstName("constname");
		rest.setDefault("100");
		rest.setForeign(new ForeignConstraint());
		rest.setNotnull(true);
		rest.setUnique(false);
		
		ColumnConstraint cc = (ColumnConstraint) rest.clone();
		
		assertNotSame(rest, cc);
		
		assertSame(rest.getIndex(), cc.getIndex());
		
		assertSame(rest.isPrimary(), cc.isPrimary());
		
		assertEquals(rest.getPrimaryConstName(), cc.getPrimaryConstName());
		assertNotSame(rest.getPrimaryConstName(), cc.getPrimaryConstName());
		
		assertEquals(rest.getDefault(), cc.getDefault());
		assertNotSame(rest.getDefault(), cc.getDefault());
		
		assertEquals(rest.getCheck(), cc.getCheck());
		assertNotSame(rest.getCheck(), cc.getCheck());
		assertEquals(rest.getCheckConstName(), cc.getCheckConstName());
		assertNotSame(rest.getCheckConstName(), cc.getCheckConstName());
		
		assertSame(rest.isNotnull(), cc.isNotnull());
		
		assertSame(rest.isUnique(), cc.isUnique());
		
		assertNotSame(rest.getForeign(), cc.getForeign());

	}
}
