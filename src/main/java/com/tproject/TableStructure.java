package com.tproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * テーブル構造を表すクラス
 * @author no_known
 *
 */
public class TableStructure implements Serializable, Cloneable, Identity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static long tablenumber = 0;
	
	private String id, tableid = "";
	private String tablename;
	private List<TableColumnM> schema;
	
	public TableStructure(String tablename) {
		// TODO Auto-generated constructor stub
		this.id = tablename + ":" + tablenumber;
		tablenumber++;
		
		setTableName(tablename);
		schema = new LinkedList<TableColumnM>();
	}

	/**
	 * テーブルIDを設定する
	 * @param tableid 設定するテーブルID
	 */
	public void setTableId(String tableid) {
		if(tableid != null) this.tableid = tableid;
	}

	/**
	 * テーブルIDを取得する
	 * @return 取得するテーブルID
	 */
	public String getTableId() {
		return tableid;
	}
	
	/**
	 * テーブル名を取得する
	 * @return テーブル名
	 */
	public String getTableName() {
		return tablename;
	}
	
	/**
	 * テーブル名を設定する。テーブル名はoracleの命名規約にしたがっているものだけが設定される。
	 * したがっていないテーブル名は無視される。
	 * @param tablename 設定するテーブル名
	 */
	public void setTableName(String tablename) {
		if(NamingMatch.matching(tablename)) {
			this.tablename = tablename;
		}
	}
	
	/**
	 * テーブルに設定されたカラムの集合を取得する
	 * @return カラムの集合
	 */
	public List<TableColumnM> getSchemas() {
		return schema;
	}
	
	/**
	 * テーブルにカラムを追加する。もし既に（カラム名が）同じカラムが入っていた
	 * 場合は、そのカラムは無視する。
	 * @param column 追加するカラム
	 * @return カラムが追加された場合はtrue,追加されなかった場合はfalseを返す。
	 */
	public boolean addSchema(TableColumnM column) {
		boolean bool = false;
		if(!this.schema.contains(column)) {
			this.schema.add(column);
			bool = true;
		}
		return bool;
	}
	
	/**
	 * テーブルからカラムを削除する。もし与えられたカラムがテーブル中になければ、なにもしない。
	 * @param column 削除するカラム
	 */
	public void removeSchema(TableColumnM column) {
		// TODO Auto-generated method stub
		if(this.schema.contains(column))
		this.schema.remove(column);
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		// 自分の名前が設定されていなければfalse
		if(this.tablename == null) return false;
		return this.tablename.equals(((TableStructure)obj).getTableName());
	}
	
	/**
	 * ディープコピーの処理
	 */
	public Object clone() {
		TableStructure ts = new TableStructure(new String(tablename));
		ts.id = id;
		ts.tableid = new String(tableid);
		
		for(TableColumnM tcm : schema) {
			ts.addSchema((TableColumnM)tcm.clone());
		}
		
		return ts;
	}

	/**
	 * 各テーブルに一意の値をとるIDを返す。
	 * nullになることはなく、cloneメソッドを用いたディープコピーでしか
	 * この値をコピーすることはできない。
	 * @return テーブルのID
	 */
	public String getId() {
		return id;
	}

	/**
	 * テーブル内に主キーがあるかどうかを判断する。
	 * @return 主キーの設定されたカラムネームリスト
	 */
	public List<String> getPrimarycolumn() {
		// TODO Auto-generated method stub
		List<String> slist = new ArrayList<String>();
		
		for(TableColumnM tcm : schema) {
			if(tcm.getConstraint().isPrimary()) slist.add(tcm.getName());
		}

		return slist;
	}

	/**
	 * テーブル内に外部キー制約があるかどうかを判断する。
	 * @return 外部キーの設定されたカラムネームリスト
	 */
	public List<String> getForeigncolumn() {
		// TODO Auto-generated method stub
		List<String> slist = new ArrayList<String>();
		
		for(TableColumnM tcm : schema) {
			if(tcm.getConstraint().getForeign().getReferenceTableColumn() != null) slist.add(tcm.getConstraint().getForeign().getReferenceTableColumn());
		}
		
		return slist;
	}

	/**
	 * あるテーブル間との関連性があるかどうかを判断する。
	 * @param tablestruct 関連性の有無を調べるテーブル
	 * @return 関連性があればtrue,それ以外はfalse
	 */
	public boolean isRelation(TableStructure tablestruct) {
		// TODO Auto-generated method stub
		boolean flag = false;
		
		if(tablestruct == null) return false;
		
		for(TableColumnM tcm : tablestruct.getSchemas()) {
			String tablename;
			String refername;
			
			if(this.tablename != null && tcm.getConstraint().getForeign().getReferenceTable() != null) {
			
				tablename = this.tablename.toUpperCase();
				refername = tcm.getConstraint().getForeign().getReferenceTable().toUpperCase();

				if(tablename.equals(refername)) flag = true;
			}
			
			if(this.tablename != null && tcm.getTableName() != null) {
			
				tablename = this.tablename.toUpperCase();
				refername = tcm.getTableName().toUpperCase();

				if(tablename.equals(refername)) flag = true;

			}
			
		}
		
		for(TableColumnM tcm : this.schema) {
			String tablename;
			String refername;
			
			if(tablestruct.tablename != null && tcm.getConstraint().getForeign().getReferenceTable() != null) {

				tablename = tablestruct.tablename.toUpperCase();
				refername = tcm.getConstraint().getForeign().getReferenceTable().toUpperCase();

				if(tablename.equals(refername)) flag = true;
			}
			
			if(tablestruct.tablename != null && tcm.getTableName() != null) {

				tablename = tablestruct.tablename.toUpperCase();
				refername = tcm.getTableName().toUpperCase();

				if(tablename.equals(refername)) flag = true;
			}
		}
		
		return flag;
	}
	

	

}
