package com.tproject;

import java.io.Serializable;
/**
 * 外部キー制約の情報を保持するモデルクラス
 * @author no_known
 *
 */
public class ForeignConstraint implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String constraintname, referencetable, referencetablecolumn,
					referenceconstraintname, deletetype;

	public ForeignConstraint() {
		// TODO Auto-generated constructor stub
		constraintname = null;
		referenceconstraintname = null;
		init();
	}

	/**
	 * 外部キー制約に登録された制約名を返す。
	 * @return 制約名
	 */
	public String getConstraintName() {
		return constraintname;
	}

	/**
	 * 新しい制約名を設定する。渡された制約名はOracleの命名規約にしたがっている
	 * ものだけが設定される。命名規約にしたがっていない制約名が渡された場合は
	 * 以前に設定された制約名のままとする。
	 * 
	 * 命名規約についてはNamingMatchクラスを参照。
	 * @param constraintname 制約名
	 */
	public void setConstraintName(String constraintname) {
		if(NamingMatch.matching(constraintname)) {
			this.constraintname = constraintname;
		}
	}

	/**
	 * 外部キーの参照先であるテーブル名を返す
	 * @return 外部キーの参照先テーブル名
	 */
	public String getReferenceTable() {
		return referencetable;
	}

	/**
	 * 新しい外部キーの参照先を設定する。渡されたテーブル名はOracleの命名規約にしたがった
	 * ものだけが設定される。命名規約にしたがっていないテーブル名が渡された場合は
	 * 以前に設定されたテーブル名のままとする。
	 * @param referencetable 外部キーの参照先テーブル名
	 */
	public void setReferenceTable(String referencetable) {
		if(NamingMatch.matching(referencetable)) {
			this.referencetable = referencetable;
		}
	}
	
	/**
	 * 新しい外部キーの参照先を設定する。渡されたカラム名はOracleの命名規約にしたがった
	 * ものだけが設定される。命名規約にしたがっていないカラム名が渡された場合は
	 * 以前に設定されたカラム名のままとする。
	 * @param referencetablecolumn 外部キーの参照先カラム名
	 */
	public void setReferenceTableColumn(String referencetablecolumn) {
		if(NamingMatch.matching(referencetablecolumn)) {
			this.referencetablecolumn = referencetablecolumn;
		}
	}

	/**
	 * 外部キーの参照先であるカラム名を返す
	 * @return 外部キーの参照先カラム名
	 */
	public String getReferenceTableColumn() {
		return referencetablecolumn;
	}

	/**
	 * 外部キーの参照先テーブルの制約名を返す
	 * @return 制約名
	 */
	public String getReferenceConstraintName() {
		return referenceconstraintname;
	}

	/**
	 * 新しい外部キーの参照先テーブル制約名を設定する。渡された制約名はOracleの
	 * 命名規約にしたがったものだけが設定される。命名規約にしたがっていない制約名
	 * が渡された場合は以前に設定されたテーブル名のままとする。
	 * @param referenceconstraintname 制約名
	 */
	public void setReferenceConstraintName(String referenceconstraintname) {
		if(NamingMatch.matching(referenceconstraintname)) {
			this.referenceconstraintname = referenceconstraintname;
		}
	}

	/**
	 * データを削除する際の参照操作名を返す
	 * @return 参照操作名
	 */
	public String getDeleteType() {
		return deletetype;
	}

	/**
	 * データを削除する際の参照操作名を設定する。参照操作名は
	 * <p>
	 *  NO ACTION<br/>
	 *  RESTRICT<br/>
	 *  SET NULL<br/>
	 *  SET DEFAULT<br/>
	 *  CASCADE<br/>
	 * </p>
	 * のいずれかである必要がある。もしそれ以外の文字列が渡された場合は以前に
	 * 設定された内容のままとする。
	 * 
	 * 渡された参照操作名の大文字小文字を区別しない。
	 * 
	 * @param deletetype 設定する参照操作名
	 */
	public void setDeleteType(String deletetype) {
		String[] str = new String[]{"NO ACTION", "RESTRICT", "SET NULL", "SET DEFAULT", "CASCADE"};
		
		if(deletetype == null) return;
		
		deletetype = deletetype.toUpperCase();
		
		boolean flag = false;
		for(int i = 0; i < str.length; i++) {
			if(deletetype.equals(str[i])) {
				flag = true;
				break;
			}
		}
		
		if(flag) {
			this.deletetype = deletetype;
		}
	}
	
	/**
	 * 初期化処理。参照先テーブル名、カラム名、削除タイプの初期化。
	 */
	public void init() {
		referencetable = null;
		referencetablecolumn = null;
		deletetype = "NO ACTION";
	}
	
	/**
	 * ForeignConstraintオブジェクトのディープコピーを行う。
	 * @return ディープコピーされたForeignConstraintオブジェクト
	 */
	public Object clone() {
		ForeignConstraint fc = new ForeignConstraint();
		
		if(constraintname != null) fc.setConstraintName(new String(constraintname));
		else fc.constraintname = null;
		if(referencetable != null) fc.setReferenceTable(new String(referencetable));
		else fc.referencetable = null;
		if(referencetablecolumn != null) fc.setReferenceTableColumn(new String(referencetablecolumn));
		else fc.referencetablecolumn = null;
		if(referenceconstraintname != null) fc.setReferenceConstraintName(new String(referenceconstraintname));
		else fc.referenceconstraintname = null;
		if(deletetype != null) 	fc.setDeleteType(new String(deletetype));
		else fc.deletetype = null;
		
		return fc;
	}
	
}
