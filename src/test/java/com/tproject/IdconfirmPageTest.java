package com.tproject;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
/**
 * 
 * @author Oh
 *
 */
public class IdconfirmPageTest extends TestCase{
	private WicketTester tester;
	private IdconfirmPage idconfirm;
	private ConnectDao con;
	private Username user;
	private String f = "idForm";
	private PageParameters param;
	//初期設定
	@Override
	public void setUp() {
		param = new PageParameters();
		user = new Username();
		
		tester = new WicketTester(new WicketApplication());
		con = ConnectDao.getInstance();
		con.insert("insert into system.users values('hioh','hiohhioh')");
		con.insert("commit");
	}	
	//テスト終了後の処理設定
	@Override
	public void tearDown(){
		con.insert("delete from system.users where user_name='hioh'");
		con.insert("commit");
//		con.disconnect();
	}
	/**
	 * ID重複確認ページの表示
	 */
	@Test
	public void testIdconfirmPage() {
		param.put("username", user);
		tester.startPage(new IdconfirmPage(param));		
		
		tester.assertRenderedPage(IdconfirmPage.class);		
	}
	/**
	 * ゆーざーネームの入力フォーム
	 */
	@Test
	public void testRenderUser_name() {
		param.put("username", user);
		tester.startPage(new IdconfirmPage(param));		
		
		tester.assertComponent(f+":username", RequiredTextField.class);
		tester.assertVisible(f+":username");
		
		tester.assertComponent(f+":idButton", AjaxButton.class);
		tester.assertVisible(f+":idButton");
		
		tester.assertInvisible(f+":okButton");
		tester.assertInvisible(f+":infoMessage");

	}
	/**
	 * 使用可能IDの場合のLabelの表示	
	 */
	@Test
	public void testInfoMessageLabel() {		
		user.setUsername("user");
		param.put("username", user);
		tester.startPage(new IdconfirmPage(param));		

		tester.executeAjaxEvent(f+":idButton","onclick");		
		tester.assertComponent("idForm:infoMessage", Label.class);
		tester.assertVisible(f+":infoMessage");
		tester.assertComponent(f+":okButton", AjaxButton.class);
		tester.assertVisible(f+":okButton");
		
		tester.assertLabel("idForm:infoMessage","このIDは使用可能です");
				
	}
	/**
	 * データベースにIDがある場合
	 */
	@Test
	public void testInfoMessage_No() {
		
		user.setUsername("hioh");
		param.put("username", user);					
		tester.startPage(new IdconfirmPage(param));		

		String[] str = {"このIDは使用できません。"};
		tester.executeAjaxEvent(f+":idButton", "onclick");
		tester.assertInfoMessages(str);
		tester.assertInvisible(f+":okButton");
		tester.assertInvisible(f+":infoMessage");
						
	}
	/**
	 * データベースにIDがない場合
	 */
	@Test
	public void testInfoMessage_Yes() {
		user.setUsername("oh");
		param.put("username", user);					
		tester.startPage(new IdconfirmPage(param));		
		con = ConnectDao.getInstance();
		con.insert("delete from system.users where user_name='oh'");
		
		tester.setParameterForNextRequest("idForm:username","oh");
		tester.executeAjaxEvent(f+":idButton", "onclick");		
		tester.assertVisible(f+":infoMessage");
		tester.assertVisible(f+":okButton");				
	}
}
