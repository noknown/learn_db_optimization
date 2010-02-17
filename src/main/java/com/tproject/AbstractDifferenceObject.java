package com.tproject;

import java.io.Serializable;

/**
 * オブジェクトリスト間の違いを表す抽象クラス
 * @author no_known
 *
 */
abstract class AbstractDifferenceObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Character differentchar;
	private String objectid;
	
	/**
	 * カラムの変更種類を表す文字を設定する。設定できる文字は<br/>
	 * 'A' -> 追加<br/>
	 * 'D' -> 削除<br/>
	 * 'U' -> データ変更<br/>
	 * の３文字のみである。
	 * それ以外の文字が渡された場合はnullを設定する。
	 * @param differentchar カラムの変更種類を表す１文字
	 */
	public void setDifferentChar(Character differentchar) {

		if(differentchar == null || differentchar != 'A' && differentchar != 'D' && differentchar != 'U') {
			this.differentchar = null;
		} else {
			this.differentchar = differentchar;
		}
		
	}

	/**
	 * カラムの変更種類を表す文字を取得する
	 * @return カラムの変更種類を表す１文字
	 */
	public Character getDifferentChar() {
		return differentchar;
	}

	public void setObjectId(String objectid) {
		this.objectid = objectid;
	}

	public String getObjectId() {
		return objectid;
	}
	

}
