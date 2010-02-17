package com.tproject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * テーブル間の一つの関連に対して、一つの番号を付加するクラス。 
 * @author no_known
 *
 */
public class PrimaryForeignRelationship implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5015592729240907536L;

	public static final int UNDEFINED_RELATION = -1;

	private HashMap<String, Integer> relation = new HashMap<String, Integer>();
	private int counter = 0;
	
	/**
	 * リレーションを追加する
	 * リレーションが登録済みの場合はなにもしない。
	 * @param string 追加するリレーションの名前
	 */
	public void addRelation(String string) {
		// TODO Auto-generated method stub
		if(relation.containsKey(string.toUpperCase())) return;
		
		relation.put(string.toUpperCase(), new Integer(counter));
		counter++;
	}

	/**
	 * リレーションの番号を取得する
	 * @param string 番号を取得するリレーションの名前
	 * @return リレーション番号。リレーションが存在しない場合はUNDEFINED_RELATION
	 */
	public int getRelationNumber(String string) {
		// TODO Auto-generated method stub
		if(!relation.containsKey(string.toUpperCase())) return UNDEFINED_RELATION;
		
		return relation.get(string.toUpperCase());
	}

	
}
