package com.tproject;



import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
/**
 * 
 * @author Oh
 * 
 */

public class RegisterPageTest extends TestCase{
	private WicketTester tester;
	private RegisterPage register;
	private Username user;
	/**
	 * 
	 */
	@Override
	@Before
	public void setUp() {
		tester = new WicketTester(new WicketApplication());
		register = new RegisterPage();
				
	}
	/**
	 * 登録用ページの表示
	 */
	@Test
	public void testRenderRegisterPage() {
		tester.startPage(register);
		
		tester.assertRenderedPage(RegisterPage.class);
		
	}
	/**
	 * ゆーざーネームの入力フォーム
	 */
	@Test
	public void testRenderUser_name() {
		tester.startPage(register);
		
		tester.assertComponent("testForm:User_name", TextField.class);
		
		tester.assertVisible("testForm:User_name");
	}
	/**
	 * パスワードの入力フォーム
	 */
	@Test
	public void testRenderPassword() {
		tester.startPage(register);
		
		tester.assertComponent("testForm:Password", PasswordTextField.class);
		
		tester.assertVisible("testForm:Password");
		
	}
	/**
	 * 確認用の入力フォーム
	 */
	@Test
	public void testRenderCompPassword() {
		tester.startPage(register);
		
		tester.assertComponent("testForm:Comp_Password", PasswordTextField.class);
		
		tester.assertVisible("testForm:Comp_Password");
				
	}
	/**
	 * Linkの表示
	 */
	@Test
	public void testIdButton() {
		tester.startPage(register);
		
		tester.assertComponent("testForm:duplication", AjaxLink.class);
		
		tester.assertVisible("testForm:duplication");
				
	}
	/**
	 * Linkの処理
	 */
	@Test
	public void testLinkBehavior(){
		tester.startPage(register);
		
		tester.setParameterForNextRequest("testForm:User_name","hioh");
		tester.clickLink("testForm:duplication");
		tester.assertVisible("modal");
	}
	/**
	 * ユーザーネームのバリデータの確認（31文字入力）
	 */
	@Test
	public void testUser_Name_Length31() {
		
		tester.startPage(register);
				
		// 31文字
		tester.setParameterForNextRequest("testForm:User_name", "abcdefghijklnmopqlstuvwxyzabcde");
		tester.submitForm("testForm");
		
		assertEquals("Input name of user",register.getUsername().getUsername());
				
	}
	/**
	 * ユーザーネームのバリデータの確認（30文字入力）
	 */
//	@Test
//	public void testUser_Name_Length30(){
//		tester.startPage(register);

//		FormTester form = tester.newFormTester("testForm");
//		form.setValue("User_name", "abcdefghijklnmopqlstuvwxyzabc");
//		tester.setParameterForNextRequest("testForm:User_name", "abcdefghijklnmopqlstuvwxyzabc");
//		form.submit();
//		
//		assertEquals(register.getUsername().getUsername(), "abcdefghijklnmopqlstuvwxyzabc");
//
//		
//	}
	/**
	 * パスワードのバリデータの確認（7文字入力）
	 */
	@Test
	public void testPassword_Length7(){
		tester.startPage(register);
		
		// 7文字
		tester.setParameterForNextRequest("testForm:Password", "abcd123");
		tester.submitForm("testForm");
		
		assertNull(register.getPassword());
		
	}
	/**
	 * パスワードのバリデータの確認（8文字入力）
	 */
	@Test
	public void testPasssword_Length8(){
		tester.startPage(register);
		// 8文字
		register.getUsername().setUsercheck(true);
		FormTester form = tester.newFormTester("testForm");
		
		form.setValue("Password", "abcd1234");
		form.setValue("Comp_Password", "abcd1234");
		form.submit();
		
		assertEquals(register.getPassword(), "abcd1234");
	}
	/**
	 * パスワードのバリデータの確認（16字入力）
	 */
	@Test
	public void testPassword16(){
		tester.startPage(register);
		// 16文字
		FormTester form = tester.newFormTester("testForm");
		
		form.setValue("Password", "abcdefgh12345678");
		form.setValue("Comp_Password","abcdefgh12345678");
		form.submit();
			
		assertEquals(register.getPassword(), "abcdefgh12345678");
	}
	/**
	 * パスワードのバリデータの確認（17文字入力）
	 */
	@Test
	public void testPassword17(){
		tester.startPage(register);
		// 17文字
		tester.setParameterForNextRequest("testForm:Password", "abcdefgh123456789");
		tester.submitForm("testForm");
		
		assertNull(register.getPassword());
	}
	/**
	 * 確認用パスワードのバリデータの確認（パスワードと違った場合）
	 */
	@Test
	public void testCompPassword_Notequal(){
		tester.startPage(register);
		
		tester.setParameterForNextRequest("testForm:Password", "abcde12345");
		tester.setParameterForNextRequest("testForm:Comp_Password", "abcde123456");
		tester.submitForm("testForm");
		
		assertNull(register.getComppassword());
	}
	/**
	 * 確認用パスワードのバリデータの確認（パスワードと同じ場合）
	 */
	@Test
	public void testCompPassword_equal(){
		tester.startPage(register);
		
		tester.setParameterForNextRequest("testForm:Password", "abcde12345");
		tester.setParameterForNextRequest("testForm:Comp_Password", "abcde12345");
		tester.submitForm("testForm");
		
		assertEquals(register.getComppassword(), register.getPassword());
	}
	
	/**
	 * 確認画面への遷移
	 */
//	@Test
//	public void testMoveConfirmationpage(){
//		tester.startPage(register);
//		
//		register.getUsername().setUsercheck(true);
//		
//		tester.setParameterForNextRequest("testForm:User_name", "abcdefghigk");
//		tester.setParameterForNextRequest("testForm:Password", "123456789");
//		tester.setParameterForNextRequest("testForm:Comp_Password", "123456789");
//		tester.submitForm("testForm");
//		tester.assertRenderedPage(ConfirmationPage.class);
//	}
	@Test
	public void testIdconfirmPageMove(){
		tester.startPage(register);
		
		tester.clickLink("testForm:duplication");
				
		tester.assertRenderedPage(RegisterPage.class);
	}
}
