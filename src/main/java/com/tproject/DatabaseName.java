package com.tproject;

import java.io.Serializable;

public class DatabaseName implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String databasename;
	private String createday;
	private String updateday;
	
	public DatabaseName(String databasename,String createday,String updateday) {
		this.databasename = databasename;
		this.createday = createday;
		this.updateday = updateday;
		
	}
	public String getDatabaseName() {
		return databasename;
	}
	public void setDatabaseName(String databasename){
		this.databasename = databasename ;
	}
	public void setCreateday(String createday) {
		this.createday = createday;
	}
	public String getCreateday() {
		return createday;
	}
	public void setUpdateday(String updateday) {
		this.updateday = updateday;
	}
	public String getUpdateday() {
		return updateday;
	}
}
