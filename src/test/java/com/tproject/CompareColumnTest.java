package com.tproject;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * 
 * @author no_known
 *
 */
public class CompareColumnTest extends TestCase {

	CompareColumn comc;
	List<TableColumnM> oldColumns, newColumns;
	
	@Override
	protected void setUp() {
		// TODO Auto-generated method stub
		comc = new CompareColumn();
		
		oldColumns = new ArrayList<TableColumnM>();
		oldColumns.add(new TableColumnM("table1", "NUMBER", 1, new ColumnConstraint()));
		oldColumns.add(new TableColumnM("table2", "NUMBER", 1, new ColumnConstraint()));
		oldColumns.add(new TableColumnM("tables", "NUMBER", 1, new ColumnConstraint()));
		
		newColumns = new ArrayList<TableColumnM>();
		newColumns.add((TableColumnM)oldColumns.get(0).clone());
		newColumns.add((TableColumnM)oldColumns.get(1).clone());
		newColumns.add((TableColumnM)oldColumns.get(2).clone());
	}
	
	/**
	 * クローンを用いてコピーしたオブジェクトリストを渡した場合は空のリストが
	 * 返ることをテスト
	 */
	public void testCompareEqualsList() {
		List<? extends AbstractDifferenceObject> dclist = comc.compare(oldColumns, newColumns);
		
		assertTrue(dclist.isEmpty());
		
		oldColumns.add(new TableColumnM("table3", "NUMBER", 100, new ColumnConstraint()));
		
		newColumns.add((TableColumnM)oldColumns.get(3).clone());
		
		dclist = null;
		dclist = comc.compare(oldColumns, newColumns);
		
		assertTrue(dclist.isEmpty());
	}
	
	/**
	 * 二つのTableColumnMオブジェクトを比較する処理のテスト
	 */
	public void testDiffTableColumn() {
		TableColumnM t1 = new TableColumnM("Table", "VARCHAR", 0, new ColumnConstraint());
		TableColumnM t2 = (TableColumnM) t1.clone();
		
		DifferenceColumn dc = (DifferenceColumn) comc.generateSubtraction(t1, t2);
		
		assertNull(dc);
		
		t2.setName("ffas");
		t2.getConstraint().setPrimary(true);
		
		dc = null;
		dc = (DifferenceColumn) comc.generateSubtraction(t1, t2);
		
		assertNotNull(dc);
		
		assertEquals(dc.getDifferentChar(), new Character('U'));
		assertEquals(dc.getObjectId(), t1.getId());
		
		assertTrue(dc.isColumnNameFlag());
		assertEquals(dc.getColumnName(), "ffas");
		
		assertTrue(dc.isColumnPrimaryFlag());
		assertTrue(dc.isColumnPrimary());
		
		assertFalse(dc.isColumnTypeFlag());
		assertFalse(dc.isColumnLengthFlag());
		assertFalse(dc.isColumnPrecisionLengthFlag());
		assertFalse(dc.isColumnIndexFlag());
		assertFalse(dc.isColumnCheckFlag());
		assertFalse(dc.isColumnDefaultFlag());
		assertFalse(dc.isColumnUniqueFlag());
		assertFalse(dc.isColumnNotNullFlag());
		assertFalse(dc.isColumnForeignFlag());
	}
	
	/**
	 * 二つのTableColumnMオブジェクトを比較したとき、属性の型だけが変化していた場合は
	 * 型の長さ、精度も変更されたとみなして値を設定することのテスト
	 */
	public void testDiffTableColumnType() {
		TableColumnM t1 = new TableColumnM("Table", "VARCHAR", 0, new ColumnConstraint());
		TableColumnM t2 = (TableColumnM) t1.clone();
		t2.setType("NUMBER");
		
		DifferenceColumn dc = (DifferenceColumn) comc.generateSubtraction(t1, t2);
		
		assertTrue(dc.isColumnTypeFlag());
		assertEquals(dc.getColumnType(), "NUMBER");
		
		assertTrue(dc.isColumnLengthFlag());
		assertEquals(dc.getColumnLength(), new Integer(0));
		
		assertTrue(dc.isColumnPrecisionLengthFlag());
		assertEquals(dc.getColumnPrecisionLength(), new Integer(0));
		
		assertFalse(dc.isColumnNameFlag());
		assertFalse(dc.isColumnIndexFlag());
		assertFalse(dc.isColumnPrimary());
		assertFalse(dc.isColumnPrimaryConstNameFlag());
		assertFalse(dc.isColumnCheckFlag());
		assertFalse(dc.isColumnDefaultFlag());
		assertFalse(dc.isColumnUniqueFlag());
		assertFalse(dc.isColumnNotNullFlag());
		assertFalse(dc.isColumnForeignFlag());
		
	}
	
