package com.tproject;

import java.io.Serializable;

/**
 * 統計情報格納用のモデル
 * @author no_known
 *
 */
public class Statistics implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int recursivecalls, dbblockgets, consistentgets, physicalreads, redosize,
				bytessentviasql, bytesreceivedviasql, sqlroundtrips, sortsmemory, sortsdisk, rowsprocessed;

	public int getRecursiveCalls() {
		return recursivecalls;
	}

	public void setRecursiveCalls(int recursivecalls) {
		this.recursivecalls = recursivecalls;
	}

	public int getDbBlockGets() {
		return dbblockgets;
	}

	public void setDbBlockGets(int dbblockgets) {
		this.dbblockgets = dbblockgets;
	}

	public int getConsistentGets() {
		return consistentgets;
	}

	public void setConsistentGets(int consistentgets) {
		this.consistentgets = consistentgets;
	}

	public int getPhysicalReads() {
		return physicalreads;
	}

	public void setPhysicalReads(int physicalreads) {
		this.physicalreads = physicalreads;
	}

	public int getRedoSize() {
		return redosize;
	}

	public void setRedoSize(int redosize) {
		this.redosize = redosize;
	}

	public int getBytesSentViaSQL() {
		return bytessentviasql;
	}

	public void setBytesSentViaSQL(int bytessentviasql) {
		this.bytessentviasql = bytessentviasql;
	}

	public int getBytesReceivedViaSQL() {
		return bytesreceivedviasql;
	}

	public void setBytesReceivedViaSQL(int bytesreceivedviasql) {
		this.bytesreceivedviasql = bytesreceivedviasql;
	}

	public int getSQLRoundtrips() {
		return sqlroundtrips;
	}

	public void setSQLRoundtrips(int roundtrips) {
		sqlroundtrips = roundtrips;
	}

	public int getSortsMemory() {
		return sortsmemory;
	}

	public void setSortsMemory(int sortsmemory) {
		this.sortsmemory = sortsmemory;
	}

	public int getSortsDisk() {
		return sortsdisk;
	}

	public void setSortsDisk(int sortsdisk) {
		this.sortsdisk = sortsdisk;
	}

	public int getRowsProcessed() {
		return rowsprocessed;
	}

	public void setRowsProcessed(int rowsprocessed) {
		this.rowsprocessed = rowsprocessed;
	}


}
