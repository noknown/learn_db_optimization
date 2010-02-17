package com.tproject;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
/**
 * 
 * @author Oh
 *
 */
public class LogoutPageTest extends TestCase{
	private WicketTester tester;
	private LogoutPage logout;
	/**
	 * 初期化
	 */
	@Before
	public void setUp() {
		tester = new WicketTester(new WicketApplication());
		logout = new LogoutPage();
	}
	/**
	 * ログアウトページ表示
	 */
	@Test
	public void testRenderLogoutPage() {
		tester.startPage(logout);
		
		tester.assertRenderedPage(LogoutPage.class);
		
	}
	

}
