package com.tproject;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.http.TestFilePageStore;
import org.apache.wicket.util.tester.DummyPanelPage;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.TestPanelSource;
import org.apache.wicket.util.tester.WicketTester;

import junit.framework.TestCase;

/**
 * @author no_known
 *
 */
public class ConstraintViewPageTest extends TestCase{

	WicketTester tester;
	DatabaseStructure dbstruc;
	TableColumnM tcm;
	
	@Override
	protected void setUp() {
		// TODO Auto-generated method stub
		tester = new WicketTester(new WicketApplication());
		
		dbstruc = new DatabaseStructure("database1");
		
		TableStructure ts1 = new TableStructure("table1");
		
		ForeignConstraint fc = new ForeignConstraint();
		fc.setConstraintName("FK_TO_TABLE2");
		fc.setDeleteType("NO ACTION");
		fc.setReferenceConstraintName("PK_TABLE2");
		fc.setReferenceTable("table2");
		
		ColumnConstraint cc = new ColumnConstraint();
		cc.setForeign(fc);
				
		TableColumnM tm = new TableColumnM("", "", 0, cc);
		
		ts1.addSchema(tm);
		
		TableStructure ts2 = new TableStructure("table2");
		
		ColumnConstraint c2 = new ColumnConstraint();
		c2.setPrimaryConstName("PK_TABLE2");
		
		TableColumnM t2 = new TableColumnM("", "", 0, c2);
		
		ts2.addSchema(t2);
		
		dbstruc.addTable(ts1);
		dbstruc.addTable(ts2);
		
		
		ColumnConstraint cc2 = new ColumnConstraint();
		cc2.setPrimaryConstName("NUM_PK");
		ForeignConstraint fc2 = new ForeignConstraint();
		fc2.setConstraintName("NUM_FK");
		cc2.setForeign(fc2);
		cc2.setIndex(IndexTypes.NORMAL);
		tcm = new TableColumnM("num", "NUMBER", 10, cc2);
		tcm.setPrecisionLength(2);
		
		PageParameters param = new PageParameters();
		param.put("dbstruc", dbstruc);
		param.put("tcm", tcm);
		tester.startPage(new ConstraintViewPage(param));

	}
	
	@Override
	protected void tearDown(){
		// TODO Auto-generated method stub
		dbstruc.getTables().clear();
	}
	
	/**
	 * パネルの表示内容が描画されているかのテスト
	 */
	public void testPanelRender() {
		String st = "form";
		
		tester.assertRenderedPage(ConstraintViewPage.class);
		tester.assertComponent("feedback", FeedbackPanel.class);
		tester.assertComponent(st+":name", TextField.class);
		tester.assertComponent(st+":type", DropDownChoice.class);
		tester.assertComponent(st+":leng", TextField.class);
		tester.assertComponent(st+":pleng", TextField.class);
//		tester.assertComponent(st+":primary", CheckTextPanel.class);
		tester.assertComponent(st+":primary", CheckBox.class);
		tester.assertComponent(st+":unique", CheckBox.class);
		tester.assertComponent(st+":notnull", CheckBox.class);
		tester.assertComponent(st+":check", CheckTextPanel.class);
		tester.assertComponent(st+":defaults", CheckTextPanel.class);
		tester.assertComponent(st+":index", CheckChoicePanel.class);
		tester.assertComponent(st+":foreign", ForeignPanel.class);	
		tester.assertComponent(st+":submit", AjaxButton.class);
		
		CheckChoicePanel ccp = (CheckChoicePanel) tester.getComponentFromLastRenderedPage(st+":index");
		assertTrue(ccp.isCheck());
		
	}
	