	/**
	 * 二つのTableColumnMオブジェクトを比較したとき、属性の長さだけが変化していた場合は
	 * 型も変更されたとみなして値を設定することのテスト。精度も変更。
	 */
	public void testDiffTableColumnLength() {
		TableColumnM t1 = new TableColumnM("Table", "NUMBER", 0, new ColumnConstraint());
		TableColumnM t2 = (TableColumnM) t1.clone();
		t2.setLength(11);
		
		DifferenceColumn dc = (DifferenceColumn) comc.generateSubtraction(t1, t2);
		
		assertTrue(dc.isColumnTypeFlag());
		assertEquals(dc.getColumnType(), "NUMBER");
		assertTrue(dc.isColumnLengthFlag());
		assertEquals(dc.getColumnLength(), new Integer(11));
		assertTrue(dc.isColumnPrecisionLengthFlag());
		assertEquals(dc.getColumnPrecisionLength(), new Integer(0));
		
		assertFalse(dc.isColumnNameFlag());
		assertFalse(dc.isColumnIndexFlag());
		assertFalse(dc.isColumnPrimary());
		assertFalse(dc.isColumnPrimaryConstNameFlag());
		assertFalse(dc.isColumnCheckFlag());
		assertFalse(dc.isColumnDefaultFlag());
		assertFalse(dc.isColumnUniqueFlag());
		assertFalse(dc.isColumnNotNullFlag());
		assertFalse(dc.isColumnForeignFlag());
	}
	
	/**
	 * 二つのTableColumnMオブジェクトを比較したとき、属性の精度だけが変化していた場合は
	 * 型も変更されたとみなして値を設定することのテスト。長さも変更
	 */
	public void testDiffColumnTablePrecisionLength() {
		TableColumnM t1 = new TableColumnM("Table", "NUMBER", 0, new ColumnConstraint());
		TableColumnM t2 = (TableColumnM) t1.clone();
		t2.setPrecisionLength(4);
		
		DifferenceColumn dc = (DifferenceColumn) comc.generateSubtraction(t1, t2);
		
		assertTrue(dc.isColumnTypeFlag());
		assertEquals(dc.getColumnType(), "NUMBER");
		assertTrue(dc.isColumnLengthFlag());
		assertEquals(dc.getColumnLength(), new Integer(0));
		assertTrue(dc.isColumnPrecisionLengthFlag());
		assertEquals(dc.getColumnPrecisionLength(), new Integer(4));
		
		assertFalse(dc.isColumnNameFlag());
		assertFalse(dc.isColumnIndexFlag());
		assertFalse(dc.isColumnPrimary());
		assertFalse(dc.isColumnPrimaryConstNameFlag());
		assertFalse(dc.isColumnCheckFlag());
		assertFalse(dc.isColumnDefaultFlag());
		assertFalse(dc.isColumnUniqueFlag());
		assertFalse(dc.isColumnNotNullFlag());
		assertFalse(dc.isColumnForeignFlag());
	}
	
	/**
	 * 二つのTableColumnMオブジェクトを比較したとき、デフォルト値が変化した場合は
	 * 属性の型まで取得できる事をテスト
	 */
	public void testDiffTableDefault() {
		TableColumnM t1 = new TableColumnM("Table", "VARCHAR2", 0, new ColumnConstraint());
		TableColumnM t2 = (TableColumnM) t1.clone();
		
		ColumnConstraint cc = new ColumnConstraint();
		cc.setDefault("Gloovy");
		t2.setConstraint(cc);
		
		DifferenceColumn dc = (DifferenceColumn) comc.generateSubtraction(t1, t2);
		
		assertTrue(dc.isColumnDefaultFlag());
		assertEquals(dc.getColumnDefault(), "Gloovy");
		
		assertEquals(dc.getColumnType(), "VARCHAR2");

		assertFalse(dc.isColumnNameFlag());
		assertFalse(dc.isColumnTypeFlag());
		assertFalse(dc.isColumnLengthFlag());
		assertFalse(dc.isColumnPrecisionLengthFlag());
		assertFalse(dc.isColumnIndexFlag());
		assertFalse(dc.isColumnPrimary());
		assertFalse(dc.isColumnPrimaryConstNameFlag());
		assertFalse(dc.isColumnCheckFlag());
		assertFalse(dc.isColumnUniqueFlag());
		assertFalse(dc.isColumnNotNullFlag());
		assertFalse(dc.isColumnForeignFlag());
	}
	
