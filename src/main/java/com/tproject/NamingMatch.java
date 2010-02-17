package com.tproject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Oracleの命名規約にしたがっているかを判断するクラス
 * @author no_known
 *
 */
public class NamingMatch {
	
	private static String[] reserveword = {
	"ACCESS",
	"ADD",
	"ALL",
	"ALTER",
	"AND",
	"ANY",
	"AS",
	"ASC",
	"AUDIT",
	"BETWEEN",
	"BY",
	"CHAR",
	"CHECK",
	"CLUSTER",
	"COLUMN",
	"COMMENT",
	"COMPRESS",
	"CONNECT",
	"CREATE",
	"CURRENT",
	"DATE",
	"DECIMAL",
	"DEFAULT",
	"DELETE",
	"DESC",
	"DISTINCT",
	"DROP",
	"ELSE",
	"EXCLUSIVE",
	"EXISTS",
	"FILE",
	"FLOAT",
	"FOR",
	"FROM",
	"GRANT",
	"GROUP",
	"HAVING",
	"IDENTIFIED",
	"IMMEDIATE",
	"IN",
	"INCREMENT",
	"INDEX",
	"INITIAL",
	"INSERT",
	"INTEGER",
	"INTERSECT",
	"INTO",
	"IS",
	"LEVEL",
	"LIKE",
	"LOCK",
	"LONG",
	"MAXEXTENTS",
	"MINUS",
	"MLSLABEL",
	"MODE",
	"MODIFY",
	"NOAUDIT",
	"NOCOMPRESS",
	"NOT",
	"NOWAIT",
	"NULL",
	"NUMBER",
	"OF",
	"OFFLINE",
	"ON",
	"ONLINE",
	"OPTION",
	"OR",
	"ORDER",
	"PCTFREE",
	"PRIOR",
	"PRIVILEGES",
	"PUBLIC",
	"RAW",
	"RENAME",
	"RESOURCE",
	"REVOKE",
	"ROW",
	"ROWID",
	"ROWNUM",
	"ROWS",
	"SELECT",
	"SESSION",
	"SET",
	"SHARE",
	"SIZE",
	"SMALLINT",
	"START",
	"SUCCESSFUL",
	"SYNONYM",
	"SYSDATE",
	"TABLE",
	"THEN",
	"TO",
	"TRIGGER",
	"UID",
	"UNION",
	"UNIQUE",
	"UPDATE",
	"USER",
	"VALIDATE",
	"VALUES",
	"VARCHAR",
	"VARCHAR2",
	"VIEW",
	"WHENEVER",
	"WHERE",
	"WITH"
	};
	
	/**
	 * 入力された文字列がOracleの命名規約にしたがっているかどうかを判断する。
	 * 
	 * @param string 判断される文字列
	 * @return Oracleの命名規約にしたがっているならtrue, それ以外はfalse
	 */
	public static boolean matching(String string) {
		if(string == null) return false;
		
		Pattern pattern2 = Pattern.compile("\".+\"");
		Matcher matcher2 = pattern2.matcher(string);
		if(matcher2.matches()) return true;
	
		if(string.length() > 30) return false;
		
		boolean bool = false;
		for(int i = 0; i < reserveword.length; i++) {
			if(string.equals(reserveword[i]) || string.equals(reserveword[i].toLowerCase())) {
				bool = true;
				break;
			}
		}
		if(bool) return false;

		Pattern pattern3 = Pattern.compile("^([ぁ-ヶ]|[亜-黑]|[a-zA-Z$#])([ぁ-ヶ]|[亜-黑]|[\\w$#])*");
		Matcher matcher3 = pattern3.matcher(string);
		
		return matcher3.matches();
	}
	
}
