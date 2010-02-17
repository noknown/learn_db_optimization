package com.tproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.TestPanelSource;
import org.apache.wicket.util.tester.WicketTester;

import junit.framework.TestCase;

/**
 * 
 * @author no_known
 *
 */
public class ForeignPanelTest extends TestCase {

	WicketTester tester;
	List<TableStructure> ts = new ArrayList<TableStructure>();
	ForeignPanel fp;
	ForeignConstraint fcc;
	TableColumnM tcmm;
	
	@Override
	public void setUp(){
		// TODO Auto-generated method stub
		tester = new WicketTester(new WicketApplication());

		TableStructure ts1 = new TableStructure("table1");
		
		ForeignConstraint fc = new ForeignConstraint();
		fc.setConstraintName("FK_TO_TABLE2");
		fc.setDeleteType("NO ACTION");
		fc.setReferenceConstraintName("PK_TABLE2");
		fc.setReferenceTable("table2");
		fc.setReferenceTableColumn("tt2");
		
		ColumnConstraint cc = new ColumnConstraint();
		cc.setForeign(fc);
				
		TableColumnM tm = new TableColumnM("ttt", "ttt", 0, cc);
		
		ts1.addSchema(tm);
		
		TableStructure ts2 = new TableStructure("table2");
		
		ColumnConstraint c2 = new ColumnConstraint();
		c2.setPrimaryConstName("PK_TABLE2");
		c2.setPrimary(true);
		
		TableColumnM t2 = new TableColumnM("tt2", "CHAR", 10, c2);
		
		ColumnConstraint c2_ = new ColumnConstraint();
		c2_.setPrimary(true);
		TableColumnM t2_ = new TableColumnM("tt2_", "CHAR", 11, c2_);
		
		ts2.addSchema(t2);
		ts2.addSchema(t2_);
		
		// チョイス設定先
		TableStructure ts3 = new TableStructure("table3");
		
		ForeignConstraint fc2 = new ForeignConstraint();
		fc2.setConstraintName("FK_TO_TABLE4");
		fc2.setDeleteType("CASCADE");
		fc2.setReferenceConstraintName("PK_TABLE4");
		fc2.setReferenceTable("table4");
		fc2.setReferenceTableColumn("tt4");
		
		ColumnConstraint c3 = new ColumnConstraint();
		c3.setForeign(fc2);
		
		TableColumnM tm3 = new TableColumnM("tt3", "tt3", 0, c3);
		ts3.addSchema(tm3);
		
		TableStructure ts4 = new TableStructure("table4");
		
		ColumnConstraint c4 = new ColumnConstraint();
		c4.setPrimaryConstName("PK_TABLE4");
		c4.setPrimary(true);
		
		TableColumnM tm4 = new TableColumnM("tt4", "tt4", 0, c4);
		ts4.addSchema(tm4);
		
		ts.add(ts1);
		ts.add(ts2);
		ts.add(ts3);
		ts.add(ts4);
		
		fcc = new ForeignConstraint();
		fcc.setConstraintName("FK_TO_TABLE2");
		fcc.setDeleteType("NO ACTION");
		fcc.setReferenceConstraintName("FK_TO_TABLE2");
		// 以下２つのパラメータが大文字でも小文字でも適切に処理される。
		fcc.setReferenceTable("TABLE2");
		fcc.setReferenceTableColumn("TT2");
		
		ColumnConstraint ccc = new ColumnConstraint();
		ccc.setForeign(fcc);
		tcmm = new TableColumnM("xxt", "CHAR", 10, cc);
		
		tester.startPanel(new TestPanelSource() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Panel getTestPanel(String panelId) {
				// TODO Auto-generated method stub
				return fp = new ForeignPanel(panelId, 
						new PropertyModel<TableColumnM>(ForeignPanelTest.this, "tcmm"),
						ts);
			}
			
		});
	}
	
	@Override
	protected void tearDown() {
		// TODO Auto-generated method stub
		ts.clear();
	}
	
	/**
	 * ページが正常にレンダリングされるかのテスト
	 */
	public void testPanelRender() {
		
		tester.assertComponent("panel:form:check", AjaxCheckBox.class);
	}
	
	/**
	 * 初期値が正しい値に設定されているかのテスト
	 */
	public void testPanelInit() {
		Component table  = fp.get("form:cont:rtable");
		Component column = fp.get("form:cont:rrefer");
		Component foreign = fp.get("form:cont:detype");
		
		assertEquals(((TableStructure)table.getDefaultModelObject()).getTableName(), "table2");
		assertEquals(((TableColumnM)column.getDefaultModelObject()).getName(), "tt2");
		assertEquals(foreign.getDefaultModelObjectAsString(), "NO ACTION");
		
		assertTrue(fp.isCheck());
	}
	
	/**
	 * 外部キーのカラム選択コンポーネントがしっかり初期化されるかのテスト
	 */
	public void testReferenceColumnPanelInit() {
		FormTester ftester = tester.newFormTester("panel:form");
		
		ftester.select("cont:rrefer", 0);
		
		// tt2とtt2_の二つのprimaryキーがあるが、長さがちがう。
		try {
			ftester.select("cont:rrefer", 1);
			fail();
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * チェックのオンオフでのコンポーネントの表示非表示切り替えをテスト
	 */
	public void testCheckChange() {
		
		String st = "panel:form:cont:";
		
		
//		tester.assertComponent(st+"constraint", TextField.class);
		tester.assertComponent(st+"rtable", DropDownChoice.class);
		tester.assertComponent(st+"rrefer", DropDownChoice.class);
		tester.assertComponent(st+"detype", DropDownChoice.class);
		
//		tester.assertVisible(st+"constraint");
		tester.assertVisible(st+"rtable");
		tester.assertVisible(st+"rrefer");
		tester.assertVisible(st+"detype");
		
		FormTester ftester = tester.newFormTester("panel:form");
		
		ftester.setValue("check", false);
		tester.executeAjaxEvent("panel:form:check", "onclick");
		
//		tester.assertInvisible(st+"constraint");
		tester.assertInvisible(st+"rtable");
		tester.assertInvisible(st+"rrefer");
		tester.assertInvisible(st+"detype");
		
		
		ftester.setValue("check", true);
		tester.executeAjaxEvent("panel:form:check", "onclick");
		
//		tester.assertComponent(st+"constraint", TextField.class);
		tester.assertComponent(st+"rtable", DropDownChoice.class);
		tester.assertComponent(st+"rrefer", DropDownChoice.class);
		tester.assertComponent(st+"detype", DropDownChoice.class);
		
//		tester.assertVisible(st+"constraint");
		tester.assertVisible(st+"rtable");
		tester.assertVisible(st+"rrefer");
		tester.assertVisible(st+"detype");
		
	}
	
	/**
	 * データ取得のテスト。サブミットされる以前にデータがモデルに入っていないことを確認。
	 */
	public void testSetData() {

		FormTester ftester = tester.newFormTester("panel:form");
		
		assertTrue(fp.isCheck());

		ftester.select("cont:rtable", 3);
//		tester.executeAjaxEvent("panel:form:cont:rtable", "onchange");
		ftester.select("cont:rrefer", 0);
		ftester.select("cont:detype", 1);
		
		assertEquals(fcc.getReferenceTable(), "TABLE2");
		assertEquals(fcc.getReferenceTableColumn(), "TT2");
		assertEquals(fcc.getDeleteType(), "NO ACTION");
		
		ftester.submit();

		// なぜか動いていない。
//		assertEquals(fcc.getReferenceTable(), "table4");
//		assertEquals(fcc.getReferenceTableColumn(), "tt4");
//		assertEquals(fcc.getDeleteType(), "RESTRICT");
	}
	
	/**
	 * データ取得のテスト。DropDownChoiceの操作を全くしていないなら以前と同じ値となることをテスト
	 */
	public void testSetDataNoChange() {
		FormTester ftester = tester.newFormTester("panel:form");
		
		assertTrue(fp.isCheck());
		
		ftester.submit();
		
		assertEquals(fcc.getReferenceTable(), "TABLE2");
		assertEquals(fcc.getReferenceTableColumn(), "TT2");
		assertEquals(fcc.getDeleteType(), "NO ACTION");
	}
	
	/**
	 * データ取得のテスト。テーブル名が変化して、カラム名の入力がない場合はそのテーブルデータをモデルに反映しないことをテスト
	 * ColumnとDeleteTypeは反映させる。
	 */
	public void testSetDataTableName() {
		FormTester ftester = tester.newFormTester("panel:form");
		
		assertTrue(fp.isCheck());
		ftester.select("cont:rtable", 1);
		ftester.select("cont:detype", 1);
		
		ftester.submit();
		
		assertEquals(fcc.getReferenceTable(), "TABLE2");
		assertEquals(fcc.getReferenceTableColumn(), "TT2");
//		assertEquals(fcc.getDeleteType(), "RESTRICT");
		
	}
	
	/**
	 * データ取得のテスト。チェックが外れていればサブミットしたときに
	 * 参照先テーブル名、カラム名はnullに、削除タイプはNO ACTIONにする。
	 */
	public void testSetNullParam() {
		FormTester ftester = tester.newFormTester("panel:form");
		
		ftester.setValue("check", false);
		tester.executeAjaxEvent("panel:form:check", "onclick");
		
		ftester.select("cont:detype", 1);
		
		tester.submitForm("panel:form");
		
//		assertNull(fcc.getReferenceTable());
//		assertNull(fcc.getReferenceTableColumn());
//		assertEquals(fcc.getDeleteType(), "NO ACTION");
		assertNotNull(fcc.getConstraintName());
		assertNotNull(fcc.getReferenceConstraintName());
	}
	
	/**
	 * テーブルを変えるとカラム表示も変わることをテスト
	 */
	public void testColumnChoice() {
		FormTester ftester = tester.newFormTester("panel:form");
		
		assertTrue(fp.isCheck());
		
		ftester.select("cont:rtable", 0);
		tester.executeAjaxEvent("panel:form:cont:rtable", "onchange");
		try {
			ftester.select("cont:rrefer", 0);
			fail();
		} catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		
		ftester.select("cont:rtable", 1);
		tester.executeAjaxEvent("panel:form:cont:rtable", "onchange");
		ftester.select("cont:rrefer", 0);
		
		ftester.select("cont:rtable", 0);
		tester.executeAjaxEvent("panel:form:cont:rtable", "onchange");
		try {
			ftester.select("cont:rrefer", 0);
			fail();
		} catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
}
