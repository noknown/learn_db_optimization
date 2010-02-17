package com.tproject;

import junit.framework.TestCase;
/**
 * 
 * @author no_known
 *
 */
public class ForeignConstraintTest extends TestCase {
	
	ForeignConstraint fc;
	
	@Override
	protected void setUp() {
		// TODO Auto-generated method stub
		fc = new ForeignConstraint();
	}
	
	/**
	 * 制約名のテスト。Oracleの命名規約にしたがったものだけが設定される。
	 * 規約にしたがわないものが入力された場合は無視する。
	 */
	public void testConstraintName() {
		assertNull(fc.getConstraintName());
		
		fc.setConstraintName("primarykey");
		
		assertEquals(fc.getConstraintName(), "primarykey");
		
		fc.setConstraintName("");
		
		assertEquals(fc.getConstraintName(), "primarykey");
	}
	
	/**
	 * テーブル名のテスト。Oracleの命名規約にしたがったものだけが設定される。
	 * 規約にしたがわないものが入力された場合は無視する。
	 */
	public void testReferenceTable() {
		fc.setReferenceTable("primarykey");
		
		assertEquals(fc.getReferenceTable(), "primarykey");
		
		fc.setReferenceTable("");
		
		assertEquals(fc.getReferenceTable(), "primarykey");
	}
	
	/**
	 * カラム名のテスト。Oracleの命名規約にしたがったものだけが設定される。
	 * 規約にしたがわないものが入力された場合は無視する。
	 */
	public void testReferenceTableColumn() {
		fc.setReferenceTableColumn("column1");
		
		assertEquals(fc.getReferenceTableColumn(), "column1");
		
		fc.setReferenceTableColumn("");
		
		assertEquals(fc.getReferenceTableColumn(), "column1");
	}
	
	/**
	 * 初期化メソッドのテスト
	 */
	public void testInit() {
		fc.setReferenceTable("table2");
		fc.setReferenceTableColumn("column1");
		fc.setDeleteType("CASCADE");
		fc.setConstraintName("const1");
		fc.setReferenceConstraintName("rconst1");
		
		assertEquals(fc.getReferenceTable(), "table2");
		assertEquals(fc.getReferenceTableColumn(), "column1");
		assertEquals(fc.getDeleteType(), "CASCADE");
		assertEquals(fc.getConstraintName(), "const1");
		assertEquals(fc.getReferenceConstraintName(), "rconst1");
		
		fc.init();
		
		assertNull(fc.getReferenceTable());
		assertNull(fc.getReferenceTableColumn());
		assertEquals(fc.getDeleteType(), "NO ACTION");
		assertEquals(fc.getConstraintName(), "const1");
		assertEquals(fc.getReferenceConstraintName(), "rconst1");
	}
	
	/**
	 * 参照先テーブルの制約名のテスト。テーブル名のテスト。Oracleの命名規約にしたがったものだけが設定される。
	 * 規約にしたがわないものが入力された場合は無視する。
	 */
	public void testReferenceConstraintName() {
		fc.setReferenceConstraintName("primarykey");
		
		assertEquals(fc.getReferenceConstraintName(), "primarykey");
		
		fc.setReferenceConstraintName("");
		
		assertEquals(fc.getReferenceConstraintName(), "primarykey");
	}
	
	/**
	 * データ削除の参照操作名のテスト。初期値は"NO ACTION"
	 * データとして受けとるのは
	 * <p>
	 *  NO ACTION<br/>
	 *  RESTRICT<br/>
	 *  SET NULL<br/>
	 *  SET DEFAULT<br/>
	 *  CASCADE<br/>
	 * </p>
	 * だけであり、それ以外の値は無視する。
	 * 大文字小文字の区別はしない。
	 */
	public void testDeleteType() {
		if(fc.getDeleteType().equals("NO ACTION"));
		
		String[] str1 = new String[]{"NO ACTION", "RESTRICT", "SET NULL", "SET DEFAULT", "CASCADE"};
		String[] str2 = new String[]{"No Action", "restrict", "SEt NuLl", "sEt deFAuLt", "CASCADE"};
		for(int i = 0; i < str1.length; i++) {
			fc.setDeleteType(str1[i]);

			assertEquals(fc.getDeleteType(), str1[i]);
			
			fc.setDeleteType(str2[i]);
			
			assertEquals(fc.getDeleteType(), str1[i]);
		}
		
		fc.setDeleteType("sadfasdse");
		
		assertEquals(fc.getDeleteType(), "CASCADE");
		
		fc.setDeleteType(null);
		
		assertEquals(fc.getDeleteType(), "CASCADE");
	}
	
	/**
	 * cloneをオーバーライドした、ディープコピーを行う関数のテスト
	 */
	public void testDeepCopy() {
		
		ForeignConstraint fc1 = (ForeignConstraint) fc.clone();
		assertNull(fc1.getConstraintName());
		assertNull(fc1.getReferenceTable());
		assertNull(fc1.getReferenceTableColumn());
		assertNull(fc1.getReferenceConstraintName());
		assertEquals(fc1.getDeleteType(), "NO ACTION");
		
		fc.setConstraintName("fnames");
		fc.setReferenceTable("table1");
		fc.setReferenceTableColumn("column1");
		fc.setReferenceConstraintName("fk_const");
		fc.setDeleteType("NO ACTION");
		
		ForeignConstraint fc2 = (ForeignConstraint) fc.clone();
		
		assertNotSame(fc, fc2);
		
		assertEquals(fc.getConstraintName(), fc2.getConstraintName());
		assertNotSame(fc.getConstraintName(), fc2.getConstraintName());
		
		assertEquals(fc.getReferenceTable(), fc2.getReferenceTable());
		assertNotSame(fc.getReferenceTable(), fc2.getReferenceTable());
		
		assertEquals(fc.getReferenceTableColumn(), fc2.getReferenceTableColumn());
		assertNotSame(fc.getReferenceTableColumn(), fc2.getReferenceTableColumn());
		
		assertEquals(fc.getReferenceConstraintName(), fc2.getReferenceConstraintName());
		assertNotSame(fc.getReferenceConstraintName(), fc2.getReferenceConstraintName());
		
		assertEquals(fc.getDeleteType(), fc.getDeleteType());
		assertNotSame(fc.getDeleteType(), fc2.getDeleteType());
	}

}
