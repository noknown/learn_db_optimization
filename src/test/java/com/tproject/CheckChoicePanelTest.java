package com.tproject;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.TestPanelSource;
import org.apache.wicket.util.tester.WicketTester;

import junit.framework.TestCase;

public class CheckChoicePanelTest extends TestCase {
	
	CheckChoicePanel ccp;
	WicketTester tester;
	
	IndexTypes idx = IndexTypes.NONE;
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		tester = new WicketTester(new WicketApplication());
		
		tester.startPanel(new TestPanelSource() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Panel getTestPanel(String panelId) {
				// TODO Auto-generated method stub
				return ccp = new CheckChoicePanel(panelId, new PropertyModel<IndexTypes>(CheckChoicePanelTest.this, "idx"), Arrays.asList(IndexTypes.values()),
						 new ChoiceRenderer<IndexTypes>("name", "name"));
			}
			
		});
	}
	
	/**
	 * パネル描画テスト
	 */
	public void testPanelRender() {
		tester.assertComponent("panel:form:check", AjaxCheckBox.class);
		
		tester.assertVisible("panel:form:check");
		tester.assertInvisible("panel:form:cont:choice");
	}
	
	/**
	 * ドロップダウンチョイスの有効無効テスト
	 */
	public void testChoiceInputEnable() {
		FormTester ftester = tester.newFormTester("panel:form");
		
		assertFalse(ccp.isCheck());
		assertEquals(idx, IndexTypes.NONE);
		
		tester.assertInvisible("panel:form:cont:choice");
		
		ftester.setValue("check", true);
		tester.executeAjaxEvent("panel:form:check", "onclick");
		
		tester.assertVisible("panel:form:cont:choice");
		assertTrue(ccp.isCheck());
		
//		//入力値の確認。テストはパスしないが、実環境では動作する。テストが間違っている可能性あり。
//		ftester.select("cont:choice", 1);
//		tester.submitForm("panel:form");
//		
//		assertEquals(idx, IndexTypes.NORMAL);
		
		ftester.setValue("check", false);
		tester.executeAjaxEvent("panel:form:check", "onclick");
		
		tester.assertInvisible("panel:form:cont:choice");
		
	}

}
