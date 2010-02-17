package com.tproject;

import java.sql.SQLException;
import java.util.List;

import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.DummyPanelPage;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.TestPanelSource;
import org.apache.wicket.util.tester.WicketTester;

import junit.framework.TestCase;
/**
 * 
 * @author no_known
 *
 */
public class SQLInputPanelTest extends TestCase {
	private WicketTester tester;
	
	@Override
	public void setUp() throws Exception {
		// TODO Auto-generated method stub
		tester = new WicketTester(new WicketApplication(ConnectDao.getInstance()));
		
		tester.startPanel(new TestPanelSource() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Panel getTestPanel(String panelId) {
				// TODO Auto-generated method stub
				return new SQLInputPanel(panelId);
			}
		});
	}
	
	/**
	 * Form,TextAreaコンポーネントが配置され、表示されているかのテスト
	 */
	public void testTextAreaComponent() {

		
		tester.assertComponent(DummyPanelPage.TEST_PANEL_ID+":form", Form.class);
		tester.assertComponent(DummyPanelPage.TEST_PANEL_ID+":form:textarea", TextArea.class);
		tester.assertVisible(DummyPanelPage.TEST_PANEL_ID+":form:textarea");
		
//		assertTrue(tester.getComponentFromLastRenderedPage(DummyPanelPage.TEST_PANEL_ID) instanceof SQLInputPanel);
	}
	
	/**
	 * サブミットし、textarea内のSQL実行結果の受け取りテスト<br/>
	 * getResultの戻り値はSQLを実行した結果をResultDataのリストにしたものである。
	 * getResultはサブミットされる以前に呼び出されると空のリストを返す
	 * @throws SQLException
	 */
	public void testGetResultCheckResult() throws SQLException {



		SQLInputPanel sqlinput;
		sqlinput = (SQLInputPanel) tester.getComponentFromLastRenderedPage(DummyPanelPage.TEST_PANEL_ID);
		
		List<ResultData> rdata = sqlinput.getResult();
		assertEquals(rdata.get(0).getDataList().get(0), "");

		tester.setParameterForNextRequest(DummyPanelPage.TEST_PANEL_ID+":form:textarea", "select empno from emp0");
		tester.submitForm(DummyPanelPage.TEST_PANEL_ID+":form");
				
		assertNotNull(sqlinput);
		
//		ResultSet rs = sqlinput.getResult();
		rdata = sqlinput.getResult();
		assertNotNull(rdata);
		
		String[] strs = {
				"7369",
				"7499",
				"7521",
				"7566",
				"7654",
				"7698",
				"7782",
				"7788",
				"7839",
				"7844",
				"7876",
				"7900",
				"7902",
				"7934"
		};
		
		for(int i = 0; i < rdata.size(); i++) {
			List<String> strr = rdata.get(i).getDataList();
			for(int j = 0; j < 1; j++) {
				assertEquals(strr.get(j), strs[i]);
			}
		}
		
//		int i = 0;
//		while(rs.next())
//			assertEquals(rs.getString(1), strs[i++]);
		
	}
	
	/**
	 * サブミットし、textarea内のSQL実行結果の要素名受け取りテスト<br/>
	 * getSchemaの戻り値は結果の要素名が入ったResultDataオブジェクトである。
	 * サブミットされる以前はResultDataの中は空のStringリスとである。
	 */
	public void testGetSchemaCheckResult() {


		SQLInputPanel sqlinput;
		sqlinput = (SQLInputPanel) tester.getComponentFromLastRenderedPage(DummyPanelPage.TEST_PANEL_ID);

		ResultData rd = sqlinput.getSchema();
		assertEquals(rd.getDataList().get(0), "");
		
		tester.setParameterForNextRequest(DummyPanelPage.TEST_PANEL_ID+":form:textarea", "select * from emp0");
		tester.submitForm(DummyPanelPage.TEST_PANEL_ID+":form");
		
		assertNotNull(sqlinput);
		
		rd = sqlinput.getSchema();
		assertNotNull(rd);
		
		assertEquals(rd.getDataList().get(0), "EMPNO");

	}
	
	/**
	 * select文以外が入力された場合はResultDataオブジェクトには何も入らないことをテスト
	 */
	public void testInputNotSelectStatement() {
		SQLInputPanel sqlinput;
		sqlinput = (SQLInputPanel) tester.getComponentFromLastRenderedPage(DummyPanelPage.TEST_PANEL_ID);
		
		ResultData rd = sqlinput.getSchema();
		assertEquals(rd.getDataList().get(0), "");
		
		tester.setParameterForNextRequest(DummyPanelPage.TEST_PANEL_ID+":form:textarea", "insert into dept0 values(90, 'FFFF')");
		tester.submitForm(DummyPanelPage.TEST_PANEL_ID+":form");
		
		assertNotNull(sqlinput);
		
		rd = sqlinput.getSchema();
		assertEquals(rd.getDataList().get(0), "");
		
	}
	
	/**
	 * サブミットした際にテキストエリア上の文字列が取得できることをテスト
	 */
	public void testSubmitString() {


		SQLInputPanel sqlinput;
		sqlinput = (SQLInputPanel) tester.getComponentFromLastRenderedPage(DummyPanelPage.TEST_PANEL_ID);

		assertEquals(sqlinput.getInputText(), "");
		
		tester.setParameterForNextRequest(DummyPanelPage.TEST_PANEL_ID+":form:textarea", "select * from emp0");
		tester.submitForm(DummyPanelPage.TEST_PANEL_ID+":form");
		
		assertNotNull(sqlinput);
				
		assertEquals(sqlinput.getInputText(), "select * from emp0");
	}
	
	/**
	 * テキストエリアに文字を入れずにサブミットしたときのテスト
	 */
	public void testNoInputSubmit() {
		SQLInputPanel sqlinput;
		sqlinput = (SQLInputPanel) tester.getComponentFromLastRenderedPage(DummyPanelPage.TEST_PANEL_ID);
		
		FormTester ftester = tester.newFormTester(DummyPanelPage.TEST_PANEL_ID+":form");
		
		ftester.setValue("textarea", "");
		ftester.submit();
		
		assertTrue(sqlinput.getSchema().getDataList().get(0).equals(""));
		
	}
	
	/**
	 * select文のときだけモデルにいれることをテスト
	 */
	public void testInputSelectOnly() {
		SQLInputPanel sqlinput;
		sqlinput = (SQLInputPanel) tester.getComponentFromLastRenderedPage("panel");
		
		FormTester ftester = tester.newFormTester("panel:form");
		
		ftester.setValue("textarea", "select * from emp0");
		ftester.submit();
		
		assertEquals(sqlinput.getInputText(), "select * from emp0");
	}
	
	/**
	 * 改行を含むSQL分でも実行することをテスト
	 */
	public void testInputSelectNextLine() {
		SQLInputPanel sqlinput;
		sqlinput = (SQLInputPanel) tester.getComponentFromLastRenderedPage("panel");
		
		FormTester ftester = tester.newFormTester("panel:form");
		
		ftester.setValue("textarea", "select * from \n emp0");
		ftester.submit();
		
		assertEquals(sqlinput.getInputText(), "select * from \n emp0");
	}
	
	/**
	 * select文以外の入力の場合は、モデルを更新せず、SQL文実行もおこなわない。
	 */
	public void testInputSelectOnlyNoExecute() {
		SQLInputPanel sqlinput;
		sqlinput = (SQLInputPanel) tester.getComponentFromLastRenderedPage("panel");
		
		FormTester ftester = tester.newFormTester("panel:form");
		
		ftester.setValue("textarea", "insert into emp0");
		ftester.submit();
		
		assertEquals(sqlinput.getInputText(), "");
		assertEquals(sqlinput.getResult().get(0).getDataList().get(0), "");
	}
	
	/**
	 * まだWicketApplicationにDaoが登録されていない状態でもデフォルトユーザとして
	 * アクセスできることを確認する
	 * @throws SQLException 
	 */
	public void testNonDao() throws SQLException {
		WicketTester wtester = new WicketTester(new WicketApplication());
		
		wtester.startPanel(new TestPanelSource() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Panel getTestPanel(String panelId) {
				// TODO Auto-generated method stub
				return new SQLInputPanel(panelId);
			}
			
		});
		
		SQLInputPanel sqlinput;
		sqlinput = (SQLInputPanel) wtester.getComponentFromLastRenderedPage(DummyPanelPage.TEST_PANEL_ID);
		
		List<ResultData> rdata = sqlinput.getResult();
		assertEquals(rdata.get(0).getDataList().get(0), "");

		wtester.setParameterForNextRequest(DummyPanelPage.TEST_PANEL_ID+":form:textarea", "select empno from emp0");
		wtester.submitForm(DummyPanelPage.TEST_PANEL_ID+":form");
				
		assertNotNull(sqlinput);
		
//		ResultSet rs = sqlinput.getResult();
		rdata = sqlinput.getResult();
		assertNotNull(rdata);
		
		String[] strs = {
				"7369",
				"7499",
				"7521",
				"7566",
				"7654",
				"7698",
				"7782",
				"7788",
				"7839",
				"7844",
				"7876",
				"7900",
				"7902",
				"7934"
		};
		
		for(int i = 0; i < rdata.size(); i++) {
			List<String> strr = rdata.get(i).getDataList();
			for(int j = 0; j < 1; j++) {
				assertEquals(strr.get(j), strs[i]);
			}
		}
		
	}
	
}