	/**
	 * 二つのTableColumnMオブジェクトを比較したとき、Checkに制約に変更が
	 * あった場合はCheck制約の制約名も一緒にコピーされる事をテスト
	 */
	public void testDiffTableCheck() {
		TableColumnM t1 = new TableColumnM("Table", "VARCHAR2", 0, new ColumnConstraint());
		TableColumnM t2 = (TableColumnM) t1.clone();
		
		ColumnConstraint cc = new ColumnConstraint();
		cc.setCheck("sss > 0");
		cc.setCheckConstName("SYS_000");
		t2.setConstraint(cc);
		
		DifferenceColumn dc = (DifferenceColumn) comc.generateSubtraction(t1, t2);
		
		assertTrue(dc.isColumnCheckFlag());
		assertEquals(dc.getColumnCheck(), "sss > 0");
		
		assertEquals(dc.getColumnCheckConst(), "SYS_000");
		
		assertFalse(dc.isColumnNameFlag());
		assertFalse(dc.isColumnTypeFlag());
		assertFalse(dc.isColumnLengthFlag());
		assertFalse(dc.isColumnPrecisionLengthFlag());
		assertFalse(dc.isColumnIndexFlag());
		assertFalse(dc.isColumnPrimary());
		assertFalse(dc.isColumnPrimaryConstNameFlag());
		assertFalse(dc.isColumnDefaultFlag());
		assertFalse(dc.isColumnUniqueFlag());
		assertFalse(dc.isColumnNotNullFlag());
		assertFalse(dc.isColumnForeignFlag());
		
		//
		cc = new ColumnConstraint();
		cc.setCheck("sss > 0");
		cc.setCheckConstName("SYS_000");
		t1 = new TableColumnM("Table", "VARCHAR2", 0, cc);
		t2 = (TableColumnM) t1.clone();
		t2.getConstraint().setCheck(null);
		
		assertEquals(t1.getConstraint().getCheck(), "sss > 0");
		assertNull(t2.getConstraint().getCheck());
		
		dc = (DifferenceColumn) comc.generateSubtraction(t1, t2);
		
		assertTrue(dc.isColumnCheckFlag());
		assertNull(dc.getColumnCheck());
		
		assertEquals(dc.getColumnCheckConst(), "SYS_000");
		
		assertFalse(dc.isColumnNameFlag());
		assertFalse(dc.isColumnTypeFlag());
		assertFalse(dc.isColumnLengthFlag());
		assertFalse(dc.isColumnPrecisionLengthFlag());
		assertFalse(dc.isColumnIndexFlag());
		assertFalse(dc.isColumnPrimary());
		assertFalse(dc.isColumnPrimaryConstNameFlag());
		assertFalse(dc.isColumnDefaultFlag());
		assertFalse(dc.isColumnUniqueFlag());
		assertFalse(dc.isColumnNotNullFlag());
		assertFalse(dc.isColumnForeignFlag());
	}
	
	/**
	 * TableColumnMオブジェクトからDifferenceColumnオブジェクトを生成する処理の
	 * テスト
	 */
	public void testGenereteDifferenceColumn() {
		ColumnConstraint cc = new ColumnConstraint();
		cc.setPrimary(true);
		cc.setPrimaryConstName("PKNAME");
		cc.setIndex(IndexTypes.NORMAL);
		TableColumnM tcm = new TableColumnM("table1", "NUMBER", 150, cc);
		
		DifferenceColumn dc = (DifferenceColumn) comc.generateDifferenceInformation(tcm);
		
		assertEquals(dc.getDifferentChar(), new Character('A'));
		assertEquals(dc.getObjectId(), tcm.getId());
		
		assertTrue(dc.isColumnNameFlag());
		assertTrue(dc.isColumnTypeFlag());
		assertTrue(dc.isColumnLengthFlag());
		assertTrue(dc.isColumnPrecisionLengthFlag());
		assertTrue(dc.isColumnIndexFlag());
		assertTrue(dc.isColumnPrimaryFlag());
		assertTrue(dc.isColumnPrimaryConstNameFlag());
		
		assertEquals(dc.getColumnName(), "table1");
		assertEquals(dc.getColumnType(), "NUMBER");
		assertEquals(dc.getColumnLength(), new Integer(150));
		assertEquals(dc.getColumnPrecisionLength(), new Integer(0));
		assertEquals(dc.getColumnIndex(), IndexTypes.NORMAL);
		assertTrue(dc.isColumnPrimary());
		assertEquals(dc.getColumnPrimaryConstName(), "PKNAME");
		
		assertFalse(dc.isColumnCheckFlag());
		assertFalse(dc.isColumnDefaultFlag());
		assertFalse(dc.isColumnUniqueFlag());
		assertFalse(dc.isColumnNotNullFlag());
		assertFalse(dc.isColumnForeignFlag());
	}
	
