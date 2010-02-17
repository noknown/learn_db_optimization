package com.tproject;

import java.io.Serializable;

/**
 * カラム単位の変更箇所を表すクラス
 * 各情報が変更されていれば、対応するフラグの値がtrueとなる。
 * 変更されていなければfalseとなる。
 * @author no_known
 *
 */
public class DifferenceColumn extends AbstractDifferenceObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String columntablename, columnname, columntype, columnprimaryconstname, columncheck, columncheckconst,
		columndefault, columnforeignname, columnreferencetable, columnreferencetablecolumn, columnreferenceconstraintname,
		columndeletetype;
	private Integer columnlength, columnprecisionlength;
	private boolean columnprimary, columnunique, columnnotnull;
	private IndexTypes columnindex;
	
	// フラグ
	private boolean columnnameflag, columntypeflag, columnlengthflag, columnprecisionlengthflag, columnindexflag, columnprimaryflag,
	columnprimaryconstnameflag, columncheckflag, columndefaultflag, columnuniqueflag, columnnotnullflag, columnforeignflag;
//	columnForeignNameFlag,columnReferenceTableFlag, columnReferenceTableColumnFlag, columnReferenceConstraintNameFlag, columnDeleteTypeFlag;
	
	public String getColumnName() {
		return columnname;
	}

	public void setColumnName(String columnname) {
		this.columnname = columnname;
	}

	public String getColumnType() {
		return columntype;
	}

	public void setColumnType(String columntype) {
		this.columntype = columntype;
	}

	public boolean isColumnPrimary() {
		return columnprimary;
	}
	
	public void setColumnPrimary(boolean columnprimary) {
		this.columnprimary = columnprimary;
	}
	
	public String getColumnPrimaryConstName() {
		return columnprimaryconstname;
	}

	public void setColumnPrimaryConstName(String columnprimary) {
		this.columnprimaryconstname = columnprimary;
	}

	public String getColumnCheck() {
		return columncheck;
	}

	public void setColumnCheck(String columncheck) {
		this.columncheck = columncheck;
	}

	public String getColumnDefault() {
		return columndefault;
	}

	public void setColumnDefault(String columndefault) {
		this.columndefault = columndefault;
	}

	public String getColumnForeignName() {
		return columnforeignname;
	}

	public void setColumnForeignName(String foreignname) {
		this.columnforeignname = foreignname;
	}

	public String getColumnReferenceTable() {
		return columnreferencetable;
	}

	public void setColumnReferenceTable(String columnreferencetable) {
		this.columnreferencetable = columnreferencetable;
	}
	
	public String getColumnReferenceTableColumn() {
		return columnreferencetablecolumn;
	}
	
	public void setColumnReferenceTableColumn(String columnreferencetablecolumn) {
		this.columnreferencetablecolumn = columnreferencetablecolumn;
	}

	public String getColumnReferenceConstraintName() {
		return columnreferenceconstraintname;
	}

	public void setColumnReferenceConstraintName(
			String columnreferenceconstraintname) {
		this.columnreferenceconstraintname = columnreferenceconstraintname;
	}

	public String getColumnDeleteType() {
		return columndeletetype;
	}

	public void setColumnDeleteType(String columndeletetype) {
		this.columndeletetype = columndeletetype;
	}

	public Integer getColumnLength() {
		return columnlength;
	}

	public void setColumnLength(Integer columnlength) {
		this.columnlength = columnlength;
	}

	public boolean isColumnUnique() {
		return columnunique;
	}

	public void setColumnUnique(boolean columnUnique) {
		this.columnunique = columnUnique;
	}

	public boolean isColumnNotNull() {
		return columnnotnull;
	}

	public void setColumnNotNull(boolean columnnotnull) {
		this.columnnotnull = columnnotnull;
	}

	public boolean isColumnNameFlag() {
		return columnnameflag;
	}

	public void setColumnNameFlag(boolean columnameflag) {
		this.columnnameflag = columnameflag;
	}

	public boolean isColumnTypeFlag() {
		return columntypeflag;
	}

	public void setColumnTypeFlag(boolean columntypeflag) {
		this.columntypeflag = columntypeflag;
	}

	public boolean isColumnLengthFlag() {
		return columnlengthflag;
	}

	public void setColumnLengthFlag(boolean columnlengthglag) {
		this.columnlengthflag = columnlengthglag;
	}

	public boolean isColumnPrimaryFlag() {
		return columnprimaryflag;
	}

	public void setColumnPrimaryFlag(boolean columnprimaryflag) {
		this.columnprimaryflag = columnprimaryflag;
	}
	
	public boolean isColumnPrimaryConstNameFlag() {
		return columnprimaryconstnameflag;
	}

	public void setColumnPrimaryConstNameFlag(boolean columnprimaryflag) {
		this.columnprimaryconstnameflag = columnprimaryflag;
	}

	public boolean isColumnCheckFlag() {
		return columncheckflag;
	}

	public void setColumnCheckFlag(boolean columncheckflag) {
		this.columncheckflag = columncheckflag;
	}

	public boolean isColumnDefaultFlag() {
		return columndefaultflag;
	}

	public void setColumnDefaultFlag(boolean columndefaultflag) {
		this.columndefaultflag = columndefaultflag;
	}

	public boolean isColumnUniqueFlag() {
		return columnuniqueflag;
	}

	public void setColumnUniqueFlag(boolean columnuniqueflag) {
		this.columnuniqueflag = columnuniqueflag;
	}

	public boolean isColumnNotNullFlag() {
		return columnnotnullflag;
	}

	public void setColumnNotNullFlag(boolean columnnotnullflag) {
		this.columnnotnullflag = columnnotnullflag;
	}

	public void setColumnCheckConst(String columncheckconst) {
		this.columncheckconst = columncheckconst;
	}

	public String getColumnCheckConst() {
		return columncheckconst;
	}

	public void setColumnForeignFlag(boolean columnforeignflag) {
		this.columnforeignflag = columnforeignflag;
	}

	public boolean isColumnForeignFlag() {
		return columnforeignflag;
	}

	
	public void setColumnTableName(String columntableid) {
		this.columntablename = columntableid;
	}

	public String getColumnTableName() {
		return columntablename;
	}

	public void setColumnIndex(IndexTypes columnindex) {
		this.columnindex = columnindex;
	}

	public IndexTypes getColumnIndex() {
		return columnindex;
	}

	public void setColumnIndexFlag(boolean columnindexflag) {
		this.columnindexflag = columnindexflag;
	}

	public boolean isColumnIndexFlag() {
		return columnindexflag;
	}

	public void setColumnPrecisionLength(Integer columnprecisionlength) {
		this.columnprecisionlength = columnprecisionlength;
	}

	public Integer getColumnPrecisionLength() {
		return columnprecisionlength;
	}

	public void setColumnPrecisionLengthFlag(boolean columnprecisionlengthflag) {
		this.columnprecisionlengthflag = columnprecisionlengthflag;
	}

	public boolean isColumnPrecisionLengthFlag() {
		return columnprecisionlengthflag;
	}


}
