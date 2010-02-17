package com.tproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.util.tester.TagTester;
import org.apache.wicket.util.tester.WicketTester;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;

import junit.framework.TestCase;

/**
 * 
 * @author no_known
 *
 */
public class TableDesignerPageTest extends TestCase {

	WicketTester tester;
	PageParameters param;
	TableDesignerPage tdp;

	@Override
	public void setUp(){
		// TODO Auto-generated method stub
		ConnectDao dao = ConnectDao.getInstance();
		param = new PageParameters();
		
		param.put("database", "database");
		param.put("user", "ikemotot");
		
		dao.insert("insert into system.users values ('ikemotot', 'password')");
		dao.insert("insert into system.databases values ('00001', 'database', 'ikemotot', '09-01-01', '09-01-01')");
		dao.insert("alter table emp0 add constraint fk_emp0 foreign key (deptno) references dept0(deptno)");
		dao.insert("insert into system.tables values ('0000001', 'emp0', '00001')");
		dao.insert("insert into system.tables values ('0000002', 'dept0', '00001')");
		
		dao.insert("commit");

		tester = new WicketTester(new MockWicketApplication(ConnectDao.getInstance()));

		tdp = new TableDesignerPage(param);
	}
	
	@Override
	protected void tearDown() {
		// TODO Auto-generated method stub
		ConnectDao dao = ConnectDao.getInstance();
		
		dao.insert("delete from system.tables where table_id='0000001' and table_name='emp0'");
		dao.insert("delete from system.tables where table_id='0000002' and table_name='dept0'");
		dao.insert("alter table emp0 drop constraint fk_emp0");
		dao.insert("delete from system.databases where database_id='00001' and database_name='database'");
		dao.insert("delete from system.users where user_name='ikemotot'");

		dao.insert("commit");
//		dao.disconnect();
	}
	
	/**
	 * テーブル設計用インターフェースページが表示されているかどうかの
	 * テスト。
	 */
	public void testRenderPage() {
		
		tester.startPage(tdp);
		
		tester.assertRenderedPage(TableDesignerPage.class);
		tester.assertComponent("form", Form.class);
		tester.assertComponent("form:addtable", Button.class);
		tester.assertComponent("tableview", WebMarkupContainer.class);
		tester.assertComponent("tableview:tables", ListView.class);
		tester.assertComponent("tableview:tables:0:cont", WebMarkupContainer.class);
		tester.assertComponent("tableview:tables:0:cont:name", AjaxEditableLabel.class);
		
		tester.assertComponent("tableview:tables:0:cont:sorted:table:0:col", Label.class);
		
		// カラムのコピー有無のチェックボックス
		tester.assertComponent("form:copycheck", AjaxCheckBox.class);
		
		tester.assertVisible("form:addtable");
		tester.assertVisible("tableview:tables:0:cont");
		tester.assertVisible("tableview:tables:0:cont:name");
		
	}
	
	/**
	 * テーブル名をクリックするとテーブル名を編集できることをテスト
	 */
	public void testTableRename() {
		tester.startPage(tdp);
		
//		tester.setParameterForNextRequest("tableview:tables:1:cont:name:editor", "phones");
//		
//		tester.assertLabel("tableview:tables:1:cont:name:label", "phones");
	}
	
	/**
	 * テーブル削除ボタンを押した後、テーブルが削除されているかのテスト
	 */
	public void testTableDelete() {
		
		tester.startPage(tdp);
		List<TableStructure> listable = new ArrayList<TableStructure>();
		listable.add(new TableStructure("emp0"));
		listable.add(new TableStructure("dept0"));

		tester.assertListView("tableview:tables", listable);
		
		tester.clickLink("tableview:tables:1:cont:delete");
		listable.remove(1);
		
		tester.assertListView("tableview:tables", listable);
		
	}
	
