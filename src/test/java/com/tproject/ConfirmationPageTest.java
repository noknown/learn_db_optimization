package com.tproject;

import java.sql.SQLException;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
/**
 * 
 * @author oh
 *
 */
public class ConfirmationPageTest extends TestCase{
	private WicketTester tester;
	private ConfirmationPage confirmation;
	private ConnectDao con = ConnectDao.getInstance();
	private ResultStruct rs ;

	/**
	 * 初期化
	 */
	@Before
	public void setUp() {
		tester = new WicketTester(new WicketApplication());
		confirmation = new ConfirmationPage("usertest","passtest");
	}
	/**
	 * 登録用のページ表示
	 */
	@Test
	public void testRenderConfirmationPage() {
		tester.startPage(confirmation);
		
		tester.assertRenderedPage(ConfirmationPage.class);
		
	}
	/**
	 * ユーザーネームのラベル表示
	 */
	@Test
	public void testRenderUsernameLabel() {
		tester.startPage(confirmation);
		
		tester.assertComponent("user_name", Label.class);
		
		tester.assertVisible("user_name");
				
	}
	/**
	 * パスワードのラベル表示
	 */
	@Test
	public void testRenderPasswordLabel() {
		tester.startPage(confirmation);
		
		tester.assertComponent("pass_word", Label.class);
		
		tester.assertVisible("pass_word");
				
	}
	/**
	 * ユーザーネームのデータの確認
	 */
	@Test
	public void testRenderUsername() {
		tester.startPage(confirmation);
		
		tester.assertLabel("user_name", "usertest");
	}
	/**
	 * パスワードのデータの確認
	*/
	@Test
	public void testRenderPassword() {
		tester.startPage(confirmation);
		
		tester.assertLabel("pass_word", "passtest");
		
	}

	/**
	 * returnLinkの動作確認
	 */
	@Test
	public void testClickLink(){
		tester.startPage(confirmation);
		
		tester.clickLink("submitForm:returnLink");
		
		tester.assertRenderedPage(RegisterPage.class);
		
	}
	@Test
	/**
	 * ログインページへの画面遷移
	 * データベースサーバにユーザーが登録されているかを確認
	 */
	public void testMoveLoginPage(){
		tester.startPage(confirmation);
		
		tester.submitForm("submitForm");
		tester.assertRenderedPage(LoginPage.class);

		rs = con.read("select user_name from system.users where user_name ='"+confirmation.getUsername()+"'");
		assertEquals(confirmation.getUsername(), rs.getResult().get(0).getDataList().get(0));
		con.insert("delete from system.users where user_name='usertest'");
		con.insert("drop user usertest");
		
	}
	

}
