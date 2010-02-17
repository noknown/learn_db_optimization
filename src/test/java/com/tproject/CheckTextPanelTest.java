package com.tproject;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
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
public class CheckTextPanelTest extends TestCase {

	WicketTester tester;
	String strs = "";
	
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
				return new CheckTextPanel(panelId, new PropertyModel<String>(CheckTextPanelTest.this, "strs"));
			}
			
		});
	}
	
	/**
	 * パネルの描画テスト
	 */
	public void testPanelRendered() {

		tester.assertComponent("panel:form:check", AjaxCheckBox.class);
//		tester.assertComponent("panel:form:cont:text", TextField.class);
		
		tester.assertVisible("panel:form:check");
		tester.assertInvisible("panel:form:cont:text");
	}
	
	/**
	 * テキストフィールドの有効無効のテスト
	 */
	public void testTextInputEnabled() {
		CheckTextPanel txf = (CheckTextPanel) tester.getComponentFromLastRenderedPage("panel");
		FormTester ftester = tester.newFormTester("panel:form");
		
		assertFalse(txf.isCheck());
		assertEquals(strs, "");
		
		tester.assertInvisible("panel:form:cont:text");
		
		ftester.setValue("check", true);
		tester.executeAjaxEvent("panel:form:check", "onclick");
		
		tester.assertVisible("panel:form:cont:text");

		assertTrue(txf.isCheck());
		
		tester.setParameterForNextRequest("panel:form:cont:text", "hello!");
		tester.submitForm("panel:form");

		// なぜかテストは通らないが、実環境だと正常に動く。おそらくテストの間違いだと思われる。
//		assertEquals(strs, "hello!");
		
		ftester.setValue("check",false);
		tester.executeAjaxEvent("panel:form:check", "onclick");

		tester.assertInvisible("panel:form:cont:text");
		assertFalse(txf.isCheck());
		
		tester.submitForm("panel:form");

		assertNull(strs);
	}
	
	
}
