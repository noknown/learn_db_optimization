package com.tproject;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
/**
 * 一つのデータベースを表すクラス
 * @author no_known
 *
 */
public class DatabaseStructure implements Serializable, Cloneable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<TableStructure> tables;
	private String databasename;
	
	public DatabaseStructure(String databaseName) {
		setDatabaseName(databaseName);
		this.tables = new LinkedList<TableStructure>();
	}
	
	/**
	 * データベース名を取得する。
	 * @return データベース名
	 */
	public String getDatabaseName() {
		return databasename;
	}

	/**
	 * データベース名を設定する。データベース名はoracleの命名規約にしたがったものだけが
	 * 設定され、それ以外は無視される。
	 * @param databasename 設定されるデータベース名
	 */
	public void setDatabaseName(String databasename) {
		if(NamingMatch.matching(databasename)) {
			this.databasename = databasename;
		}
	}
	
	/**
	 * テーブルの集合を取得する
	 * @return テーブルの集合
	 */
	public List<TableStructure> getTables() {
		return tables;
	}

	/**
	 * データベースにテーブルを追加する。
	 * もし既に（テーブル名が）同じテーブルが存在入っていれば、そのテーブルは無視する
	 * @param tablestructure 追加するテーブル
	 * @return テーブルが追加された時はtrue,されなければfalse
	 */
	public boolean addTable(TableStructure tablestructure) {
		// TODO Auto-generated method stub
		boolean bool = false;
		if(!this.tables.contains(tablestructure)) {
			this.tables.add(tablestructure);
			bool = true;
		}
		return bool;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub 
		return this.databasename.equals(((DatabaseStructure)obj).getDatabaseName());
	}

	/**
	 * ディープコピーの処理
	 */
	public Object clone() {
		DatabaseStructure ds = new DatabaseStructure(new String(databasename));
		
		for(TableStructure ts : tables) {
			ds.addTable((TableStructure)ts.clone());
		}
		
		return ds;
	}

}
