package com.tproject;

import java.util.List;

import org.apache.log4j.spi.LocationInfo;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;

/**
 * 統計情報を描画するためのパネル
 * @author no_known
 *
 */
public class AnalysisPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<AnalyticsInformation> analyticslist;
	
	private int minresponsetime = Integer.MAX_VALUE;
	
	public AnalysisPanel(String id, List<AnalyticsInformation> analyticslist) {
		super(id);
		// TODO Auto-generated constructor stub
		
//		alist = (List<AnalyticsInformation>)param.get("analysis");
		this.analyticslist = analyticslist;

		for(AnalyticsInformation a : this.analyticslist) {
			int time = convertTime(a.getResponseTime());
			
			if(minresponsetime > time && time != -1) minresponsetime = time;
		}
		
		add(new ListView<AnalyticsInformation>("plist", new PropertyModel<List<AnalyticsInformation>>(this, "analyticslist")) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<AnalyticsInformation> item) {
				// TODO Auto-generated method stub
				final AnalyticsInformation ai = item.getModelObject();

				item.add(new Label("responsetime", new PropertyModel<String>(ai, "responseTime"))
					.add(new AttributeModifier("style", new AbstractReadOnlyModel<String>() {

						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							// TODO Auto-generated method stub
							int time = convertTime(ai.getResponseTime());
							if(time != -1)
								return minresponsetime == convertTime(ai.getResponseTime()) ? "color:blue" : "";
							else
								return "";
						}
						
					})));
				
				item.add(new ListView<ExecutionPlan>("planlist", new PropertyModel<List<ExecutionPlan>>(ai, "executionList")) {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(ListItem<ExecutionPlan> items) {
						// TODO Auto-generated method stub
						ExecutionPlan ep = items.getModelObject();
						
						items.add(new Label("id", new PropertyModel<String>(ep, "id")));
						items.add(new Label("operation", new PropertyModel<String>(ep, "operation")));
						items.add(new Label("name", new PropertyModel<String>(ep, "name")));
						items.add(new Label("rowsn", new PropertyModel<String>(ep, "rows")));
						items.add(new Label("bytes", new PropertyModel<String>(ep, "bytes")));
						items.add(new Label("cost", new PropertyModel<String>(ep, "cost")));
						items.add(new Label("time", new PropertyModel<String>(ep, "time")));
						
					}
					
				});
				
				item.add(new Label("recursive", new PropertyModel<Integer>(ai, "statistics.recursiveCalls")));
				item.add(new Label("db_block", new PropertyModel<Integer>(ai, "statistics.dbBlockGets")));
				item.add(new Label("consistent", new PropertyModel<Integer>(ai, "statistics.consistentGets")));
				item.add(new Label("physical", new PropertyModel<Integer>(ai, "statistics.physicalReads")));
				item.add(new Label("redo", new PropertyModel<Integer>(ai, "statistics.redoSize")));
				item.add(new Label("bytes_sent", new PropertyModel<Integer>(ai, "statistics.bytesSentViaSQL")));
				item.add(new Label("bytes_received", new PropertyModel<Integer>(ai, "statistics.bytesReceivedViaSQL")));
				item.add(new Label("sqlnet", new PropertyModel<Integer>(ai, "statistics.SQLRoundtrips")));
				item.add(new Label("sortsmemory", new PropertyModel<Integer>(ai, "statistics.sortsMemory")));
				item.add(new Label("sortsdisk", new PropertyModel<Integer>(ai, "statistics.sortsDisk")));
				item.add(new Label("rows", new PropertyModel<Integer>(ai, "statistics.rowsProcessed")));
				
			}
			
		});
	}
	
	/**
	 * 時間表現の文字列をミリ秒単位に変換する。
	 * @param time 時間表現の文字列　00:00:01.01など
	 * @return ミリ単位に変換された時間 101など。変換できない文字列が渡された場合は-1
	 */
	public int convertTime(String time) {
		
		if(!time.matches("[\\d][\\d]:[\\d][\\d]:[\\d][\\d].[\\d][\\d]")) return -1;
		
		String[] times = time.split(":");
		
		return Integer.parseInt(times[0])*360000+Integer.parseInt(times[1])*6000+
				(int)(Double.parseDouble(times[2])*100);
	}
	
	/**
	 * AnalyticsInformationのリストを更新する処理。
	 * @param analyticslist 更新するAnalyticsInformationのリスト
	 */
	public void setAnalysisList(List<AnalyticsInformation> analyticslist) {
		this.analyticslist = analyticslist;
		
		minresponsetime = Integer.MAX_VALUE;
		for(AnalyticsInformation a : this.analyticslist) {
			int time = convertTime(a.getResponseTime());
			
			if(minresponsetime > time && time != -1) minresponsetime = time;
		}
	}
}
