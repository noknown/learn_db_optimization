package com.tproject;

import java.util.List;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import junit.framework.TestCase;
/**
 * 
 * @author Oh
 *
 */

public class TopPageTest extends TestCase{
	private WicketTester tester;
	private TopPage top;
	private UserTestDao usertest;
	private PageParameters param;

	/**
	 * 初期化
	 */
	
	@Override
	public void setUp() {
		tester = new WicketTester(new WicketApplication(ConnectDao.getInstance()));
		usertest = new UserTestDao();
		param = new PageParameters();
		param.put("user", usertest.getUsername());
		param.put("pass", usertest.getPassword());

		top = new TopPage(param);
		
	}
	@Override
	public void tearDown(){		
		usertest.deleteUser();	
//		con.disconnect();
	}
		
	/**
	 * Topのページ表示
	 */
	@Test
	public void testRenderTopPage() {
		tester.startPage(top);
		
		tester.assertRenderedPage(TopPage.class);
		
	}
	/**
	 * ログインユーザーネームを表示
	 */
	@Test
	public void testAdminLabel(){
		tester.startPage(top);
		
		tester.assertComponent("admin", Label.class);
			
		tester.assertVisible("admin");
		tester.assertLabel("admin", "user_test"+" さん＾＾");
				
	}
	/**
	 * Topページのコンポーネントの表示
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testCompenent(){
		
		tester.startPage(top);
		
		tester.assertComponent("logout", Link.class);
		tester.assertComponent("myForm:viewContainer", WebMarkupContainer.class);
		tester.assertComponent("myForm:viewContainer:datacheck", CheckGroup.class);
		tester.assertComponent("myForm:viewContainer:datacheck:list", PageableListView.class);
		tester.assertComponent("myForm:viewContainer:datacheck:list:0:index", Label.class );
		tester.assertComponent("myForm:viewContainer:datacheck:list:0:databaseName", Label.class );
		tester.assertComponent("myForm:viewContainer:datacheck:list:0:createDate", Label.class );
		tester.assertComponent("myForm:viewContainer:datacheck:list:0:updateDate", Label.class );
		tester.assertComponent("myForm:viewContainer:datacheck:list:0:check", Check.class );
		tester.assertComponent("myForm:viewContainer:datacheck:list:0:rename", AjaxLink.class);
		tester.assertComponent("myForm:viewContainer:datacheck:list:0:delete", AjaxLink.class);
		tester.assertComponent("myForm:viewContainer:datacheck:list:0:copy", AjaxLink.class);
		tester.assertComponent("myForm:viewContainer:datacheck:list:0:edit", Link.class);
		tester.assertComponent("myForm:viewContainer:navi", AjaxPagingNavigator.class);
		tester.assertComponent("myForm:databaseName", TextField.class);
		tester.assertComponent("myForm:addLocale", AjaxSubmitButton.class);
		tester.assertComponent("myForm:response", AjaxSubmitButton.class);
		
		tester.assertVisible("logout");
		tester.assertVisible("myForm:viewContainer");
		tester.assertVisible("myForm:viewContainer:datacheck");
		tester.assertVisible("myForm:viewContainer:datacheck:list");
		tester.assertVisible("myForm:viewContainer:datacheck:list:0:index");
		tester.assertVisible("myForm:viewContainer:datacheck:list:0:databaseName");
		tester.assertVisible("myForm:viewContainer:datacheck:list:0:createDate");
		tester.assertVisible("myForm:viewContainer:datacheck:list:0:updateDate");	
		tester.assertVisible("myForm:viewContainer:datacheck:list:0:check");	
		tester.assertVisible("myForm:viewContainer:datacheck:list:0:rename");
		tester.assertVisible("myForm:viewContainer:datacheck:list:0:delete");
		tester.assertVisible("myForm:viewContainer:datacheck:list:0:copy");
		tester.assertVisible("myForm:viewContainer:datacheck:list:0:edit");
		tester.assertVisible("myForm:viewContainer:navi");
		tester.assertVisible("myForm:databaseName");
		tester.assertVisible("myForm:addLocale");
		tester.assertVisible("myForm:response");
						     
	}
	/**
	 * データベースネームの削除
	 */
	@Test
	public void testDeleteButton() {
		tester.startPage(top);
		List<DatabaseName> datas = top.getDatas();

		tester.assertListView("myForm:viewContainer:datacheck:list", datas);
		tester.executeAjaxEvent("myForm:viewContainer:datacheck:list:1:delete","onclick");
		try{
			assertNull(datas.get(1));
			fail();
		}catch(IndexOutOfBoundsException ev){
			
		}
		
		
	}
	/**
	 * データベースネームのコピー
	 */
	@Test
	public void testCopyButton() {
		tester.startPage(top);
		
		List<DatabaseName> datas = top.getDatas();
		
		tester.assertListView("myForm:viewContainer:datacheck:list", datas);
		tester.executeAjaxEvent("myForm:viewContainer:datacheck:list:1:copy","onclick");

		assertNotNull(datas.get(2));
		assertEquals(top.getDatas().get(2).getDatabaseName(), "database_test1_1");
		
	}
	/**
	 * データベースの名前の変更
	 */
	@Test
	public void testRenameButton() {
		tester.startPage(top);

		List<DatabaseName> datas = top.getDatas();
		
		tester.assertListView("myForm:viewContainer:datacheck:list", datas);
		
		tester.executeAjaxEvent("myForm:viewContainer:datacheck:list:0:rename","onclick");
		tester.assertComponent("myForm:modal", ModalWindow.class);
		tester.assertVisible("myForm:modal");
		
	}
	/**
	 * editボタンを押したときにページ遷移
	 */
	@Test
	public void testEditButton(){
		tester.startPage(top);
		
		tester.clickLink("myForm:viewContainer:datacheck:list:0:edit");
		tester.assertRenderedPage(TableDesignerPage.class);
	}
	/**
	 * logoutリンクを押したときのLogoutPageへの遷移
	 */
	@Test
	public void testLogoutLink(){
		tester.startPage(top);
		
		tester.clickLink("logout");
		tester.assertRenderedPage(LogoutPage.class);
	}
	/**
	 * データベースの新規作成-入力フォームに空の場合のエラーメッセージ
	 */
	@Test
	public void testInputNewdata_ErrorMessage1(){
		
		tester.startPage(top);
		//入力フォームがNULLのときエラーメッセージ		
		tester.executeAjaxEvent("myForm:addLocale", "onclick");
		tester.assertErrorMessages(new String[]{"データベースネームを入力してください。"});		

	}
	/**
	 * 入力したデータベースネームが既に存在する場合のエラーメッセージ
	 */
	@Test
	public void testInputNewdata_ErrorMessage2(){
		
		tester.startPage(top);
		tester.assertRenderedPage(TopPage.class);
		FormTester form = tester.newFormTester("myForm");
		assertNotNull(form);
		form.setValue("databaseName", "database_test1");
		tester.executeAjaxEvent("myForm:addLocale", "onclick");
		tester.assertErrorMessages(new String[]{"すでに登録されているデータベースネームです。"});
	}
	/**
	 * データベースを新しく作成できているのかどうか
	 */
	@Test
	public void testInputNewdata(){
		tester.startPage(top);
		tester.assertRenderedPage(TopPage.class);
		
		tester.assertListView("myForm:viewContainer:datacheck:list", top.getDatas());
		FormTester form = tester.newFormTester("myForm");
		assertNotNull(form);
		form.setValue("databaseName", "database_test3");
		tester.executeAjaxEvent("myForm:addLocale", "onclick");
		assertEquals("database_test3", top.getDatas().get(2).getDatabaseName());
		
	}
	
	
}
