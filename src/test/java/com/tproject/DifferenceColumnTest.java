package com.tproject;

import junit.framework.TestCase;

/**
 * 
 * @author no_known
 *
 */
public class DifferenceColumnTest extends TestCase {

	DifferenceColumn dct;
	
	@Override
	protected void setUp() {
		// TODO Auto-generated method stub
		
		dct = new DifferenceColumn();
	}
	

	/**
	 * カラム情報の設定と取得ができるかのテスト
	 */
	public void testColumnAttribute() {
		assertNull(dct.getColumnTableName());
		assertNull(dct.getColumnName());
		assertNull(dct.getColumnType());
		assertNull(dct.getColumnLength());
		assertNull(dct.getColumnPrecisionLength());
		assertNull(dct.getColumnIndex());
		assertFalse(dct.isColumnPrimary());
		assertNull(dct.getColumnPrimaryConstName());
		assertNull(dct.getColumnCheck());
		assertNull(dct.getColumnCheckConst());
		assertNull(dct.getColumnDefault());
		assertFalse(dct.isColumnUnique());
		assertFalse(dct.isColumnNotNull());
		assertNull(dct.getColumnForeignName());
		assertNull(dct.getColumnReferenceTable());
		assertNull(dct.getColumnReferenceTableColumn());
		assertNull(dct.getColumnReferenceConstraintName());
		assertNull(dct.getColumnDeleteType());
		
		dct.setColumnTableName("00001");
		dct.setColumnName("column1");
		dct.setColumnType("NUMBER");
		dct.setColumnLength(10);
		dct.setColumnPrecisionLength(3);
		dct.setColumnIndex(IndexTypes.NORMAL);
		dct.setColumnPrimary(true);
		dct.setColumnPrimaryConstName("primary1");
		dct.setColumnCheck("check1");
		dct.setColumnCheckConst("constcheck");
		dct.setColumnDefault("100");
		dct.setColumnUnique(true);
		dct.setColumnNotNull(true);
		dct.setColumnForeignName("foreign1");
		dct.setColumnReferenceTable("rtable1");
		dct.setColumnReferenceTableColumn("column1");
		dct.setColumnReferenceConstraintName("constname");
		dct.setColumnDeleteType("CASCADE");
		
		assertEquals(dct.getColumnTableName(), "00001");
		assertEquals(dct.getColumnName(), "column1");
		assertEquals(dct.getColumnType(), "NUMBER");
		assertEquals(dct.getColumnLength(), new Integer(10));
		assertEquals(dct.getColumnPrecisionLength(), new Integer(3));
		assertEquals(dct.getColumnIndex(), IndexTypes.NORMAL);
		assertTrue(dct.isColumnPrimary());
		assertEquals(dct.getColumnPrimaryConstName(), "primary1");
		assertEquals(dct.getColumnCheck(), "check1");
		assertEquals(dct.getColumnCheckConst(), "constcheck");
		assertEquals(dct.getColumnDefault(), "100");
		assertTrue(dct.isColumnUnique());
		assertTrue(dct.isColumnNotNull());
		assertEquals(dct.getColumnForeignName(), "foreign1");
		assertEquals(dct.getColumnReferenceTable(), "rtable1");
		assertEquals(dct.getColumnReferenceTableColumn(), "column1");
		assertEquals(dct.getColumnReferenceConstraintName(), "constname");
		assertEquals(dct.getColumnDeleteType(), "CASCADE");
		
		dct.setColumnTableName(null);
		dct.setColumnName(null);
		dct.setColumnType(null);
		dct.setColumnLength(null);
		dct.setColumnPrecisionLength(null);
		dct.setColumnIndex(null);
		dct.setColumnPrimary(false);
		dct.setColumnPrimaryConstName(null);
		dct.setColumnCheck(null);
		dct.setColumnCheckConst(null);
		dct.setColumnDefault(null);
		dct.setColumnUnique(false);
		dct.setColumnNotNull(false);
		dct.setColumnForeignName(null);
		dct.setColumnReferenceTable(null);
		dct.setColumnReferenceTableColumn(null);
		dct.setColumnReferenceConstraintName(null);
		dct.setColumnDeleteType(null);

		assertNull(dct.getColumnTableName());
		assertNull(dct.getColumnName());
		assertNull(dct.getColumnType());
		assertNull(dct.getColumnLength());
		assertNull(dct.getColumnPrecisionLength());
		assertNull(dct.getColumnIndex());
		assertFalse(dct.isColumnPrimary());
		assertNull(dct.getColumnPrimaryConstName());
		assertNull(dct.getColumnCheck());
		assertNull(dct.getColumnCheckConst());
		assertNull(dct.getColumnDefault());
		assertFalse(dct.isColumnUnique());
		assertFalse(dct.isColumnNotNull());
		assertNull(dct.getColumnForeignName());
		assertNull(dct.getColumnReferenceTable());
		assertNull(dct.getColumnReferenceTableColumn());
		assertNull(dct.getColumnReferenceConstraintName());
		assertNull(dct.getColumnDeleteType());

	}
	
	/**
	 * カラムの各情報変更フラグが設定、取得できるかどうかのテスト。
	 */
	public void testColumnFlag() {
		assertFalse(dct.isColumnNameFlag());
		assertFalse(dct.isColumnTypeFlag());
		assertFalse(dct.isColumnLengthFlag());
		assertFalse(dct.isColumnPrecisionLengthFlag());
		assertFalse(dct.isColumnIndexFlag());
		assertFalse(dct.isColumnPrimaryFlag());
		assertFalse(dct.isColumnPrimaryConstNameFlag());
		assertFalse(dct.isColumnCheckFlag());
		assertFalse(dct.isColumnDefaultFlag());
		assertFalse(dct.isColumnUniqueFlag());
		assertFalse(dct.isColumnNotNullFlag());
		assertFalse(dct.isColumnForeignFlag());
		
		dct.setColumnNameFlag(true);
		dct.setColumnTypeFlag(true);
		dct.setColumnLengthFlag(true);
		dct.setColumnPrecisionLengthFlag(true);
		dct.setColumnIndexFlag(true);
		dct.setColumnPrimaryFlag(true);
		dct.setColumnPrimaryConstNameFlag(true);
		dct.setColumnCheckFlag(true);
		dct.setColumnDefaultFlag(true);
		dct.setColumnUniqueFlag(true);
		dct.setColumnNotNullFlag(true);
		dct.setColumnForeignFlag(true);
		
		assertTrue(dct.isColumnNameFlag());
		assertTrue(dct.isColumnTypeFlag());
		assertTrue(dct.isColumnLengthFlag());
		assertTrue(dct.isColumnPrecisionLengthFlag());
		assertTrue(dct.isColumnIndexFlag());
		assertTrue(dct.isColumnPrimaryFlag());
		assertTrue(dct.isColumnPrimaryConstNameFlag());
		assertTrue(dct.isColumnCheckFlag());
		assertTrue(dct.isColumnDefaultFlag());
		assertTrue(dct.isColumnUniqueFlag());
		assertTrue(dct.isColumnNotNullFlag());
		assertTrue(dct.isColumnForeignFlag());
	}
	
}