	/**
	 * テーブル追加ボタンを押した後、新しいテーブルが追加されているかどうかのテスト
	 * 新しいテーブルにはカラムが一つ必ず生成される。
	 */
	public void testTableAdd() {
		
		tester.startPage(tdp);
		
		String[] stcomp = {"EMPNO","ENAME", "JOB", "MGR", "HIREDATE", "SAL", "COMM","DEPTNO", "DNAME","DEPTNO","LOC"}; 
		
		tester.assertComponent("tableview:tables", ListView.class);
		tester.assertComponent("tableview:tables:0:cont:sorted:table", ListView.class);
		tester.assertComponent("tableview:tables:0:cont:sorted:table:0:col", Label.class);
		
		tester.assertVisible("tableview:tables");
		tester.assertVisible("tableview:tables:0:cont:sorted:table");
		tester.assertVisible("tableview:tables:0:cont:sorted:table:0:col");
		
		tester.assertComponent("tableview:tables:1:cont:sorted:table", ListView.class);
		tester.assertComponent("tableview:tables:1:cont:sorted:table:0:col", Label.class);
		tester.assertVisible("tableview:tables:1:cont:sorted:table");
		tester.assertVisible("tableview:tables:1:cont:sorted:table:0:col");
		
		for(int i = 0; i < stcomp.length; i++) {
			tester.assertContains(stcomp[i].toLowerCase());
		}
		
		Button b = (Button) tester.getComponentFromLastRenderedPage("form:addtable");
		tester.executeAjaxEvent(b, "onclick");
		
		for(int i = 0; i < stcomp.length; i++) {
			tester.assertContains(stcomp[i].toLowerCase());
		}
		tester.assertContains("new_table_1");
		// カラムも一つ追加される。
		tester.assertContains("new_column_1");
		
		tester.assertComponent("tableview:tables", ListView.class);
		tester.assertComponent("tableview:tables:2:cont:sorted:table", ListView.class);
		
		tester.executeAjaxEvent(b, "onclick");
		
		for(int i = 0; i < stcomp.length; i++) {
			tester.assertContains(stcomp[i].toLowerCase());
		}
		tester.assertContains("new_table_1");
		tester.assertContains("new_table_2");

	}
	
	/**
	 * テーブルに含まれるカラムをすべて削除したとき一緒にテーブルも削除されることをテスト
	 */
	public void testTableDeleteWithColumn() {
		tester.startPage(tdp);
		
		Button b = (Button) tester.getComponentFromLastRenderedPage("form:addtable");
		tester.executeAjaxEvent(b, "onclick");
		
		List<TableStructure> tslist = new ArrayList<TableStructure>();
		tslist.add(new TableStructure("emp0"));
		tslist.add(new TableStructure("dept0"));
		tslist.add(new TableStructure("new_table_1"));
		
		tester.assertListView("tableview:tables", tslist);
		
		tester.clickLink("tableview:tables:2:cont:create");
		tester.clickLink("tableview:tables:2:cont:sorted:table:0:del");
		
		//この時点ではテーブルに変化なし
		tester.assertListView("tableview:tables", tslist);
		
		tester.clickLink("tableview:tables:2:cont:sorted:table:0:del");
		tslist.remove(2);
		
		tester.assertListView("tableview:tables", tslist);
	}
	
	/**
	 * カラム削除ボタンが押された後、カラムが削除されているかのテスト
	 */
	public void testTableColumnDelete() {
		tester.startPage(tdp);
		String[] stcomp = {"EMPNO","ENAME", "JOB", "MGR", "HIREDATE", "SAL", "COMM","DEPTNO", "DNAME"};
		List<TableColumnM> tt = new ArrayList<TableColumnM>();
		
		for(int i = 0; i < stcomp.length; i++) {
			tt.add(new TableColumnM(stcomp[i].toLowerCase(), "", 0, null));
		}
		
		tester.assertListView("tableview:tables:0:cont:sorted:table", tt);
		
		tester.assertComponent("tableview:tables:0:cont:sorted:table:0:del", AjaxLink.class);
		tester.clickLink("tableview:tables:0:cont:sorted:table:0:del");
		tt.remove(0);
		
		tester.assertListView("tableview:tables:0:cont:sorted:table", tt);
		
		tester.clickLink("tableview:tables:0:cont:sorted:table:0:del");
		tt.remove(0);
		
		tester.assertListView("tableview:tables:0:cont:sorted:table", tt);
	}
	
	
	/**
	 * カラム編集ボタンを押した後、モーダルダイアログが開かれるかのテスト
	 */
	public void testTableColumnEdit() {
		tester.startPage(tdp);
		
		tester.assertComponent("tableview:tables:0:cont:sorted:table:0:edit", AjaxLink.class);

		tester.clickLink("tableview:tables:0:cont:sorted:table:0:edit");
				
		tester.assertComponent("modal", ModalWindow.class);
		tester.assertVisible("modal");
	
	}
	
