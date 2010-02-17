package com.tproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class TableName implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tablename;
	private String columnname;
	private String datatype;
	private String datalength;
	private String nullable;
	private boolean primary = false;
	private boolean foreign = false;
	//column_name（属性ネーム）,TABLE_NAME, DATA_TYPE（型）, DATA_LENGTH（データの長さ）
	public TableName(String tablename){
		setTablename(tablename);
	}
	public TableName(String columnname, String datatype, String datalength, String nullable,boolean primary,boolean foreign){
		setColumnname(columnname);
		setDatatype(datatype);
		setDatalength(datalength);
		
		if(nullable.equals("N"))nullable = "Required";
		else nullable = "";
		setNullable(nullable);
		
		setPrimary(primary);
		setForeign(foreign);
	 }
	
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public String getTablename() {
		return tablename;
	}
	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}
	public String getColumnname() {
		return columnname;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatalength(String datalength) {
		this.datalength = datalength;
	}
	public String getDatalength() {
		return datalength;
	}
	public void setNullable(String nullable) {
		this.nullable = nullable;
	}
	public String getNulllable() {
		return nullable;
	}
	public boolean isPrimary() {
		return primary;
	}
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}
	public boolean isForeign() {
		return foreign;
	}
	public void setForeign(boolean foreign) {
		this.foreign = foreign;
	}
}
