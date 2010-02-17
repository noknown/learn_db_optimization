package com.tproject;

import java.util.HashMap;
import java.util.Map;

/**
 * データ型と長さの有効無効の設定
 * @author no_known
 *
 */
public class ColumnLength {
	
	enum LENGTH {YES, NO};
	public static final Map<String, LENGTH> COLUMN_LENGTH = new HashMap<String, LENGTH>();
	static {
		COLUMN_LENGTH.put("VARCHAR2", LENGTH.YES);
		COLUMN_LENGTH.put("NVARCHAR2", LENGTH.YES);
		COLUMN_LENGTH.put("CHAR", LENGTH.YES);
		COLUMN_LENGTH.put("NCHAR", LENGTH.YES);
		COLUMN_LENGTH.put("LONG", LENGTH.NO);
		COLUMN_LENGTH.put("CLOB", LENGTH.NO);
		COLUMN_LENGTH.put("NUMBER", LENGTH.YES);
		COLUMN_LENGTH.put("BINARY_FLOAT", LENGTH.NO);
		COLUMN_LENGTH.put("BINARY_DOUBLE", LENGTH.NO);
		COLUMN_LENGTH.put("DATE", LENGTH.NO);
		COLUMN_LENGTH.put("TIMESTAMP", LENGTH.NO);
		COLUMN_LENGTH.put("TIMESTAMP WITH TIMEZONE", LENGTH.NO);
		COLUMN_LENGTH.put("TIMESTAMP WITH TIMEZONE", LENGTH.NO);
		COLUMN_LENGTH.put("TIMESTAMP WITH LOCAL TIMEZONE", LENGTH.NO);
		COLUMN_LENGTH.put("INTERVAL YEAR TO MONTH", LENGTH.NO);
		COLUMN_LENGTH.put("INTERVAL DAY TO SECOND", LENGTH.NO);
		COLUMN_LENGTH.put("RAW", LENGTH.YES);
		COLUMN_LENGTH.put("LONG RAW", LENGTH.NO);
		COLUMN_LENGTH.put("BLOB", LENGTH.NO);
		COLUMN_LENGTH.put("BFILE", LENGTH.NO);
		COLUMN_LENGTH.put("ROWID", LENGTH.NO);
	}

}