	/**
	 * クローンを用いてコピーしたオブジェクトリストを渡した場合でカラムの値が変わっている
	 * 時はアップデートを表すDifferenceColumnが返ることをテスト
	 */
	public void testCompareNotEqualsList() {
		newColumns.get(0).setName("tablex");
		
		List<? extends AbstractDifferenceObject> dclist = comc.compare(oldColumns, newColumns);
		
		assertFalse(dclist.isEmpty());
		DifferenceColumn dc = (DifferenceColumn) dclist.get(0);
		
		assertTrue(dc.isColumnNameFlag());
		assertFalse(dc.isColumnTypeFlag());
		assertFalse(dc.isColumnLengthFlag());
		assertFalse(dc.isColumnPrecisionLengthFlag());
		assertFalse(dc.isColumnIndexFlag());
		assertFalse(dc.isColumnPrimary());
		assertFalse(dc.isColumnPrimaryConstNameFlag());
		assertFalse(dc.isColumnCheckFlag());
		assertFalse(dc.isColumnDefaultFlag());
		assertFalse(dc.isColumnUniqueFlag());
		assertFalse(dc.isColumnNotNullFlag());
		assertFalse(dc.isColumnForeignFlag());
		
		assertEquals(dc.getDifferentChar(), new Character('U'));
		assertEquals(dc.getColumnName(), "tablex");
		assertEquals(dc.getObjectId(), newColumns.get(0).getId());
		
		try {
			dc = (DifferenceColumn) dclist.get(1);
			fail();
		} catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 新しいカラムに追加されたデータがあれば追加データを表す
	 * DifferenceColumnオブジェクトが返る
	 */
	public void testCompareAddList() {
		TableColumnM tcm = new TableColumnM("table3", "NUMBER", 10, new ColumnConstraint());
		tcm.setTableName("tableid");
		newColumns.add(tcm);
		
		List<? extends AbstractDifferenceObject> dclist = comc.compare(oldColumns, newColumns);
		
		assertFalse(dclist.isEmpty());
		DifferenceColumn dc = (DifferenceColumn) dclist.get(0);
		
		assertTrue(dc.isColumnNameFlag());
		assertTrue(dc.isColumnTypeFlag());
		assertTrue(dc.isColumnLengthFlag());
		assertTrue(dc.isColumnPrecisionLengthFlag());
		assertFalse(dc.isColumnIndexFlag());
		assertFalse(dc.isColumnPrimary());
		assertFalse(dc.isColumnPrimaryConstNameFlag());
		assertFalse(dc.isColumnCheckFlag());
		assertFalse(dc.isColumnDefaultFlag());
		assertFalse(dc.isColumnUniqueFlag());
		assertFalse(dc.isColumnNotNullFlag());
		assertFalse(dc.isColumnForeignFlag());
		
		assertEquals(dc.getDifferentChar(), new Character('A'));
		assertEquals(dc.getObjectId(), tcm.getId());
		assertEquals(dc.getColumnName(), "table3");
		assertEquals(dc.getColumnType(), "NUMBER");
		assertEquals(dc.getColumnLength(), new Integer(10));
		assertEquals(dc.getColumnPrecisionLength(), new Integer(0));
		assertEquals(dc.getColumnTableName(), "tableid");
		
		try {
			dc = (DifferenceColumn) dclist.get(1);
			fail();
		} catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 削除されたデータがあれば、削除データを表すDifferenceColumnオブジェクト
	 * を返す。
	 */
	public void testCompareRemoveList() {
		newColumns.remove(1);
		
		List<? extends AbstractDifferenceObject> dclist = comc.compare(oldColumns, newColumns);
		
		assertFalse(dclist.isEmpty());
		DifferenceColumn dc = (DifferenceColumn) dclist.get(0);
		
		assertEquals(dc.getDifferentChar(), new Character('D'));
		assertEquals(dc.getObjectId(), oldColumns.get(1).getId());
		
		try {
			dc = (DifferenceColumn) dclist.get(1);
			fail();
		} catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 変更、追加、削除がおこなわれたリストが入れられた時のテスト
	 */
	public void testCompareComplexList() {
		ColumnConstraint cs = new ColumnConstraint();
		ForeignConstraint fc = new ForeignConstraint();
		fc.setReferenceTable("goods");
		cs.setForeign(fc);
		
		newColumns.get(0).setName("tablefff");
		newColumns.get(0).setConstraint(cs);
		newColumns.remove(1);
		ColumnConstraint cc = new ColumnConstraint();
		cc.setPrimaryConstName("primarys");
		cc.setIndex(IndexTypes.BITMAP);
		TableColumnM tcm = new TableColumnM("tablex", "NUMBER", 10, cc);
		newColumns.add(tcm);
		
		List<? extends AbstractDifferenceObject> dlist = comc.compare(oldColumns, newColumns);
		
		DifferenceColumn dc = (DifferenceColumn) dlist.get(1);
		assertEquals(dc.getDifferentChar(), new Character('D'));
		assertEquals(dc.getObjectId(), oldColumns.get(1).getId());
		
		dc = (DifferenceColumn) dlist.get(0);
		assertEquals(dc.getDifferentChar(), new Character('A'));
		assertEquals(dc.getObjectId(), tcm.getId());
		assertTrue(dc.isColumnNameFlag());
		assertTrue(dc.isColumnTypeFlag());
		assertTrue(dc.isColumnLengthFlag());
		assertTrue(dc.isColumnPrecisionLengthFlag());
		assertTrue(dc.isColumnIndexFlag());
		assertTrue(dc.isColumnPrimaryConstNameFlag());
		assertFalse(dc.isColumnCheckFlag());
		assertFalse(dc.isColumnDefaultFlag());
		assertFalse(dc.isColumnUniqueFlag());
		assertFalse(dc.isColumnNotNullFlag());
		assertFalse(dc.isColumnForeignFlag());
		
		assertEquals(dc.getColumnName(), "tablex");
		assertEquals(dc.getColumnType(), "NUMBER");
		assertEquals(dc.getColumnLength(), new Integer(10));
		assertEquals(dc.getColumnPrecisionLength(), new Integer(0));
		assertEquals(dc.getColumnIndex(), IndexTypes.BITMAP);
		assertEquals(dc.getColumnPrimaryConstName(), "primarys");
		
		dc = (DifferenceColumn) dlist.get(2);
		assertEquals(dc.getDifferentChar(), new Character('U'));
		assertEquals(dc.getObjectId(), newColumns.get(0).getId());
		assertTrue(dc.isColumnNameFlag());
		assertFalse(dc.isColumnTypeFlag());
		assertFalse(dc.isColumnLengthFlag());
		assertFalse(dc.isColumnPrecisionLengthFlag());
		assertFalse(dc.isColumnIndexFlag());
		assertFalse(dc.isColumnPrimaryConstNameFlag());
		assertFalse(dc.isColumnCheckFlag());
		assertFalse(dc.isColumnDefaultFlag());
		assertFalse(dc.isColumnUniqueFlag());
		assertFalse(dc.isColumnNotNullFlag());
		assertTrue(dc.isColumnForeignFlag());
		
		assertEquals(dc.getColumnName(), "tablefff");
		assertEquals(dc.getColumnReferenceTable(), "goods");
		
		try {
			dlist.get(3);
			fail();
		} catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ２つのカラムを比べるとき、columnForeignNameはほかの外部キー制約が
	 * 変更されているときのみ更新する。
	 */
	public void testReferenceColumn() {
		ForeignConstraint fc = new ForeignConstraint();
		fc.setConstraintName("constraint");
		fc.setReferenceConstraintName("reference");
		fc.setReferenceTable("tablex");
		ColumnConstraint cc = new ColumnConstraint();
		cc.setForeign(fc);
		TableColumnM tcm = new TableColumnM("table1", "CHAR", 10, cc);
		
		TableColumnM tcm2 = (TableColumnM) tcm.clone();
		tcm2.setName("table2");
		
		CompareColumn comc = new CompareColumn();
		
		DifferenceColumn dc = (DifferenceColumn) comc.generateSubtraction(tcm, tcm2);
		
		assertEquals(dc.getColumnName(), "table2");
		assertNull(dc.getColumnForeignName());
		assertNull(dc.getColumnReferenceConstraintName());
		
		assertTrue(dc.isColumnNameFlag());
		assertFalse(dc.isColumnForeignFlag());
		
		
		TableColumnM tcm3 = (TableColumnM) tcm.clone();
		tcm3.getConstraint().getForeign().setDeleteType("CASCADE");
		
		DifferenceColumn dc2 = (DifferenceColumn) comc.generateSubtraction(tcm, tcm3);
		
		assertEquals(dc2.getColumnForeignName(), "constraint");
		assertEquals(dc2.getColumnDeleteType(), "CASCADE");
		
		assertTrue(dc2.isColumnForeignFlag());
	}
	
}
