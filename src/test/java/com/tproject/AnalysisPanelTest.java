package com.tproject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.BaseWicketTester;
import org.apache.wicket.util.tester.TagTester;
import org.apache.wicket.util.tester.TestPanelSource;
import org.apache.wicket.util.tester.WicketTester;

import junit.framework.TestCase;

/**
 * 
 * @author no_known
 *
 */
public class AnalysisPanelTest extends TestCase {
	
	AnalysisPanel ap;
	WicketTester tester;
	List<AnalyticsInformation> alist;
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		tester = new WicketTester(new WicketApplication(UserDao.newInstance("scott", "tiger")));
		
		alist = new ArrayList<AnalyticsInformation>();
		AnalyticsInformation ai1 = new AnalyticsInformation();
		ai1.setResponseTime("00:00:00.01");
		Statistics s = new Statistics();
		s.setRecursiveCalls(100);
		s.setBytesReceivedViaSQL(10);
		ai1.addExecutionPlan(new ExecutionPlan());
		ai1.setStatistics(s);
		
		AnalyticsInformation ai2 = new AnalyticsInformation();
		ai2.setResponseTime("00:00:10.00");
		ExecutionPlan ep = new ExecutionPlan();
		ep.setId("  3");
		ep.setOperation("FFFDDD");
		ai2.setStatistics(new Statistics());
		ai2.addExecutionPlan(ep);
		
		AnalyticsInformation ai3 = new AnalyticsInformation();
		ai3.setResponseTime("00:00:00.01");
		
		alist.add(ai1);
		alist.add(ai2);
		alist.add(ai3);
		
//		final PageParameters pp = new PageParameters();
//		pp.put("analysis", alist);
		
		tester.startPanel(new TestPanelSource() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Panel getTestPanel(String panelId) {
				// TODO Auto-generated method stub
				return ap = new AnalysisPanel(panelId, alist);
			}

			
		});
	}
	
	/**
	 * AnalysisPanelが描画されるかのテスト
	 */
	public void testRendered() {
		tester.assertComponent("panel:plist", ListView.class);
		tester.assertComponent("panel:plist:0:responsetime", Label.class);
		
		tester.assertComponent("panel:plist:0:planlist", ListView.class);
		tester.assertComponent("panel:plist:0:planlist:0:id", Label.class);
		tester.assertComponent("panel:plist:0:planlist:0:operation", Label.class);
		tester.assertComponent("panel:plist:0:planlist:0:name", Label.class);
		tester.assertComponent("panel:plist:0:planlist:0:rowsn", Label.class);
		tester.assertComponent("panel:plist:0:planlist:0:bytes", Label.class);
		tester.assertComponent("panel:plist:0:planlist:0:cost", Label.class);
		tester.assertComponent("panel:plist:0:planlist:0:time", Label.class);
		
		tester.assertComponent("panel:plist:0:recursive", Label.class);
		tester.assertComponent("panel:plist:0:db_block", Label.class);
		tester.assertComponent("panel:plist:0:consistent", Label.class);
		tester.assertComponent("panel:plist:0:physical", Label.class);
		tester.assertComponent("panel:plist:0:redo", Label.class);
		tester.assertComponent("panel:plist:0:bytes_sent", Label.class);
		tester.assertComponent("panel:plist:0:bytes_received", Label.class);
		tester.assertComponent("panel:plist:0:sqlnet", Label.class);
		tester.assertComponent("panel:plist:0:sortsmemory", Label.class);
		tester.assertComponent("panel:plist:0:sortsdisk", Label.class);
		tester.assertComponent("panel:plist:0:rows", Label.class);
		
		tester.assertLabel("panel:plist:0:responsetime", "00:00:00.01");
		tester.assertLabel("panel:plist:1:responsetime", "00:00:10.00");
		tester.assertLabel("panel:plist:2:responsetime", "00:00:00.01");
		
		tester.assertLabel("panel:plist:0:recursive", "100");
		tester.assertLabel("panel:plist:0:bytes_received", "10");
		tester.assertLabel("panel:plist:0:redo", "0");
		
		tester.assertLabel("panel:plist:1:planlist:0:id", "  3");
		tester.assertLabel("panel:plist:1:planlist:0:operation", "FFFDDD");
		tester.assertLabel("panel:plist:0:planlist:0:rowsn", "");
		
	}
	
	/**
	 * 時間の文字列を数字に変換するテスト
	 * 00:00:01.00 -> 100;
	 * 00:01:00.00 -> 6000;
	 * もしフォーマットに合っていない文字列が渡された場合は-1を返す。
	 */
	public void testConvertTime() {
		
		try {
			assertEquals(ap.convertTime("00:00:01.00"), 100);
			assertEquals(ap.convertTime("00:01:00.00"), 6000);
			assertEquals(ap.convertTime("01:10:46.22"), 424622);
		} catch(Exception e) {
			fail();
		}
		
		assertEquals(ap.convertTime(""), -1);
		assertEquals(ap.convertTime("ffesaooe"), -1);
	}
	
	/**
	 * すべてのレスポンスタイムの中で値がもっとも小さいものが青色で表示されるかのテスト
	 */
	public void testMinResponseTimeColorBlue() {
		List<TagTester> tlist = BaseWicketTester.getTagsByWicketId(tester, "responsetime");
		
		TagTester tag0 = tlist.get(0);
		TagTester tag1 = tlist.get(1);
		assertNotNull(tag0);
		assertNotNull(tag1);
		
		assertTrue(tag0.hasAttribute("style"));
		assertEquals(tag0.getAttribute("style"), "color:blue");
		assertTrue(tag1.hasAttribute("style"));
		assertEquals(tag1.getAttribute("style"), "");
		
	}
	
	/**
	 * AnalyticsInformationのリストを更新する。
	 */
	public void testSetAnalyticsInformation() {
		List<AnalyticsInformation> alist = new ArrayList<AnalyticsInformation>();
		AnalyticsInformation an = new AnalyticsInformation();
		an.setResponseTime("00:00:00:22");
		alist.add(an);
		
		ap.setAnalysisList(alist);
		
	}
	
}
