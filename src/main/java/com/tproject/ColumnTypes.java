package com.tproject;

import java.util.HashMap;
import java.util.Map;

/**
 * データ型とデータ表現形式の対応付け
 * @author no_known
 *
 */
public class ColumnTypes {
	
	private ColumnTypes() {
		// TODO Auto-generated constructor stub
	}

	enum TYPES {CHAR, DIGIT, TIME, BINARY}
	public static final Map<String, TYPES> COLUMN_TYPES = new HashMap<String, TYPES>();
	static {
		COLUMN_TYPES.put("VARCHAR2", TYPES.CHAR);
		COLUMN_TYPES.put("NVARCHAR2", TYPES.CHAR);
		COLUMN_TYPES.put("CHAR", TYPES.CHAR);
		COLUMN_TYPES.put("NCHAR", TYPES.CHAR);
		COLUMN_TYPES.put("LONG", TYPES.CHAR);
		COLUMN_TYPES.put("CLOB", TYPES.CHAR);
		COLUMN_TYPES.put("NUMBER", TYPES.DIGIT);
		COLUMN_TYPES.put("BINARY_FLOAT", TYPES.DIGIT);
		COLUMN_TYPES.put("BINARY_DOUBLE", TYPES.DIGIT);
		COLUMN_TYPES.put("DATE", TYPES.TIME);
		COLUMN_TYPES.put("TIMESTAMP", TYPES.TIME);
		COLUMN_TYPES.put("TIMESTAMP WITH TIMEZONE", TYPES.TIME);
		COLUMN_TYPES.put("TIMESTAMP WITH TIMEZONE", TYPES.TIME);
		COLUMN_TYPES.put("TIMESTAMP WITH LOCAL TIMEZONE", TYPES.TIME);
		COLUMN_TYPES.put("INTERVAL YEAR TO MONTH", TYPES.TIME);
		COLUMN_TYPES.put("INTERVAL DAY TO SECOND", TYPES.TIME);
		COLUMN_TYPES.put("RAW", TYPES.BINARY);
		COLUMN_TYPES.put("LONG RAW", TYPES.BINARY);
		COLUMN_TYPES.put("BLOB", TYPES.BINARY);
		COLUMN_TYPES.put("BFILE", TYPES.BINARY);
		COLUMN_TYPES.put("ROWID", TYPES.BINARY);
	}
}