	/**
	 * カラム追加ボタンが押された後、カラムが追加されているかのテスト
	 */
	public void testTableColumnAdd() {
		tester.startPage(tdp);
		
		String[] stcomp = {"EMPNO", "ENAME", "JOB", "MGR", "HIREDATE", "SAL", "COMM","DEPTNO", "DNAME"};
		List<TableColumnM> tt = new ArrayList<TableColumnM>();
		
		for(int i = 0; i < stcomp.length; i++) {
			tt.add(new TableColumnM(stcomp[i].toLowerCase(), "", 0, null));
		}
		
		tester.assertListView("tableview:tables:0:cont:sorted:table", tt);
		
		tester.clickLink("tableview:tables:0:cont:create");
		tt.add(new TableColumnM("new_column_1", "", 0, null));
		
		tester.assertListView("tableview:tables:0:cont:sorted:table", tt);
		
		tester.clickLink("tableview:tables:0:cont:create");
		tt.add(new TableColumnM("new_column_2", "", 0, null));
		
		tester.assertListView("tableview:tables:0:cont:sorted:table", tt);
		
		
	}
	
	
	/**
	 * サブミット後のデータベース書き換えテスト
	 * テーブルを追加した場合のテスト
	 */
	public void testSubmittedDatabaseTableAdd() {
//		TableDesignerPage td = new TableDesignerPage(param, dao);
//		tester.startPage(td);
//		
//		Button b = (Button) tester.getComponentFromLastRenderedPage("form:addtable");
//		tester.executeAjaxEvent(b, "onclick");
//		
//		tester.submitForm("form");
//		
//		ResultStruct rs = dao.read("select table_name, table_id, database_id from system.tables where" +
//				" table_name = 'new_table_1' and database_id = '00001'");
//		
//		assertFalse(rs.empty());
//		assertEquals(rs.getResult().get(0).getDataList().get(0).toUpperCase(), "NEW_TABLE_1");
//		
//		ResultStruct rs2 = dao.read("select table_name, column_name, data_type, data_precision from user_tab_columns where table_name = 'NEW_TABLE_1'");
//		
//		assertFalse(rs2.empty());
//		assertEquals(rs2.getResult().get(0).getDataList().get(0).toUpperCase(), "NEW_TABLE_1");
//		
//		dao.insert("delete from system.tables where table_name='new_table_1'");		
//		dao.insert("drop table new_table_1");

	}
	
	/**
	 * サブミットされた後はトップページに戻ることをテスト
	 */
	public void testSubmittedToTopPage() {
		tester.startPage(tdp);
		
		tester.submitForm("form");
		
		tester.assertRenderedPage(TopPage.class);
	}
	
	/**
	 * 主キーのテーブルにはclass属性にprimary+数字、外部キーのテーブルにはclass属性にforeign+数字
	 * が設定されることをテスト。
	 * 対応するprimaryとforeignの数字は一致する。
	 */
	public void testClassAttribute() {
		tester.startPage(tdp);
		
		List<TagTester> tlist = WicketTester.getTagsByWicketId(tester, "table");
		assertEquals(tlist.size(), 11);
		
		TagTester tagt = tlist.get(7);
		assertNotNull(tagt);
		
		/*
		 * classに付加するprimaryとforeignがモーダルウィンドウの実行後に付加されるので、
		 * この下のテストは実行できない・・・。
		 */
		
//		assertTrue(tagt.getAttribute("class").matches(".*foreign[0-9]+"));
//		
//		TagTester tagd = tlist.get(9);
//		assertNotNull(tagd);
//		
//		assertTrue(tagd.getAttribute("class").matches(".*primary[0-9]+"));
//		
//		String str1 = tagt.getAttribute("class").split(" ")[1];
//		String str2 = tagd.getAttribute("class").split(" ")[1];
//		
//		assertEquals(str1.substring(7), str2.substring(7));
	}
	
	/**
	 * 主キーが設定されているカラムは水色のラベル部分が青色になっていることをテスト
	 */
	public void testPrimaryColor() {
		tester.startPage(tdp);
		
		List<TagTester> tlist = WicketTester.getTagsByWicketId(tester, "sort");
		//primarykeyがあるのは10番め
		TagTester tagt = tlist.get(9);
		assertNotNull(tagt);
		
		assertTrue(tagt.getAttribute("style").equals("background-color: blue; color: blue;"));
		
		tagt = tlist.get(10);
		assertNotNull(tagt);
		
		assertTrue(tagt.getAttribute("style").equals("background-color: skyblue; color: skyblue;"));
		
	}
	
	/**
	 * トップページへ移動することをテスト
	 */
	public void testToTopPage() {
		tester.startPage(tdp);
		
		tester.clickLink("form:toppage");
		
		tester.assertRenderedPage(TopPage.class);
	}
	
	private class MockWicketApplication extends WicketApplication {
		@Override
		public Session newSession(Request request, Response response) {
			// TODO Auto-generated method stub
			LoginSession session = new LoginSession(request);
			session.authenticate("ikemotot", "password");
			
			return session;
		}
		
		public MockWicketApplication(IDao dao) {
			// TODO Auto-generated constructor stub
			super(dao);
		}
	}
}
