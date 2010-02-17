package com.tproject;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.util.tester.WicketTester;

import junit.framework.TestCase;
/**
 * 
 * @author no_known
 *
 */
public class SQLBehaviorConfirmPageTest extends TestCase {
	WicketTester tester;
	
	@Override
	public void setUp() throws Exception {
		// TODO Auto-generated method stub
		tester = new WicketTester(new WicketApplication(new MockDao()));
		tester.startPage(new SQLBehaviorConfirmPage());
	}
	
	/**
	 * ページが描画され見えているかどうかのテスト
	 */
	public void testRenderPage() {
	
		tester.assertRenderedPage(SQLBehaviorConfirmPage.class);
		
		tester.assertComponent("form", Form.class);
		tester.assertComponent("form:sqlpanel", SQLInputPanel.class);
		tester.assertVisible("form:sqlpanel");
		
		tester.assertComponent("head", ListView.class);
		tester.assertComponent("head:0:header", Label.class);
		
		tester.assertComponent("row", PageableListView.class);
		tester.assertComponent("row:0:col", ListView.class);
		tester.assertComponent("row:0:col:0:values", Label.class);
		tester.assertComponent("navigator", PagingNavigator.class);
	}
	
	/**
	 * パネルにSQLを入力しサブミットした後、リストにデータが表示されるかのテスト
	 */
	public void testExecuteSql() {
		
		tester.setParameterForNextRequest("form:sqlpanel", "SELECT empno FROM emp0");
		tester.submitForm("form");
		
		tester.assertContains("EMPNO");
		
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
				"7844"
		};
		for(int i = 0; i < strs.length; i++) {
			tester.assertContains(strs[i]);
		}
		
		// ２ページ目へ移動
		String[] str2 = {
				"7876",
				"7900",
				"7902",
				"7934"
		};
		tester.clickLink("navigator:next");
		for(int i = 0; i < str2.length; i++) {
			tester.assertContains(str2[i]);
		}
		
		// １ページ目へ移動
		tester.clickLink("navigator:prev");
		for(int i = 0; i < strs.length; i++) {
			tester.assertContains(strs[i]);
		}
	}
	
//	public void testRenderSqlResubmit() {
//		SQLBehaviorConfirmPage sqlb = new SQLBehaviorConfirmPage(new MockDao());
//		tester.startPage(sqlb);
//		
//		
//		tester.setParameterForNextRequest("form:sqlpanel", "SELECT empno FROM emp0");
//		tester.submitForm("form");
//		
//		tester.clickLink("navigator:next");
//
//		tester.setParameterForNextRequest("form:sqlpanel", "SELECT empno FROM emp0");
//		tester.submitForm("form");
//
//		String[] strs = {
//				"7369",
//				"7499",
//				"7521",
//				"7566",
//				"7654",
//				"7698",
//				"7782",
//				"7788",
//				"7839",
//				"7844"
//		};
//		for(int i = 0; i < strs.length; i++) {
//			tester.assertContains(strs[i]);
//		}
//		
//		// ２ページ目へ移動
//		String[] str2 = {
//				"7876",
//				"7900",
//				"7902",
//				"7934"
//		};
//		tester.clickLink("navigator:next");
//		for(int i = 0; i < str2.length; i++) {
//			tester.assertContains(str2[i]);
//		}
//		
//	}

}
