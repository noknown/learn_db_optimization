package com.tproject;

import java.io.Serializable;
import java.util.List;
/**
 * スキーマ構造とデータを格納するためのクラス
 * @author no_known
 *
 */
public class ResultStruct implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ResultData schema;
	private List<ResultData> result;
	private boolean empty;

	/**
	 * スキーマ情報を入力するメソッド
	 * @param schema スキーマ情報として入力するResultDataインスタンス
	 */
	public void setSchema(ResultData schema) {
		// TODO Auto-generated method stub

		this.schema = schema;
	}

	/**
	 * データを入力するメソッド
	 * @param result データとして入力するResultDataのリスト
	 */
	public void setResult(List<ResultData> result) {
		// TODO Auto-generated method stub
		
		boolean bool =false;
		for(int i = 0; i < result.size(); i++){
			
			ResultData rd = result.get(0);
			for(int j = 0; j < rd.getDataList().size(); j++) {
				if(!rd.getDataList().get(j).equals("")) bool = true;
			}
		}
		if(bool) {
			this.empty = false;
		} else {
			this.empty = true;
		}
		
		this.result = result;
	}

	/**
	 * スキーマ情報を取得するメソッド
	 * @return スキーマ情報を格納したResultData
	 */
	public ResultData getSchema() {
		// TODO Auto-generated method stub
		return schema;
	}

	/**
	 * データを取得するメソッド
	 * @return データを格納したResultDataのリスト
	 */
	public List<ResultData> getResult() {
		// TODO Auto-generated method stub
		return result;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return empty;
	}

}