	/**
	 * 初期値設定のテスト。
	 * 変更せずにサブミットするとそのままの値が出る。
	 */
	public void testInitParameter() {
		
		assertEquals(tcm.getName(), "num");
		assertEquals(tcm.getType(), "NUMBER");
		assertEquals(tcm.getLength(), new Integer(10));
		assertEquals(tcm.getPrecisionLength(), new Integer(2));
		assertFalse(tcm.getConstraint().isPrimary());
		assertEquals(tcm.getConstraint().getPrimaryConstName(), "NUM_PK");
		assertEquals(tcm.getConstraint().getForeign().getConstraintName(), "NUM_FK");
		assertEquals(tcm.getConstraint().getIndex(), IndexTypes.NORMAL);
		
		tester.submitForm("form");
		
		assertEquals(tcm.getName(), "num");
		assertEquals(tcm.getType(), "NUMBER");
		assertEquals(tcm.getLength(), new Integer(10));
		assertEquals(tcm.getPrecisionLength(), new Integer(2));
		assertFalse(tcm.getConstraint().isPrimary());
		assertEquals(tcm.getConstraint().getPrimaryConstName(), "NUM_PK");
		assertEquals(tcm.getConstraint().getForeign().getConstraintName(), "NUM_FK");
		assertEquals(tcm.getConstraint().getIndex(), IndexTypes.NORMAL);
		
	}
	
	/**
	 * データ入力テスト。サブミットされるまでは適用されない。
	 * 
	 */
	public void testInputData() {
		
		FormTester ftester = tester.newFormTester("form");
		
		ftester.setValue("name", "names");
		ftester.select("type", 0);
		ftester.setValue("leng", "100");
		ftester.setValue("pleng", "3");
		
		ftester.setValue("primary", true);
		ftester.setValue("unique", true);
//		tester.executeAjaxEvent("form:unique", "onclick");
		ftester.setValue("notnull", true);
//		tester.executeAjaxEvent("form:notnull", "onclick");
		
		assertFalse(tcm.getName().equals("names"));
		assertFalse(tcm.getType().equals("VARCHAR2"));
		assertFalse(tcm.getLength().equals(100));
		assertFalse(tcm.getPrecisionLength().equals(3));
		assertFalse(tcm.getConstraint().isPrimary());
		assertFalse(tcm.getConstraint().isUnique());
		assertFalse(tcm.getConstraint().isNotnull());
		
		ftester.submit();
		
		assertEquals(tcm.getName(), "names");
		assertEquals(tcm.getType(), "VARCHAR2");
		assertEquals(tcm.getLength(), new Integer(100));
		// Number型でないので3が入力されない。
		assertEquals(tcm.getPrecisionLength(), new Integer(2));
		assertTrue(tcm.getConstraint().isPrimary());
		assertTrue(tcm.getConstraint().isUnique());
		assertTrue(tcm.getConstraint().isNotnull());
	}
	
	/**
	 * データ型がNumberの場合以外は精度のテキストフィールドは表示されないことをテスト
	 */
	public void testPrecisionLengthField() {
		FormTester ftester = tester.newFormTester("form");
		
		ftester.select("type", 0);
		
		tester.assertInvisible("form:pleng");
		
		ftester.select("type", 6);
		
		tester.assertVisible("form:pleng");
		
		ftester.select("type", 0);
		
		tester.assertInvisible("form:pleng");
	}
	
	/**
	 * 名前だけを変更したときのテスト
	 * （Typeを変更しなかった場合、値が設定されていた場合でもwicketがエラーをだしてしまっていた）
	 */
	public void testColumnNameChange() {
		FormTester ftester = tester.newFormTester("form");
		
		ftester.setValue("name", "names");
		ftester.submit();
		
		assertEquals(tcm.getName(), "names");
	}
	
	/**
	 * 入力必須のコンポーネントにデータを入力しないときのテスト
	 */
	public void testComponentWithoutData() {
		FormTester ftester = tester.newFormTester("form");
		String[] str = {"'name' 欄 は必須です。","'leng' 欄 は必須です。"};
		
		ftester.setValue("name", "");
		ftester.setValue("leng", "");
		
		ftester.submit();
		
		tester.assertErrorMessages(str);
		
	}
}
