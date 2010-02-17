package com.tproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * テーブルの差分情報クラス
 * @author no_known
 *
 */
public class DifferenceTable extends AbstractDifferenceObject  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tablename, tableid = "";
	private boolean tablenameflag;
	private List<DifferenceColumn> differencecolumns = new ArrayList<DifferenceColumn>();
	
	public void setTableName(String tablename) {
		this.tablename = tablename;
	}

	public String getTableName() {
		return tablename;
	}

	public void setTableNameFlag(boolean tablenameflag) {
		this.tablenameflag = tablenameflag;
	}

	public boolean isTableNameFlag() {
		return tablenameflag;
	}

	public void setDifferenceColumns(List<DifferenceColumn> differencecolumns) {
		this.differencecolumns = differencecolumns;
	}

	public List<DifferenceColumn> getDifferenceColumns() {
		return differencecolumns;
	}

	public void setTableId(String tableid) {
		if(tableid != null) this.tableid = tableid;
	}

	public String getTableId() {
		return tableid;
	}

}
