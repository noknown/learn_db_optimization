package com.tproject;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.TestPanelSource;
import org.apache.wicket.util.tester.WicketTester;

import junit.framework.TestCase;
/**
 * 
 * @author Oh
 *
 */
public class DatabaseRenamePanelTest extends TestCase{
	WicketTester tester;
	DatabaseName database;
	String str="";
//	String user="";
	UserTestDao user;
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		
		tester = new WicketTester(new WicketApplication(ConnectDao.getInstance()));
		user = new UserTestDao();
		database = user.getDatabaseName();
		tester.startPanel(new TestPanelSource() {
			
			private static final long serialVersionUID = 1L;

			public Panel getTestPanel(String panelId) {
				// TODO Auto-generated method stub
				return new DatabaseRenamePanel(panelId, database,user.getUsername());
			}
			
		});
	}
	@Override
	public void tearDown(){		
		user.deleteUser();	
//		con.disconnect();
	}
	/**
	 * パネルの描画テスト
	 */
	public void testPanelRendered() {

		tester.assertComponent("panel:databaseForm:databasename", RequiredTextField.class);
		tester.assertComponent("panel:databaseForm:renameButton", AjaxButton.class);
		tester.assertComponent("panel:feedback", FeedbackPanel.class);
		
		tester.assertVisible("panel:databaseForm:databasename");
		tester.assertVisible("panel:databaseForm:renameButton");
		tester.assertVisible("panel:feedback");
	}
	/**
	 * パネルの処理動作確認
	 */
	public void testTextinput() {
		IDao con = ConnectDao.getInstance();
		
		FormTester form = tester.newFormTester("panel:databaseForm");
		
		form.setValue("databasename", "test1");
		tester.executeAjaxEvent("panel:databaseForm:renameButton", "onclick");
		
		ResultStruct r = con.read("select database_name from system.databases where database_name='test1'");
		
		assertTrue(!r.isEmpty());
		
		r = con.read("select database_name from system.databases where database_name='"+user.getDatabaseName().getDatabaseName()+"'");
		
		assertTrue(r.isEmpty());
	}
	/**
	 * 既に登録されているデータベースネームを入力したときのエラーメッセージ
	 */
	public void testInputData_Error(){
		FormTester form = tester.newFormTester("panel:databaseForm");
		
		form.setValue("databasename", user.getDatabaseName().getDatabaseName());
		tester.executeAjaxEvent("panel:databaseForm:renameButton", "onclick");
		
		tester.assertErrorMessages(new String[]{"同じデータベース名があります。"});
	}
}
