package com.tproject;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import com.tproject.LoginPage;
import junit.framework.TestCase;

/**
 * 
 * @author Oh
 *
 */
public class LoginPageTest extends TestCase {
	
	private WicketTester tester;
	/**
	 * 
	 */
	@Before
	public void setUp() {
		tester = new WicketTester(new WicketApplication());
						
	}
	/**
	 * Login-page作成
	 */
	@Test
	public void testRenderLoginPage() {
		tester.startPage(LoginPage.class);
		
		tester.assertRenderedPage(LoginPage.class);
		
	}
	/**
	 * ユーザーネーム入力フォーム
	 */
	@Test
	public void testRenderUser_name() {
		tester.startPage(LoginPage.class);
		
		tester.assertComponent("loginForm:userName", TextField.class);
		
		tester.assertVisible("loginForm:userName");
	}
	/**
	 * パスワード入力フォーム
	 */
	@Test
	public void testRenderPass_word() {
		tester.startPage(LoginPage.class);
		
		tester.assertComponent("loginForm:password", PasswordTextField.class);
		
		tester.assertVisible("loginForm:password");
		
	}
	/**
	 * 登録していないユーザーネームとパスワードを入力した場合
	 */
	@Test
	public void testNoinput_Username() {
		String[] str={"ユーザーネームかパスワードが間違っています。"};
		tester.startPage(LoginPage.class);
		tester.setParameterForNextRequest("loginForm:userName", "userName");
		tester.setParameterForNextRequest("loginForm:password", "password");
		tester.submitForm("loginForm");
		tester.assertErrorMessages(str);		
	}
	/**
	 * 登録されているユーザーネームとパスワードを入力した場合
	 */
	@Test
	public void testMoveTopPage() {
		tester.startPage(LoginPage.class);
		ConnectDao s = ConnectDao.getInstance();
		tester.setParameterForNextRequest("loginForm:userName", "username");
		tester.setParameterForNextRequest("loginForm:password", "username");
		tester.submitForm("loginForm");
		tester.assertRenderedPage(TopPage.class);
	}
}
