package com.tproject;

import junit.framework.TestCase;

/**
 * 
 * @author no_known
 *
 */
public class TableColumnMTest extends TestCase {

	TableColumnM rc;
	
	@Override
	protected void setUp() {
		// TODO Auto-generated method stub
		rc = new TableColumnM();
	}
	
	/**
	 * 生成直後の状態テスト
	 */
	public void testInit() {
		assertEquals(rc.getLength(), new Integer(-1));
		assertEquals(rc.getPrecisionLength(), new Integer(-1));
		assertNull(rc.getName());
		assertNull(rc.getType());
		assertNull(rc.getTableName());
		
		assertTrue(rc.getConstraint() instanceof ColumnConstraint);
		
	}
	
	/**
	 * カラム名に適切な値が設定されるかのテスト
	 */
	public void testColumnName() {
		rc.setName("record1");
		
		assertEquals(rc.getName(), "record1");
		
		rc.setName("");
		
		assertEquals(rc.getName(), "record1");
		
		// 30文字より多い名前
		rc.setName("w123456789012345678901234567891");
		
		assertEquals(rc.getName(), "record1");
		
		// 30文字以下の名前
		rc.setName("w12345678901234567890123456789");
		
		assertEquals(rc.getName(), "w12345678901234567890123456789");
		
		rc.setTableName("00011");
		
		assertEquals(rc.getTableName(), "00011");
		
		rc.setTableName(null);
		
		assertNull(rc.getTableName());
	}
	
	/**
	 * カラムのデータ型に適切な値が設定されるかのテスト
	 */
	public void testColumnType() {
		rc.setType("Number");
		
		assertEquals(rc.getType(), "NUMBER");
		
		rc.setType("Boolean");
		
		assertEquals(rc.getType(), "NUMBER");
	}
	
	/**
	 * データ型の列長が設定されるかのテスト
	 */
	public void testColumnLength() {
		rc.setLength(6);
		
		assertEquals(rc.getLength(), new Integer(6));
		
		rc.setLength(null);
		
		assertEquals(rc.getLength(), new Integer(0));
		
	}
	
	/**
	 * データ型のスケールが適切に設定されるかのテスト
	 */
	public void testColumnPrecisionLength() {
		// 型がnull
		rc.setPrecisionLength(10);
		
		assertEquals(rc.getPrecisionLength(), new Integer(0));
		
		// 型がnumber
		rc.setType("NUMBER");
		
		rc.setPrecisionLength(6);
		
		assertEquals(rc.getPrecisionLength(), new Integer(6));
		
		rc.setPrecisionLength(null);
		
		assertEquals(rc.getPrecisionLength(), new Integer(0));
		
		rc.setPrecisionLength(-85);
		assertEquals(rc.getPrecisionLength(), new Integer(0));
		
		rc.setPrecisionLength(-84);
		assertEquals(rc.getPrecisionLength(), new Integer(-84));
		
		rc.setPrecisionLength(128);
		assertEquals(rc.getPrecisionLength(), new Integer(0));
		
		rc.setPrecisionLength(127);
		assertEquals(rc.getPrecisionLength(), new Integer(127));
		
		// 型がchar
		rc.setType("CHAR");
		
		rc.setPrecisionLength(8);
		
		assertEquals(rc.getPrecisionLength(), new Integer(0));
	}
	
	/**
	 * 参照制約が設定されるかのテスト
	 */
	public void testConstraint() {
		ColumnConstraint cc = new ColumnConstraint();
		cc.setPrimaryConstName("primary");
		
		rc.setConstraint(cc);
		
		assertEquals(rc.getConstraint().getPrimaryConstName(), "primary");
		
	}
	
	/**
	 * 引数つきコンストラクタのテスト
	 */
	public void testArgConstractor() {
		rc = null;
		ColumnConstraint cc = new ColumnConstraint();
		cc.setPrimaryConstName("primary");
		
		rc = new TableColumnM("column1", "VARCHAR2", 6, cc);
		
		assertEquals(rc.getName(), "column1");
		assertEquals(rc.getType(), "VARCHAR2");
		assertEquals(rc.getLength(), new Integer(6));
		assertEquals(rc.getPrecisionLength(), new Integer(0));
		assertEquals(rc.getConstraint().getPrimaryConstName(), "primary");
		
		rc = null;
		rc = new TableColumnM("column2", "NUMBER", 10, 2, cc, "table1");
		
		assertEquals(rc.getName(), "column2");
		assertEquals(rc.getType(), "NUMBER");
		assertEquals(rc.getLength(), new Integer(10));
		assertEquals(rc.getPrecisionLength(), new Integer(2));
		assertEquals(rc.getConstraint().getPrimaryConstName(), "primary");
		assertEquals(rc.getTableName(), "table1");
	}
	
	/**
	 * オーバーライドされたequalsのテスト
	 */
	public void testEquals() {
		rc = new TableColumnM("column1", "VARCHAR2", 6, null);
		TableColumnM tc2 = new TableColumnM("column2", "NUMBER", 10, null);
		TableColumnM tc3 = new TableColumnM("column1", "NUMBER", 7, null);
		
		assertFalse(rc.equals(tc2));
		assertTrue(rc.equals(tc3));
		
	}
	
	/**
	 * ディープコピーのテスト
	 */
	public void testDeepCopy() {
		TableColumnM tcm0 = (TableColumnM) rc.clone();

		assertNotSame(tcm0, rc);
		assertNull(tcm0.getName());
		assertNull(tcm0.getType());
		assertEquals(tcm0.getLength(), new Integer(-1));
		assertTrue(tcm0.getConstraint() instanceof ColumnConstraint);
		assertNull(tcm0.getTableName());
		
		rc.setName("columnna");
		rc.setType("NUMBER");
		rc.setLength(20);
		rc.setTableName("00001");

		TableColumnM tcm1 = (TableColumnM) rc.clone();
		
		assertNotSame(tcm1, rc);
		
		assertEquals(tcm1.getName(), rc.getName());
		assertNotSame(tcm1.getName(), rc.getName());
		
		assertEquals(tcm1.getType(), rc.getType());
		assertNotSame(tcm1.getType(), rc.getType());
		
		assertEquals(tcm1.getLength(), rc.getLength());
		assertNotSame(tcm1.getLength(), rc.getLength());
		
		assertEquals(tcm1.getPrecisionLength(), rc.getPrecisionLength());
		assertNotSame(tcm1.getPrecisionLength(), rc.getPrecisionLength());
		
		assertNotSame(tcm1.getConstraint(), rc.getConstraint());
		
		assertEquals(tcm1.getTableName(), rc.getTableName());
		assertNotSame(tcm1.getTableName(), rc.getTableName());
	}
	
	/**
	 * 一意なIDが付与されているかのテスト
	 */
	public void testIdentity() {
		rc.setName("tolegiz");
		TableColumnM tcm1 = (TableColumnM) rc.clone();
		TableColumnM tcm2 = new TableColumnM();
		tcm2.setName("tolegiz");
		
		
		assertEquals(tcm1.getId(), rc.getId());
		assertFalse(tcm2.getId().equals(rc.getId()));
		
		tcm1.setName("ords");
		tcm1.setType("CASCADE");
		tcm2.setName("ords");
		tcm2.setType("CASCADE");
		
		assertEquals(tcm1.getId(), rc.getId());	
		assertFalse(tcm2.getId().equals(rc.getId()));
	}
}
