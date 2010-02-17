package com.tproject;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
/**
 * 実行計画情報、統計情報、応答時間情報をまとめるクラス
 * @author no_known
 *
 */
public class AnalyticsInformation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<ExecutionPlan> eplist;
	private Statistics statistics;
	private String responsetime;
	
	public AnalyticsInformation() {
		// TODO Auto-generated constructor stub
		eplist = new LinkedList<ExecutionPlan>();
		statistics = new Statistics();
		responsetime = "";
	}

	public void addExecutionPlan(ExecutionPlan executionplan) {
		// TODO Auto-generated method stub
		eplist.add(executionplan);
	}

	public List<ExecutionPlan> getExecutionList() {
		// TODO Auto-generated method stub
		return eplist;
	}

	public void setResponseTime(String responsetime) {
		// TODO Auto-generated method stub
		this.responsetime = responsetime;
	}

	public String getResponseTime() {
		// TODO Auto-generated method stub
		return responsetime;
	}

	public void setStatistics(Statistics statistics) {
		this.statistics = statistics;
	}

	public Statistics getStatistics() {
		return statistics;
	}

}
