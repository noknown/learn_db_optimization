package com.tproject;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;


import junit.framework.TestCase;
/**
 * 
 * @author no_known
 *
 */
public class ResponseMeasurementPageTest extends TestCase {
	
	WicketTester tester;
	PageParameters pp;
	ResponseMeasurementPage rmp;
	IDao dao;
	String tid = "form:cont:";
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		tester = new WicketTester(new MockWicketApplication(ConnectDao.getInstance()));
		
		dao = ConnectDao.getInstance();
		
		dao.insert("insert into system.users values ('scott', 'tigerrrr')");
		dao.insert("insert into system.databases values ('00001', 'database1', 'scott', '09-01-01', '09-01-01')");
		dao.insert("insert into system.databases values ('00002', 'database2', 'scott', '09-01-02', '09-01-02')");
		dao.insert("insert into system.tables values ('0000001', 'emp0', '00001')");
		dao.insert("insert into system.tables values ('0000002', 'dept0', '00001')");
		dao.insert("insert into system.tables values ('0000003', 'emp', '00002')");
		dao.insert("insert into system.tables values ('0000004', 'dept', '00002')");
		
		dao.insert("commit");
		
		pp = new PageParameters();
		String[] strs = new String[]{"database1", "database2"};
		pp.put("databases", strs);
		pp.put("user", "ikemotot");

		
		tester.startPage(rmp = new ResponseMeasurementPage(pp, new MockCommand()));
	}
	
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub

		dao.insert("delete from system.tables where table_id='0000001' and table_name='emp0'");
		dao.insert("delete from system.tables where table_id='0000002' and table_name='dept0'");
		dao.insert("delete from system.tables where table_id='0000003' and table_name='emp'");
		dao.insert("delete from system.tables where table_id='0000004' and table_name='dept'");
		dao.insert("delete from system.databases where database_id='00001' and database_name='database1'");
		dao.insert("delete from system.databases where database_id='00002' and database_name='database2'");
		dao.insert("delete from system.users where user_name='scott'");

		dao.insert("commit");
//		dao.disconnect();
	}
	
	/**
	 * ページのレンダリングテスト
	 */
	public void testRenderd() {
		tester.assertRenderedPage(ResponseMeasurementPage.class);
		
		tester.assertComponent("form", Form.class);
		tester.assertComponent(tid+"tabs", ListView.class);
		tester.assertComponent(tid+"text", ListView.class);
		tester.assertComponent("response", AnalysisPanel.class);
		
		tester.assertVisible(tid+"tabs");
		tester.assertVisible(tid+"text");
		tester.assertVisible(tid+"text:0:sqlinput");
		tester.assertInvisible(tid+"text:1:sqlinput");

		tester.assertLabel(tid+"tabs:0:link:label", "database1");
		tester.assertLabel(tid+"tabs:1:link:label", "database2");
		
	}
	
	/**
	 * リンクをクリックすることで表示されるパネルが変わることをテスト
	 */
	public void testChoiceCheck() {
		
		tester.assertRenderedPage(ResponseMeasurementPage.class);
		
		tester.assertVisible(tid+"text:0:sqlinput");
		tester.assertInvisible(tid+"text:1:sqlinput");

		tester.clickLink(tid+"tabs:0:link");
		tester.assertVisible(tid+"text:0:sqlinput");
		tester.assertInvisible(tid+"text:1:sqlinput");
		
		tester.clickLink(tid+"tabs:1:link");
		tester.assertInvisible(tid+"text:0:sqlinput");
		tester.assertVisible(tid+"text:1:sqlinput");
		
	}
	
	/**
	 * サブミットしたときにすべてのSQLinputPanelに入力した
	 * SQL文が得られるかどうかのテスト
	 */
	public void testSubmitted() {
		tester.assertRenderedPage(ResponseMeasurementPage.class);
		
		tester.clickLink(tid+"tabs:0:link");
		FormTester ftester = tester.newFormTester("form");
		ftester.setValue("cont:text:0:sqlinput:form:textarea", "select * from emp0");
		tester.executeAjaxEvent(tid+"text:0:sqlinput:form:textarea", "onchange");
		
		tester.clickLink(tid+"tabs:1:link");
		ftester.setValue("cont:text:1:sqlinput:form:textarea", "select * from dept0");
		tester.executeAjaxEvent(tid+"text:1:sqlinput:form:textarea", "onchange");
		
		tester.executeAjaxEvent("form:button", "onclick");
		
		List<String> lstring = rmp.getInputList();
		assertFalse(lstring.get(0).equals(""));
		assertFalse(lstring.get(1).equals(""));
		
		tester.assertVisible(tid+"text:0:sqlinput");
		tester.assertInvisible(tid+"text:1:sqlinput");
		
	}
	
	/**
	 * 入力項目にデータを入力し、ボタンをクリックするとAnalysisパネルが表示されることをテスト
	 */
	public void testAnalysisPanel() {
		tester.assertRenderedPage(ResponseMeasurementPage.class);
		
		tester.clickLink(tid+"tabs:0:link");
		FormTester ftester = tester.newFormTester("form");
		ftester.setValue("cont:text:0:sqlinput:form:textarea", "select * from emp0");
		tester.executeAjaxEvent(tid+"text:0:sqlinput:form:textarea", "onchange");
		
		tester.clickLink(tid+"tabs:1:link");
		ftester.setValue("cont:text:1:sqlinput:form:textarea", "select * from dept0");
		tester.executeAjaxEvent(tid+"text:1:sqlinput:form:textarea", "onchange");
		
		tester.executeAjaxEvent("form:button", "onclick");
		
		tester.assertComponent("response", AnalysisPanel.class);
		tester.assertVisible("response");
		
		tester.assertComponent("response:plist", ListView.class);
		tester.assertComponent("response:plist:0:responsetime", Label.class);
		
		tester.assertContains("レスポンスタイム");
		tester.assertLabel("response:plist:0:responsetime", "00:00:00.01");
		
		tester.assertContains("recursive calls");
		tester.assertLabel("response:plist:0:recursive", "1900");
		
	}
	
	public void testToTopPage() {
		tester.clickLink("toppage");
		
		tester.assertRenderedPage(TopPage.class);
	}
	
	public void testToBehavior() {
		tester.clickLink("behavior");
		
		tester.assertRenderedPage(SQLBehaviorConfirmPage.class);
	}
	
	private class MockWicketApplication extends WicketApplication {
		@Override
		public Session newSession(Request request, Response response) {
			// TODO Auto-generated method stub
			LoginSession session = new LoginSession(request);
			session.authenticate("scott", "tiger");
			
			return session;
		}
		
		public MockWicketApplication(IDao dao) {
			// TODO Auto-generated constructor stub
			super(dao);
		}
	}
	
	private class MockCommand implements Command , Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public AnalyticsInformation execCommand(String user, String pass,
				String sql) {
			// TODO Auto-generated method stub
			AnalyticsInformation ai = new AnalyticsInformation();
			ai.setResponseTime("00:00:00.01");
			Statistics s = new Statistics();
			s.setRecursiveCalls(1900);
			ai.setStatistics(s);
			
			return ai;
		}
		
	}

}
