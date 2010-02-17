package com.tproject;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
/**
 * 
 * @author Oh
 *
 */
public class DataAutoCreateTest extends TestCase{
	private WicketTester tester;
	private DataAutoCreatePage dataauto;
	private UserTestDao user;
	/**
	 * 
	 */
	@Before
	public void setUp() {
		user = new UserTestDao();
		user.createData();
		tester = new WicketTester(new WicketApplication(UserDao.newInstance(user.getUsername(), user.getPassword())));

		PageParameters database = new PageParameters();
		database.put("database", user.getDatabaseName());
		database.put("user", user.getUsername());
		dataauto = new DataAutoCreatePage(database);
	}
	/**
	 * データ自動作成ページの表示
	 */
	@Test
	public void testRenderDataAutoCreatePage() {
		tester.startPage(dataauto);
		
		tester.assertRenderedPage(DataAutoCreatePage.class);
		
		tester.assertComponent("tablechoiceForm", Form.class);
		tester.assertVisible("tablechoiceForm");
		//テーブルネームのドロップダウン
//		tester.assertComponent("tablechoiceForm:tablenamechoice", DropDownChoice.class);
//		tester.assertComponent("tablechoiceForm:selectedName", Label.class);
//		tester.assertComponent("tablechoiceForm:tableNButton", AjaxButton.class);
		
//		tester.assertVisible("tablechoiceForm:tablenamechoice");
//		tester.assertVisible("tablechoiceForm:selectedName");
//		tester.assertVisible("tablechoiceForm:tableNButton");
		//カラムねーむのドロップダウン
//		tester.assertComponent("tablechoiceForm:tablecolumnchoice", DropDownChoice.class);
//		tester.assertComponent("tablechoiceForm:selectedColumn", Label.class);
//		tester.assertComponent("tablechoiceForm:tableCButton", AjaxButton.class);
		
//		tester.assertVisible("tablechoiceForm:tablecolumnchoice");
//		tester.assertVisible("tablechoiceForm:selectedColumn");
//		tester.assertVisible("tablechoiceForm:tableCButton");
		
//		con.disconnect();
		user.deleteUser();				
	}
	/**
	 * テーブルネームButtonの動作確認
	 */
	@Test
	public void testTableNameAjaxButton(){
		
	}
	/**
	 * カラムネームButtonの動作確認
	 */
	@Test
	public void testColumnAjaxButton(){
		
	}
}
