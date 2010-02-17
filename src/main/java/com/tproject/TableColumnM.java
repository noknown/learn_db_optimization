package com.tproject;

import java.io.Serializable;

/**
 * レコード一行を表すクラス。
 * 
 * レコードが別テーブルに移動した際のデータ構造変更を
 * するためにレコードが所属するテーブルのIDを保持している。
 * 
 * @author no_known
 *
 */
public class TableColumnM implements Serializable, Cloneable, Identity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name, type, tablename;
	private Integer length, precisionlength;
	private ColumnConstraint constraint;
//	private IndexTypes index = IndexTypes.NONE;
	private static long columnnumber = 0;
	
//	final String[] str = new String[]{"VARCHAR2", "NVARCHAR2", "CHAR", "NCHAR",
//								"LONG", "CLOB", "NUMBER", "BINARY_FLOAT", "BINARY_DOUBLE",
//								"DATE", "TIMESTAMP", "TIMESTAMP WITH TIMEZONE", "TIMESTAMP WITH TIMEZONE",
//								"TIMESTAMP WITH LOCAL TIMEZONE", "INTERVAL YEAR TO MONTH", 
//								"INTERVAL DAY TO SECOND", "RAW", "LONG RAW", "BLOB", "BFILE", "ROWID"};
	
	public TableColumnM() {
		
		this.id = "nonID:" + columnnumber;
		columnnumber++;
		
		this.name = null;
		this.type = null;
		this.length = -1;
		this.precisionlength = -1;
		this.constraint = new ColumnConstraint();
		this.tablename = null;
	}
	
	public TableColumnM(String name, String type, int length,
			ColumnConstraint constraint) {
		
//		this.id = name + ":" + columnnumber;
//		columnnumber++;
//		
//		setName(name);
//		setType(type);
//		setLength(length);
//		setConstraint(constraint);
		this(name, type, length, 0 ,constraint, "dummy");
	}
	
	public TableColumnM(String name, String type, int length, int precisionlength, 
			ColumnConstraint constraint, String tablename) {
		this.id = name + ":" + columnnumber;
		columnnumber++;
		
		setName(name);
		setType(type);
		setLength(length);
		setPrecisionLength(precisionlength);
		setConstraint(constraint);
		setTableName(tablename);
	}
	
	/**
	 * カラム名を取得する。
	 * @return カラム名
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * カラム名を設定する。
	 * 渡された属性名はoracleの命名規約にしたがっているものだけが設定される。
	 * それ以外のものは無視する。
	 * @param name 設定するカラム名
	 */
	public void setName(String name) {
		if(NamingMatch.matching(name)) {
			this.name = name;
		}
	}
	
	/**
	 * カラムのデータ型名を取得する。
	 * @return カラムのデータ型名
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * カラムのデータ型名を設定する。
	 * 設定されたデータ型名はoracleで利用できるデータ型の場合だけ設定し、
	 * それ以外の場合は無視する。
	 * @param type 設定するカラムのデータ型名
	 */
	public void setType(String type) {
		type = type.toUpperCase();
		
		boolean bool = false;

		bool = ColumnTypes.COLUMN_TYPES.containsKey(type);
		
		if(bool) {
			this.type = type;
		}
	}
	
	/**
	 * データ型の列長の取得。
	 * @return データ型の列長
	 */
	public Integer getLength() {
		return length;
	}
	
	/**
	 * データ型の列長の設定。nullが渡された場合は0に変換する。
	 * @param length 設定するデータ型の列長
	 */
	public void setLength(Integer length) {
		if(length == null) length = new Integer(0);
		this.length = length;
	}
	
	/**
	 * データ型のスケールの取得。
	 * @return データ型のスケール
	 */
	public Integer getPrecisionLength() {
		return precisionlength;
	}
	
	/**
	 * データ型のスケールの設定。nullが渡された場合は0に変換する。
	 * データ型がNumber以外ならば0になる。
	 * スケールに設定できるのは-８４〜１２７まで。範囲を越えた場合は0に変換する。
	 * @param precisionlength 設定するデータ型のスケール
	 */
	public void setPrecisionLength(Integer precisionlength) {
		if(precisionlength == null) precisionlength = new Integer(0);
		if(type == null || !type.equals("NUMBER")) precisionlength = new Integer(0);
		if(precisionlength < -84 || precisionlength > 127) precisionlength = new Integer(0);
		this.precisionlength = precisionlength;
	}
	
	/**
	 * カラムにある参照制約を取得する
	 * @return 参照制約
	 */
	public ColumnConstraint getConstraint() {
		return constraint;
	}
	
	/**
	 * カラムにある参照制約を設定する
	 * @param constraint 設定する参照制約
	 */
	public void setConstraint(ColumnConstraint constraint) {
		this.constraint = constraint;
	}
	
	/**
	 * カラムが属しているテーブル名を取得する
	 * @return カラムが属しているテーブルの名前
	 */
	public String getTableName() {
		return tablename;
	}
	
	/**
	 * カラムが属しているテーブル名を設定する。
	 * @param tablename テーブルの名前
	 */
	public void setTableName(String tablename) {
		this.tablename = tablename;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		// 名前が同じなら同一。自分の名前が設定されていない時はfalse
		if(this.name == null) return false;
		return this.name.equals(((TableColumnM)obj).name);
	}

	/**
	 * ディープコピーの関数
	 * @return ディープコピーされたTableColumnMオブジェクト
	 */
	public Object clone() {
		TableColumnM tcm = new TableColumnM();
		tcm.id = id;
		
		if(name != null) tcm.setName(new String(name));
		else tcm.name = null;
		if(type != null) tcm.setType(new String(type));
		else tcm.type = null;
		if(length != null) tcm.setLength(new Integer(length));
		else tcm.type = null;
		if(precisionlength != null) tcm.setPrecisionLength(new Integer(precisionlength));
		else tcm.type = null;
		if(tablename != null) tcm.setTableName(new String(tablename));
		else tcm.tablename = null;
		
		if(constraint != null) tcm.setConstraint((ColumnConstraint)constraint.clone());
		else tcm.constraint = null;
		
		return tcm;
	}

	/**
	 * 各カラムに一意の値をとるIDを返す。
	 * nullになることはなく、cloneメソッドを用いたディープコピーでしか
	 * この値をコピーすることはできない。
	 * データの形式は　元の名前:数字　となっている。
	 * @return カラムのID
	 */
	public String getId() {
		return id;
	}

}
