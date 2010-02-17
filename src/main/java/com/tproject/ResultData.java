package com.tproject;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
/**
 * SQL結果を受け取るクラス
 * @author no_known
 *
 */
public class ResultData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> datalist;
	
	public ResultData(String[] datas) {
		datalist = new LinkedList<String>();
		
		for(int i = 0; i < datas.length; i++) {
			datalist.add(new String(datas[i] == null ? "" : datas[i]));
		}
	}

	/**
	 * SQL実行結果を要素ごとに文字列で保持したStringのListを返す
	 * @return 要素ごとの文字列List
	 */
	public List<String> getDataList() {
		// TODO Auto-generated method stub
		return datalist;
	}
}
