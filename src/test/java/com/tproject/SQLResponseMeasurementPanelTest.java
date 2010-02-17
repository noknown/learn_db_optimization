package com.tproject;

import junit.framework.TestCase;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.DummyPanelPage;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.TestPanelSource;
import org.apache.wicket.util.tester.WicketTester;

/**
 * @author no_known
 *
 */
public class SQLResponseMeasurementPanelTest extends TestCase{
	
	WicketTester tester;
	SQLResponseMeasurementPanel srmp;
	
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
				return srmp = new SQLResponseMeasurementPanel(panelId);
			}
			
		});
		
	}
	
	/**
	 * ページがきちんと表示されているかのテスト
	 */
	public void testRenderedPanel() {
		
		tester.assertComponent("panel:form", Form.class);
		tester.assertComponent("panel:form:textarea", TextArea.class);
		
	}
	
	/**
	 * テキストエリアの内容が変えられると、サブミットされていなくても
	 * テキストエリア上の文字を保持する。
	 */
	public void testTextAreaOnChange() {
		assertEquals(srmp.getText(), "");
		
		FormTester ftester = tester.newFormTester("panel:form");
		
//		tester.setParameterForNextRequest(DummyPanelPage.TEST_PANEL_ID+":form:textarea", "select * from emp0");
		ftester.setValue("textarea", "select * from emp0");
		
		tester.executeAjaxEvent("panel:form:textarea", "onchange");
		
		assertEquals(srmp.getText(), "select * from emp0");
	}
	
	/**
	 * select文以外の命令や文字列が与えられた場合はその値を取得しない。
	 */
	public void textTextAreaValidate() {
		assertEquals(srmp.getText(), "");
		
		FormTester ftester = tester.newFormTester("panel:form");
		
		ftester.setValue("textarea", "gyaaaaa");
		tester.executeAjaxEvent("panel:form:textarea", "onchange");
		assertEquals(srmp.getText(), "");
		
		ftester.setValue("textarea", "insert into emp0 values(100, 100, 100)");
		tester.executeAjaxEvent("panel:form:textarea", "onchange");
		assertEquals(srmp.getText(), "");
	}

}
