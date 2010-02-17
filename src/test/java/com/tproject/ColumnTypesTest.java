package com.tproject;

import junit.framework.TestCase;

public class ColumnTypesTest extends TestCase {
	
	final String[] str = new String[]{"VARCHAR2", "NVARCHAR2", "CHAR", "NCHAR",
			"LONG", "CLOB", "NUMBER", "BINARY_FLOAT", "BINARY_DOUBLE",
			"DATE", "TIMESTAMP", "TIMESTAMP WITH TIMEZONE", "TIMESTAMP WITH TIMEZONE",
			"TIMESTAMP WITH LOCAL TIMEZONE", "INTERVAL YEAR TO MONTH", 
			"INTERVAL DAY TO SECOND", "RAW", "LONG RAW", "BLOB", "BFILE", "ROWID"};
	
	public void testColumnTypes() {
		for(int i = 0; i < str.length; i++) {
			assertTrue(ColumnTypes.COLUMN_TYPES.containsKey(str[i]));
		}
	}

}
