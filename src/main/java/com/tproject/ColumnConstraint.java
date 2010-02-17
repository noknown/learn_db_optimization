package com.tproject;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 各テーブル属性の参照制約を保持するクラス
 * @author no_known
 *
 */
public class ColumnConstraint implements Serializable, Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean unique, notnull, primary;
	private String check, checkconstname, primaryconstname, defaults;
	private ForeignConstraint foreign;
	private IndexTypes index = IndexTypes.NONE;
	
	public ColumnConstraint() {
		
		unique = false;
		notnull = false;
		check = null;
		primary = false;
		primaryconstname = null;
		defaults = null;
		foreign = new ForeignConstraint();
	}

	/**
	 * 一意性規約の有無を返す
	 * @return 一意性規約があればtrue, なければfalse
	 */
	public boolean isUnique() {
		return unique;
	}

	/**
	 * 一意性規約の有無を設定する。
	 * 一意性規約を有効にしたい場合はtrue,無効にしたい場合はfalse
	 * @param unique 一意性規約の有無
	 */
	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	/**
	 * not null規約の有無を返す
	 * @return not null規約があればtrue, なければfalse
	 */
	public boolean isNotnull() {
		return notnull;
	}

	/**
	 * not null規約の有無を設定する。
	 * not null規約を有効にしたい場合はtrue, 無効にしたい場合はfalse
	 * @param notnull not null規約の有無
	 */
	public void setNotnull(boolean notnull) {
		this.notnull = notnull;
	}

	/**
	 * check制約の内容を返す
	 * @return check制約の内容。check規約がない場合はnull
	 */
	public String getCheck() {
		return check;
	}

	/**
	 * check規約を設定する。
	 * 渡された文字列が不正な文字列だった場合は渡された値を無視する
	 * nullが渡された場合はnullを設定する。
	 * @param check 設定するcheck規約
	 */
	public void setCheck(String check) {
		if(check == null) {
			this.check = null; return;
		}
		
		Pattern pattern = Pattern.compile(".*(>=|<=|=|>|<).*");
		Matcher matcher = pattern.matcher(check);
		
		if(matcher.matches()){
			this.check = check;
		}
	}

	/**
	 * 主キー制約の制約名を設定する。渡された制約名はOracleの命名規約にしたがっている
	 * ものだけが設定される。命名規約にしたがっていない制約名が渡された場合は
	 * 以前に設定された制約名のままとする。
	 * nullが渡された場合はnullを設定する
	 * @param primary 主キー制約の制約名
	 */
	public void setPrimaryConstName(String primary) {
		if(primary == null) {
			this.primaryconstname = null;
			return;
		}
		
		if(NamingMatch.matching(primary)) {
			this.primaryconstname = primary;
		}
	}

	/**
	 * 主キー制約の制約名を返す。
	 * @return 主キー制約がある場合は制約名、ない場合はnull
	 */
	public String getPrimaryConstName() {
		return primaryconstname;
	}

	/**
	 * 主キー制約の有無を設定する
	 * @param primary 制約を有効にするならtrue,してないならfalse
	 */
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	/**
	 * 主キー制約の有無を返す
	 * @return 主キー制約がある場合はtrue,ない場合はfalse
	 */
	public boolean isPrimary() {
		return primary;
	}
	
	/**
	 * 外部キー制約を設定する。nullが渡された場合はnullを設定する
	 * @param foreign 設定する外部キー制約
	 */
	public void setForeign(ForeignConstraint foreign) {
			this.foreign = foreign;
	}

	/**
	 * 外部キー制約を返す。
	 * @return 外部キー制約があるならばForeignConstraintクラスのオブジェクト
	 * なければnull
	 */
	public ForeignConstraint getForeign() {
		return foreign;
	}

	/**
	 * デフォルト値を設定する。数字、文字列、日時以外のものは無視される。
	 * null、もしくは空文字列が渡されるとnullを設定する。
	 * @param defaults 設定するデフォルト値
	 */
	public void setDefault(String defaults) {
		if(defaults == null || defaults.equals("")) {
			this.defaults = null;
			return;
		}
		
		if(!defaults.equals("")) {
			this.defaults = defaults;
		}
	}

	/**
	 * デフォルト値を返す。
	 * @return デフォルト値を設定していればデフォルト値、設定していなければnull
	 */
	public String getDefault() {
		return this.defaults;
	}
	
	/**
	 * Check制約の制約名を設定する。渡された制約名はOracleの命名規約にしたがっている
	 * ものだけが設定される。命名規約にしたがっていない制約名が渡された場合は
	 * 以前に設定された制約名のままとする。
	 * nullが渡された場合は無視する
	 * @param checkconstname 設定するチェック制約名
	 */
	public void setCheckConstName(String checkconstname) {

		if(NamingMatch.matching(checkconstname)) {
			this.checkconstname = checkconstname;
		}
	}

	/**
	 * Check制約名を返す。
	 * @return チェック制約が設定されていれば制約名、されていなければnull
	 */
	public String getCheckConstName() {
		return checkconstname;
	}
	
	/**
	 * 索引を設定する。indexの値がnullの場合は無視する
	 * @param index 設定する索引の種類
	 */
	public void setIndex(IndexTypes index) {
		if(index == null) return;
		this.index = index;
	}

	/**
	 * 索引を取得する
	 * @return 索引の種類
	 */
	public IndexTypes getIndex() {
		return index;
	}
	
	/**
	 * ディープコピーされたColumnConstraintオブジェクトを返す
	 * @return ディープコピーされたColumnConstraintオブジェクト
	 */
	public Object clone() {
		ColumnConstraint cc = new ColumnConstraint();
		
		cc.setIndex(index);
		cc.setPrimary(primary);
		if(primaryconstname != null) cc.setPrimaryConstName(new String(primaryconstname));
		else cc.primaryconstname = null;
		if(check != null) cc.setCheck(new String(check));
		else cc.check = null;
		if(checkconstname != null) cc.setCheckConstName(new String(checkconstname));
		else cc.checkconstname = null;
		if(defaults != null) cc.setDefault(new String(defaults));
		else cc.defaults = null;
		cc.setNotnull(notnull);
		cc.setUnique(unique);
		
		if(foreign != null) cc.setForeign((ForeignConstraint)foreign.clone());
		else foreign = null;
		
		return cc;
	}


}
